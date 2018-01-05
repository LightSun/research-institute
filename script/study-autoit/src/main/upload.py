# coding:UTF8
from selenium import webdriver
import time
import os

"""
 1, 安装 python + selenium (你可能需要python idea/eclipse插件)
     下载 chromedriver.exe.

  2, 编写一个简单的html.(实际中可能来自其他web网站)
  3, 编写python自动化脚本并运行
  4, 使用autoit window info ----- 拖动finder tool到edittext 或者 button 观察得到对象属性

  5, SCITE Script Editor 写au3脚本,测试
  
  6, Compile Script to.exe 将au3脚本转化为exe, 然后用python调用

  7, 完整测试最终的python脚本


idea from:  https://jingyan.baidu.com/article/925f8cb8df6f11c0dde056c1.html
"""

driver = webdriver.Chrome("D:/Program Files/py_depend/chromedriver.exe")
file_path = "file:///" + os.path.abspath(r"E:\test\test-html\src\main\upload.html")
driver.get(file_path)
driver.find_element_by_name("file").click()

os.system(r"E:\test\test-html\src\main\upfile.exe")
driver.close()


















