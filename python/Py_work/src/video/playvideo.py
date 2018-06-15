#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import cv2
import ntpath

def path_leaf(path):
    head, tail = ntpath.split(path)
    return tail or ntpath.basename(head)

print(path_leaf("c:/a/b/c.tf").split("."))
cap = cv2.VideoCapture('F:/videos/故事线/婚礼1/成片/Demo8.mp4')

while(cap.isOpened()):
    ret, frame = cap.read()
    # 灰度化
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    cv2.imshow('frame',gray)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
