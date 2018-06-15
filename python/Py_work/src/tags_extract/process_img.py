#!/usr/bin/env python
# -*- coding: utf-8 -*-

import subprocess
import os,sys

import ntpath
import cv2
import feature_extractor
import numpy
import tensorflow as tf
from tensorflow import app
from tensorflow import flags

FLAGS = flags.FLAGS

# In OpenCV3.X, this is available as cv2.CAP_PROP_POS_MSEC
# In OpenCV2.X, this is available as cv2.cv.CV_CAP_PROP_POS_MSEC
CAP_PROP_POS_MSEC = 0

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

def path_leaf(path):
    head, tail = ntpath.split(path)
    return tail or ntpath.basename(head)

## 读取图像，解决imread不能读取中文路径的问题
def cv_imread(file_path):
    return cv2.imdecode(numpy.fromfile(file_path, dtype=numpy.uint8), -1)

def process_img(filename):
    print(filename)
    # print(filename.encode("utf-8"))

    prefix = path_leaf(filename).split(".")[0]
    output = "%s_output.tfrecord" % prefix
    extractor = feature_extractor.YouTube8MFeatureExtractor(FLAGS.model_dir)
    writer = tf.python_io.TFRecordWriter(output)
    # print("$$$$$ %s" % (FLAGS.input_videos_csv))
    labels = "0"

    rgb_features = []
    # rgb = cv2.imread(filename, cv2.IMREAD_COLOR)
    rgb = cv_imread(filename)
    features = extractor.extract_rgb_frame_features(rgb[:, :, ::-1])
    rgb_features.append(_bytes_feature(quantize(features)))
    if not rgb_features:
        print >> sys.stderr, 'Could not get features for ' + video_file
        return
        # Create SequenceExample proto and write to output.
    feature_list = {
        FLAGS.image_feature_key: tf.train.FeatureList(feature=rgb_features),
    }
    # if FLAGS.insert_zero_audio_features:
    #     feature_list['audio'] = tf.train.FeatureList(
    #         feature=[_bytes_feature(_make_bytes([0] * 128))] * len(rgb_features))

    print(feature_list)
    example = tf.train.SequenceExample(
        context=tf.train.Features(feature={
            FLAGS.labels_feature_key:
                _int64_list_feature(sorted(map(int, labels.split(';')))),
            FLAGS.video_file_key_feature_key:
                _bytes_feature(_make_bytes(map(ord, "0"))),# filename should not have chinese, or else cause problem
        }),
        feature_lists=tf.train.FeatureLists(feature_list=feature_list))
    writer.write(example.SerializeToString())
    writer.close()
    print('Successfully encoded path = %s ' % filename)

# MAIN
if len(sys.argv) < 2:
    print("Error!")
video_file = sys.argv[1].strip()
if os.path.isdir(video_file):
    for file in os.listdir(video_file):
        file_path = os.path.join(video_file, file)
        if file_path.startswith("."):
            continue
        process_img(file_path)
else:
    process_img(video_file)