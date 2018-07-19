"""
在实践中，如果视频不切割成每秒的小视频。 得到的tag。无法切割镜头。也就是说只能1个镜头
"""
import os
import sys

import numpy
import tensorflow as tf
from tensorflow import app
from tensorflow import flags
from urllib.parse import quote

import cv_video_helper
import feature_extractor

FLAGS = flags.FLAGS

# In OpenCV3.X, this is available as cv2.CAP_PROP_POS_MSEC
# In OpenCV2.X, this is available as cv2.cv.CV_CAP_PROP_POS_MSEC
CAP_PROP_POS_MSEC = 0
VIDEO_FORMAT = ['.mp4', '.MP4']

if __name__ == '__main__':
    # Required flags for input and output.
    flags.DEFINE_string('output_tfrecords_file', None,
                        'File containing tfrecords will be written at this path.')
    flags.DEFINE_string('input_videos_csv', None,
                        'CSV file with lines "<video_file>,<labels>", where '
                        '<video_file> must be a path of a video and <labels> '
                        'must be an integer list joined with semi-colon ";"')
    # Optional flags.
    flags.DEFINE_string('model_dir', os.path.join(format(os.getenv('HOME')), 'yt8m'),
                        'Directory to store model files. It defaults to ~/yt8m')

    # The following flags are set to match the YouTube-8M dataset format.
    flags.DEFINE_integer('frames_per_second', 1,
                         'This many frames per second will be processed')
    flags.DEFINE_string('labels_feature_key', 'labels',
                        'Labels will be written to context feature with this '
                        'key, as int64 list feature.')
    flags.DEFINE_string('image_feature_key', 'rgb',
                        'Image features will be written to sequence feature with '
                        'this key, as bytes list feature, with only one entry, '
                        'containing quantized feature string.')
    flags.DEFINE_string('video_file_key_feature_key', 'video_id',
                        'Input <video_file> will be written to context feature '
                        'with this key, as bytes list feature, with only one '
                        'entry, containing the file path of the video. This '
                        'can be used for debugging but not for training or eval.')
    flags.DEFINE_boolean('insert_zero_audio_features', True,
                         'If set, inserts features with name "audio" to be 128-D '
                         'zero vectors. This allows you to use YouTube-8M '
                         'pre-trained model.')


def _int64_list_feature(int64_list):
    return tf.train.Feature(int64_list=tf.train.Int64List(value=int64_list))


def _bytes_feature(value):
    return tf.train.Feature(bytes_list=tf.train.BytesList(value=[value]))


def _make_bytes(int_array):
    if bytes == str:  # Python2
        return ''.join(map(chr, int_array))
    else:
        return bytes(int_array)


def quantize(features, min_quantized_value=-2.0, max_quantized_value=2.0):
    """Quantizes float32 `features` into string."""
    assert features.dtype == 'float32'
    assert len(features.shape) == 1  # 1-D array
    features = numpy.clip(features, min_quantized_value, max_quantized_value)
    quantize_range = max_quantized_value - min_quantized_value
    features = (features - min_quantized_value) * (255.0 / quantize_range)
    features = [int(round(f)) for f in features]

    return _make_bytes(features)


def printError(msg):
    print(msg, file=sys.stderr)


def process(videofile, tfrecordDir=''):
    if not os.path.exists(tfrecordDir):
        os.makedirs(tfrecordDir)
    file_arr = os.path.splitext(videofile)
    if (tfrecordDir):  # valid
        filename = os.path.basename(videofile).split(".")[0]
        output_tfrecords_file = "%s_output.tfrecord" % (tfrecordDir + '/' + str(filename))
    else:
        output_tfrecords_file = "%s_output.tfrecord" % file_arr[0]
    FLAGS.output_tfrecords_file = output_tfrecords_file
    print("output_tfrecords_file", output_tfrecords_file)

    extractor = feature_extractor.YouTube8MFeatureExtractor(FLAGS.model_dir)
    writer = tf.python_io.TFRecordWriter(FLAGS.output_tfrecords_file)

    total_written = 0
    total_error = 0
    labels = "0"
    vh = cv_video_helper.VideoHelper()

    for num_retrieved, rgb in vh.frame_iterator(videofile, every_ms=1000.0 / FLAGS.frames_per_second):
        rgb_features = []
        features = extractor.extract_rgb_frame_features(rgb[:, :, ::-1])
        rgb_features.append(_bytes_feature(quantize(features)))
        if not rgb_features:
            printError('Could not get features for ' + videofile + ' ,frame order is ' + num_retrieved)
            total_error += 1
            continue

        # Create SequenceExample proto and write to output.
        feature_list = {
            FLAGS.image_feature_key: tf.train.FeatureList(feature=rgb_features),
        }
        if FLAGS.insert_zero_audio_features:
            feature_list['audio'] = tf.train.FeatureList(
                feature=[_bytes_feature(_make_bytes([0] * 128))] * len(rgb_features))

        # print(feature_list)
        # E:/work/ai_script/tmp.MP4 -> E:/work/ai_script/tmp/%d.MP4
        # url encode
        virtualPath = quote(file_arr[0]) + '/' + str(num_retrieved) + file_arr[1]
        print("virtualPath = ", virtualPath)
        example = tf.train.SequenceExample(
            context=tf.train.Features(feature={
                FLAGS.labels_feature_key:
                    _int64_list_feature(sorted(map(int, labels.split(';')))),
                FLAGS.video_file_key_feature_key:
                    _bytes_feature(_make_bytes(map(ord, virtualPath))),
            }),
            feature_lists=tf.train.FeatureLists(feature_list=feature_list))
        writer.write(example.SerializeToString())
        total_written += 1

    writer.close()
    print('Successfully encoded %i out of %i videos' % (total_written, total_written + total_error))


#################################################
if len(sys.argv) < 3:
    printError("Error!")
else:
    video_file = sys.argv[1].strip()
    out_dir = sys.argv[2].strip()

    if os.path.isdir(video_file):
        for file in os.listdir(video_file):
            file_path = os.path.join(video_file, file)
            if file_path.startswith("."):
                continue
            if not os.path.isfile(file_path):
                continue
            extension = os.path.splitext(file_path)[1]  # get the extension of file
            if extension not in VIDEO_FORMAT:
                continue
            process(file_path, out_dir)
    else:
        process(video_file, out_dir)
