#!/usr/bin/env python
# -*- coding: utf-8 -*-

import subprocess
import os, sys

import ntpath
import cv2
import feature_extractor
import numpy
import tensorflow as tf
import time
from tensorflow import app
from tensorflow import flags
from urllib.parse import quote

FLAGS = flags.FLAGS

# In OpenCV3.X, this is available as cv2.CAP_PROP_POS_MSEC
# In OpenCV2.X, this is available as cv2.cv.CV_CAP_PROP_POS_MSEC
CAP_PROP_POS_MSEC = 0
IMG_FORMATS = ['.jpg', '.jpeg', '.png', '.JPG', '.JPEG', '.PNG']

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


## 读取图像，解决imread不能读取中文路径的问题. 效率要低点
def cv_imread(file_path):
    return cv2.imdecode(numpy.fromfile(file_path, dtype=numpy.uint8), -1)


def writeTfrecord(filename, extractor, writer):
    labels = "0"
    rgb_features = []
    # rgb = cv2.imread(filename, cv2.IMREAD_COLOR)
    rgb = cv_imread(filename)
    features = extractor.extract_rgb_frame_features(rgb[:, :, ::-1])
    rgb_features.append(_bytes_feature(quantize(features)))
    if not rgb_features:
        print('Could not get features for ' + filename, file=sys.stderr)
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
                _bytes_feature(_make_bytes(map(ord, quote(filename)))),
            # filename should not have chinese, or else causeproblem
        }),
        feature_lists=tf.train.FeatureLists(feature_list=feature_list))
    writer.write(example.SerializeToString())


def process_img(filename, output):
    print(filename)

    # prefix = path_leaf(filename).split(".")[0]  # prefix is simple filename
    # output = "%s/%s_output.tfrecord" % (dir, prefix)
    extractor = feature_extractor.YouTube8MFeatureExtractor(FLAGS.model_dir)
    writer = tf.python_io.TFRecordWriter(output)

    writeTfrecord(filename, extractor, writer)
    writer.close()
    print('Successfully encoded path = %s ' % filename)


def printError(msg):
    print(msg, file=sys.stderr)


def getTimeInMills():
    return int(round(time.time() * 1000))


# MAIN
if len(sys.argv) < 3:
    printError(" Error! argument is not enough.")
else:
    img_file = sys.argv[1].strip()
    dir = sys.argv[2].strip()  # for single file, this is absolute filename

    if os.path.isdir(img_file):
        extractor = feature_extractor.YouTube8MFeatureExtractor(FLAGS.model_dir)
        # tfs_time_outputs.tfrecord
        tfs_path = 'tfs_%s_outputs.tfrecord' % str(getTimeInMills())
        tfs_full_path = '%s\%s' % (dir, tfs_path)
        # tfs_config.txt
        tfsConfigWriter = open('%s%stfs_config.txt' % (dir, '\\'), "at")  # append

        tfWriter = tf.python_io.TFRecordWriter(tfs_full_path)
        first = True
        for file in os.listdir(img_file):
            file_path = os.path.join(img_file, file)
            if file_path.startswith("."):
                continue
            if not os.path.isfile(file_path):
                continue
            extension = os.path.splitext(file_path)[1]  # get the extension of file
            if extension not in IMG_FORMATS:
                continue

            # write tfrecord
            writeTfrecord(file_path, extractor, tfWriter)
            # write to congig
            if first:
                tfsConfigWriter.write(tfs_full_path)
                tfsConfigWriter.write(",")
                tfsConfigWriter.write(file_path)
                first = False
            else:
                tfsConfigWriter.write(" " + file_path)

        tfsConfigWriter.write("\n")
        tfsConfigWriter.close()
        tfWriter.close()
    else:
        process_img(img_file, dir)
