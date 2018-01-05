#
# import xml.etree.ElementTree as ET
from bs4 import BeautifulSoup
from selenium import webdriver
import requests
import logging
import os
import re
import time

# driver = webdriver.Chrome("D:/Program Files/py_depend/chromedriver.exe")
# mock file upload
files = {'files': "C:\Users\Administrator\Downloads\212_short2_dream-catcher_0037_preview.mp3"}

r = requests.post("https://chordify.net/chords/song/file", files)  # url wrong ?
# https://chordify.net/song/data/file:58ab7278cb8699e3a60fca40cc88661d.mp3?vocabulary=extended_inversions
# print(r.content)

soup = BeautifulSoup(r.content, "html.parser")
ele = soup.find_all(id='song')
target = str(ele)
matchObj = re.search(r"data-stream=(\".*\")", target)
if matchObj:
    print "matchObj.group() : ", matchObj.group()
else:
    print "No match!!"

data_stream = matchObj.group().split(' ')[0]
data_stream.index("=")
tlen = len('data-stream="')
# print data_stream[tlen:-1]  # https://api.soundcloud.com/tracks/207430667/stream?client_id=14c02ab42701368742b26d4a6eef0151

cid_index = data_stream[tlen:-1].index("client_id=")
cid_index += len('client_id=')
client_id = data_stream[tlen:-1][cid_index:]
# time.sleep(20)

url = 'https://chordify.net/song/data/file:%s.mp3?vocabulary=extended_inversions' % client_id
r = requests.get(url)
print r.status_code, url

# driver.close()
