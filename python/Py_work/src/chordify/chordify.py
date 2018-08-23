#!/usr/bin/env python
# -*- coding: utf-8 -*-

from encode import multipart_encode
from streaminghttp import register_openers
# import urllib2
import urllib.request as urllib2
import json
import re
import sys
import os

# HEADERS
KEY_HOST = "Host"
KEY_CONNECTION = "Connection"
KEY_ACCEPT = "Accept"
KEY_USER_AGENT = "User-Agent"
KEY_REFERER = "Referer"
KEY_ACCEPT_LANGUAGE = "Accept-Language"
KEY_COOKIE = "Cookie"
KEY_ORIGIN = "Origin"
KEY_X_REQUESTED_WITH = "X-Requested-With"

# REFERER = https://chordify.net/profile/5a321486786de05ff400156c/uploads
HOST = "chordify.net"
CONNECTION = "keep-alive"
ACCEPT = "application/json, text/javascript, */*; q=0.01"
USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"
REFERER = "https://chordify.net/profile/5a321486786de05ff400156c/uploads"
ACCEPT_LANGUAGE = "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7"
COOKIE = "_ga=GA1.2.1160919573.1532400605; __gads=ID=bafb717027cd5f39:T=1532400619:S=ALNI_Man22KSI5mY3g-QSFIcuEMy25PFRA; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; PHPSESSID=ka2jsm9njit6imus4q147eml91; _gid=GA1.2.1798073200.1532744823; _gat=1"
#COOKIE = "_ga=GA1.2.1705613249.1513219305; msgsRead=%7B%22explain_vid%22%3A1513231285%7D; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; __gads=ID=973328aa85f3925f:T=1513231543:S=ALNI_MZ7gW9Ac6aKifOre0KlR-ZNvdwa0Q; survey_8=true; PHPSESSID=2slvnfq7l0rv31ofpt83s3s1p1; _gid=GA1.2.1052716780.1523970156; _gat=1"
ORIGIN = "https://chordify.net"
X_REQUESTED_WITH = "XMLHttpRequest"

RETRY_TIMES = 2

# post @https://chordify.net/song/file:
# return slug
def upload_music(music_file):
	register_openers()
	datagen, headers = multipart_encode({"files[]": open(music_file, "rb")})
	headers[KEY_HOST] = HOST
	headers[KEY_CONNECTION] = CONNECTION
	headers[KEY_ACCEPT] = ACCEPT
	headers[KEY_USER_AGENT] = USER_AGENT
	headers[KEY_REFERER] = REFERER
	headers[KEY_ACCEPT_LANGUAGE] = ACCEPT_LANGUAGE
	headers[KEY_COOKIE] = COOKIE
	headers[KEY_ORIGIN] = ORIGIN
	headers[KEY_X_REQUESTED_WITH] = X_REQUESTED_WITH

	url = "https://chordify.net/song/file:"
	request = urllib2.Request(url, datagen, headers)
	print("POST: %s" % url)
	response = urllib2.urlopen(request).read()
	response_json = json.loads(response)
	print("response: %s" % response_json)

	if response_json.has_key("slug"):
		return (True, response_json["slug"])
	else:
		return (False, "")

# 1. https://chordify.net/chords/$(slug)
# 2. https://chordify.net/song/data/$(data-pseudoid)
# return: chordify_json
def get_chordify_json(slug):
	# 1. https://chordify.net/chords/$(slug)
	url = "https://chordify.net/chords/%s" % slug
	print("GET: %s" % url)
	request = urllib2.Request(url)
	request.add_header(KEY_COOKIE, COOKIE)
	request.add_header(KEY_USER_AGENT, USER_AGENT)
	request.add_header(KEY_HOST, HOST)
	html = urllib2.urlopen(request).read()

	# 2. https://chordify.net/song/data/$(data-pseudoid)
	result = re.search("data-pseudoid=\"(\S*)\"", html)
	data_pseudoid = result.group(1)
	url = "https://chordify.net/song/data/%s" % data_pseudoid
	print("GET: %s" % url)
	request = urllib2.Request(url)
	request.add_header(KEY_COOKIE, COOKIE)
	request.add_header(KEY_USER_AGENT, USER_AGENT)
	request.add_header(KEY_HOST, HOST)
	response = urllib2.urlopen(request)
	html = response.read()
	chordify_json = json.loads(html)
	return chordify_json

def save_chordify(chordify_json_file, chordify_str):
	print(chordify_str)
	f = open(chordify_json_file,'w+')
	f.write(chordify_str)
	f.close()

# MAIN
if len(sys.argv) < 2:
	print("default music!")
	music_file = "F:\\videos\\故事线\\音乐\\Dr. Dre,Snoop Dogg - The Next Episode.mp3"
else:
	music_file = sys.argv[1].strip()

dirname = os.path.dirname(music_file)
basename = os.path.basename(music_file).split(".")[0]
chordify_json_file = "%s/%s_chordify.json" % (dirname, basename)

retry_times = 0
(success, slug) = upload_music(music_file)
while (success == False) and (retry_times <= RETRY_TIMES):
	# retry
	print("retry: %d" % retry_times)
	(success, slug) = upload_music(music_file)
	retry_times += 1

if success == False:
	print("Fail and exit!")
	exit()

chordify_json = get_chordify_json(slug)
save_chordify(chordify_json_file, json.dumps(chordify_json,sort_keys=True, indent=4))
