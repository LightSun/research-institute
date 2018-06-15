#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import cv2
import os

"""
cv2.IMREAD_COLOR : Loads a color image. Any transparency of image will be neglected. It is the default flag.
cv2.IMREAD_GRAYSCALE : Loads image in grayscale mode
cv2.IMREAD_UNCHANGED : Loads image as such including alpha channel
"""

#in python path should be \\(windows)
image_path='E:\\work\\Py_work\\src\\resource\\d.jpg'
print(image_path)
print(os.path.exists(image_path))

img=cv2.imread(image_path, cv2.IMREAD_COLOR)
# print(img.shape)
cv2.imshow('image', img)
k=cv2.waitKey(0)
if k == 27:         # wait for ESC key to exit
    cv2.destroyAllWindows()
elif k == ord('s'): # wait for 's' key to save and exit
    cv2.imwrite('messigray.png',img)
    cv2.destroyAllWindows()