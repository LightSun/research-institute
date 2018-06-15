#!/usr/bin/env python
# -*- coding: utf-8 -*-

import subprocess
import os,sys
import re
import math
from decimal import *

#ffmpeg -ss 00:00:10 -t 00:00:2 -i model1.mp4 -vcodec copy -acodec copy 6.MP4

def get_video_duration(video_file):
	process = subprocess.Popen(['F:/ffmpeg-win64-static/bin/ffprobe', '-i', video_file], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
	stdout, stderr = process.communicate()
	print(stdout)
	stdout = stdout.decode('utf-8')
	matches = re.search(r"Duration:\s{1}(\d+?):(\d+?):(\d+\.\d+?),", stdout)
	hours = Decimal(matches.group(1))
	minutes = Decimal(matches.group(2))
	seconds = Decimal(matches.group(3))
	total = 0
	total += 60 * 60 * hours
	total += 60 * minutes
	total += seconds
	return float(total)

def write_stocks_file(video_file, frames):
	# os.system("rm -rf ./tmp/*")
	os.system("del /s/q tmp")
	fo = open("stocks.csv", "w")
	for i in range(frames):
		start = "00:" + str(int(i/60)) + ":" + str(i%60)	
		filename = os.path.abspath(".") + "/tmp/" + str(i) + ".MP4"
		csv = filename + ",0\n"
		fo.write(csv)
		subprocess.call(["ffmpeg", "-ss", start, "-t", "00:00:1", "-i", video_file, "-vcodec", "copy", "-acodec", "copy", filename])
	fo.close()

def process_video(video_file):
	prefix = os.path.basename(video_file).upper().split(".MP4")[0]
	# abs_prefix = os.path.abspath(video_file).lower().split(".MP4")[0]
	output_tfrecords_file = "%s_output.tfrecord" % prefix
	print("$$$process video: %s, output file: %s" % (video_file, output_tfrecords_file))
	duration = get_video_duration(video_file)
	frames = int(math.ceil(duration))
	write_stocks_file(video_file, frames)
	cmd = "python extract_tfrecords_main.py  --input_videos_csv stocks.csv --output_tfrecords_file '%s'" % output_tfrecords_file
	os.system(cmd)

# MAIN
if len(sys.argv) < 2:
	print("Error!")
video_file = sys.argv[1].strip()
if os.path.isdir(video_file):
	for file in os.listdir(video_file):
		file_path = os.path.join(video_file, file)
		if file_path.startswith("."):
			continue
		process_video(file_path)
else:
	process_video(video_file)
