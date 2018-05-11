#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'io test'

__author__ = 'Mingxin Liu'

# try:
#     f = open('/Users/mingxin/Desktop/Python/files/test', 'r')
#     print(f.read())
# finally:
#     if f:
#         f.close()

# 下面的写法和前面的try ... finally是一样的，但是代码更佳简洁，并且不必调用f.close()方法。
# with open('/Users/mingxin/Desktop/Python/files/test', 'r') as f:
#     print(f.read())

# 调用read()会一次性读取文件的全部内容，如果文件有10G，内存就爆了。
# 所以，要保险起见，可以反复调用read(size)方法，每次最多读取size个字节的内容。
# 另外，调用readline()可以每次读取一行内容。
# 调用readlines()一次读取所有内容并按行返回list。
# 因此，要根据需要决定怎么调用。
# 如果文件很小，read()一次性读取最方便；
# 如果不能确定文件大小，反复调用read(size)比较保险；
# 如果是配置文件，调用readlines()最方便；

# with open('/Users/mingxin/Desktop/Python/files/test', 'rb') as f: # 读取字节流
#     print(f.read())

#序列化
import pickle
import json

#先写入,序列化
# d = dict(name='Bob', age=20, score=88)
# # print(pickle.dumps(d))
# f = open('/Users/mingxin/Desktop/Python/files/test', 'wb')
# pickle.dump(d, f)
# f.close()

#再读取,反序列化
# f = open('/Users/mingxin/Desktop/Python/files/test', 'rb')
# d = pickle.load(f)
# f.close()
# print(d)

# json
# d = dict(name='Bob', age=20, score=88)
# # print(json.dumps(d))
# json_str = '{"age": 21, "score": 89, "name": "Bob"}'
# d = json.loads(json_str)
# print(d)

class Student(object):
    def __init__(self, name, age, score):
        self.name = name
        self.age = age
        self.score = score
    def student2dict(self):
        return {
            'name': self.name,
            'age': self.age,
            'score': self.score
        }

s = Student('Bob', 20, 88)
print(json.dumps(s, default=Student.student2dict))
