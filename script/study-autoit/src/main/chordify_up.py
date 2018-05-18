#
# import xml.etree.ElementTree as ET
from bs4 import BeautifulSoup
from selenium import webdriver
import requests
import logging
import os

import urllib
import urllib2
import cookielib
import re
import datetime

import poster
from poster.encode import multipart_encode, MultipartParam
from poster.streaminghttp import register_openers


def postFileRequest(url, paramName, fileObj, additionalHeaders={}, additionalParams={}):
    items = []
    # wrap post parameters
    for name, value in additionalParams.items():
        items.append(MultipartParam(name, value))
    # add file
    items.append(MultipartParam.from_file(paramName, fileObj))
    datagen, headers = multipart_encode(items)
    # add headers
    for item, value in additionalHeaders.iteritems():
        headers[item] = value
    return urllib2.Request(url, datagen, headers)


path = 'https://chordify.net/song/file:'
headers = multipart_encode({"files[]": open("C:\\Users\\Administrator\\Downloads\\test.mp3", "r")})

headers = {}
headers['Origin'] = 'https://chordify.net'
headers['Host'] = 'chordify.net'
headers['User-Agent'] = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36'
headers['Referer'] = 'https://chordify.net/chords/gyom-dream-catcher-%s' % datetime.datetime.now()
headers['Accept'] = 'application/json, text/javascript, */*, q=0.01'
headers['Connection'] = 'keep-alive'
headers['Cookie'] = '_ga=GA1.2.1705613249.1513219305; msgsRead=%7B%22explain_vid%22%3A1513231285%7D; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; __gads=ID=973328aa85f3925f:T=1513231543:S=ALNI_MZ7gW9Ac6aKifOre0KlR-ZNvdwa0Q; PHPSESSID=c9lre7dphfiigttatffb1575v1; dzSupport=0; _gid=GA1.2.232232449.1516258359; survey_8=true; _gat=1'
# headers = {('Origin', 'https://chordify.net'),
#            ('Host', 'chordify.net'),
#            ('User-Agent', 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36'),
#            ('Referer', 'https://chordify.net/chords/gyom-dream-catcher-%s' % datetime.datetime.now()),
#            ('Accept', 'application/json, text/javascript, */*, q=0.01'),
#            ('X-Requested-With', 'XMLHttpRequest'),
#            ('Connection', 'keep-alive'),
#            ('Content-Type', 'multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__'),
#            ('Cookie',
#             '_ga=GA1.2.1705613249.1513219305; msgsRead=%7B%22explain_vid%22%3A1513231285%7D; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; __gads=ID=973328aa85f3925f:T=1513231543:S=ALNI_MZ7gW9Ac6aKifOre0KlR-ZNvdwa0Q; PHPSESSID=c9lre7dphfiigttatffb1575v1; dzSupport=0; _gid=GA1.2.232232449.1516258359; survey_8=true; _gat=1'),
#            }
req = postFileRequest(path, 'files[]',  open("C:\\Users\\Administrator\\Downloads\\test.mp3", "rb"), headers);
print urllib2.urlopen(req).read()
#
# opener = poster.streaminghttp.register_openers()
# opener.add_handler(urllib2.HTTPCookieProcessor(cookielib.CookieJar()))
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookielib.CookieJar()))
urllib2.install_opener(opener)

# datagen, headers = multipart_encode({"files[]": open("C:\\Users\\Administrator\\Downloads\\test.mp3", "r")})
# headers['Origin'] = 'https://chordify.net'
# headers['Host'] = 'chordify.net'
# headers[
#     'User-Agent'] = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36'
# headers['Referer'] = 'https://chordify.net/chords/gyom-dream-catcher-%s' % datetime.datetime.now()
# headers['Accept'] = 'application/json, text/javascript, */*, q=0.01'
# headers['Connection'] = 'keep-alive'
# headers[
#     'Cookie'] = '_ga=GA1.2.1705613249.1513219305; msgsRead=%7B%22explain_vid%22%3A1513231285%7D; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; __gads=ID=973328aa85f3925f:T=1513231543:S=ALNI_MZ7gW9Ac6aKifOre0KlR-ZNvdwa0Q; PHPSESSID=c9lre7dphfiigttatffb1575v1; dzSupport=0; _gid=GA1.2.232232449.1516258359; survey_8=true; _gat=1'

#     ('Origin','https://chordify.net'),
#     ('Host','chordify.net'),
#                     ('User-Agent','Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36'),
#                     ('Referer', 'https://chordify.net/chords/gyom-dream-catcher-%s' % datetime.datetime.now()),
#                     ('Accept', 'application/json, text/javascript, */*, q=0.01'),
#                     ('X-Requested-With', 'XMLHttpRequest'),
#                     ('Connection', 'keep-alive'),
#                     ('Content-Type', 'multipart/form-data; charset=utf-8; boundary=__X_PAW_BOUNDARY__'),
#                     ('Cookie', '_ga=GA1.2.1705613249.1513219305; msgsRead=%7B%22explain_vid%22%3A1513231285%7D; user=Sq7ntH1nkgEh1TbKfv45%2BIofciZM3GOZ6gAJpCzNkEY%3D; __gads=ID=973328aa85f3925f:T=1513231543:S=ALNI_MZ7gW9Ac6aKifOre0KlR-ZNvdwa0Q; PHPSESSID=c9lre7dphfiigttatffb1575v1; dzSupport=0; _gid=GA1.2.232232449.1516258359; survey_8=true; _gat=1'),
#                     ]
# req = urllib2.Request(path, datagen, headers)
#
# result = urllib2.urlopen(req)
# print result.read()

# driver = webdriver.Chrome("D:/Program Files/py_depend/chromedriver.exe")
#
# urllib2.install_opener(opener)
# path = 'https://chordify.net/song/file:'
# req = urllib2.Request(path, post_data)
# conn = urllib2.urlopen(req)

# print urllib2.urlopen(req).read()

# files = {'fileupload': "C:\Users\Administrator\Downloads\212_short2_dream-catcher_0037_preview.mp3"}
#
# r = requests.post("https://chordify.net/chords/song/file", files)  # url wrong ?
# # https://chordify.net/song/data/file:58ab7278cb8699e3a60fca40cc88661d.mp3?vocabulary=extended_inversions
# print(r.content)
#
# soup = BeautifulSoup(r.content, "html.parser")
# ele = soup.find_all(id='song')
# target = str(ele)
# matchObj = re.search(r"data-stream=(\".*\")", target)
# if matchObj:
#     print "matchObj.group() : ", matchObj.group()
# else:
#     print "No match!!"
#
# data_stream = matchObj.group().split(' ')[0]
# data_stream.index("=")
# tlen = len('data-stream="')
# # print data_stream[tlen:-1]  # https://api.soundcloud.com/tracks/207430667/stream?client_id=14c02ab42701368742b26d4a6eef0151
#
# cid_index = data_stream[tlen:-1].index("client_id=")
# cid_index += len('client_id=')
# client_id = data_stream[tlen:-1][cid_index:]
# # time.sleep(20)
#
# url = 'https://chordify.net/song/data/file:%s.mp3?vocabulary=extended_inversions' % client_id
# r = requests.get(url)
# print r.status_code, url

# driver.close()
