#!/usr/bin/env python
# -*- coding:utf-8 -*-
# Author:ZhengzhengLiu
#Python3中实现gb2312编码转utf-8编码转gbk编码

s = "你好"
print(type(s))
print(s)
unicode_to_gb2312 = s.encode("gb2312")
print(type(unicode_to_gb2312))
print(unicode_to_gb2312)
gb2312_to_utf8 = unicode_to_gb2312.decode("gb2312").encode("utf-8")
print(type(gb2312_to_utf8))
print(gb2312_to_utf8)
utf8_to_gbk = gb2312_to_utf8.decode("utf-8").encode("gbk")
print(type(utf8_to_gbk))
print(utf8_to_gbk)

#运行结果：
#<class 'str'>
#你好
#<class 'bytes'>
#b'\xc4\xe3\xba\xc3'
#<class 'bytes'>
#b'\xe4\xbd\xa0\xe5\xa5\xbd'
#<class 'bytes'>
#b'\xc4\xe3\xba\xc3'