import ntpath
import sys
import tensorflow as tf
import csv
import os
import urllib
from urllib.parse import quote
import cv2

def path_leaf(path):
    head, tail = ntpath.split(path)
    return tail or ntpath.basename(head)

stock_file= "E:\\work\\ai_script\\stocks.csv"
print("dirname: ", os.path.dirname(stock_file)) # 目录
print("basename: ",os.path.basename(stock_file)) # 带后缀的文件名.
print("splitext[0]: ",os.path.splitext(stock_file)[0])
print("path_leaf: ", path_leaf(stock_file))

file_arr = os.path.splitext(stock_file)
virtualPath = file_arr[0] + '/0' + file_arr[1]
print(virtualPath)

path="F:\\videos\\故事线\\婚礼2\\晚宴\\C0192.mp4"
# url_code_result = urllib.parse.urlencode(path) # can't used for string
# url_code_result = quote(path, safe=';/?:@=|,')
url_code_result = quote(path, safe=':\\/')
print(type(url_code_result))
print(url_code_result)
# for video_file, labels in csv.reader(open(stock_file)):
#     print("video_file", video_file) # E:\work\ai_script/tmp/93.MP4
#     print("labels", labels)


# Find OpenCV version
(major_ver, minor_ver, subminor_ver) = (cv2.__version__).split('.')
video_capture = cv2.VideoCapture()
state= video_capture.open(path)
if not state:
    print('Error: Cannot open video file ' + path, file=sys.stderr)
else:
    # 获取帧率
    if int(major_ver)  < 3 :
        fps = video_capture.get(cv2.cv.CV_CAP_PROP_FPS)
        print("Frames per second using video.get(cv2.cv.CV_CAP_PROP_FPS): {0}".format(fps))
    else :
        fps = video_capture.get(cv2.CAP_PROP_FPS)
        print("Frames per second using video.get(cv2.CAP_PROP_FPS) : {0}".format(fps))
    video_capture.release()


# sess = tf.Session(config=tf.ConfigProto(log_device_placement=True))
from tensorflow.python.client import device_lib
print(device_lib.list_local_devices()[0].device_type)