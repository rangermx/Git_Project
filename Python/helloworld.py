#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# 第一行注释是为了告诉Linux/OS X系统，这是一个Python可执行程序，Windows系统会忽略这个注释；
# 第二行注释是为了告诉Python解释器，按照UTF-8编码读取源代码，否则，你在源代码中写的中文输出可能会有乱码。
# 申明了UTF-8编码并不意味着你的.py文件就是UTF-8编码的，必须并且要确保文本编辑器正在使用UTF-8 without BOM编码

# print('hello world!')
# print('The quick browm fox', 'jumps over', 'the lazy dog')
# print(10 + 20)
# print('100 + 200 =', 100 + 200)

# print('please enter your name: (use enter to stop typing and submit)')
# name = input()
# print('name =', name)

# print('''line1
# line2
# line3''')

# print(ord('A'))
# print(ord('中'))
# print('abc'.encode('ascii'))
# print('中文'.encode('utf-8'))
# print(b'abc'.decode('ascii'))
# print(b'\xe4\xb8\xad\xff'.decode('utf-8', errors='ignore'))
# print(len('中文'))
# print(len('中文'.encode('utf-8')))

# print('Hello, %s' % 'world')
# print('Hi, %s, you have $%d' % ('Michael', 10000))
# print('growth rate: %d %%' % 7)

# list
# classmates = ['Michael', 'Bob', 'Tracy']
# print(classmates)
# print(len(classmates))
# print(classmates[0])
# print(classmates[-1])
# classmates.append('Adam')
# classmates.insert(1, 'jack')
# print(classmates)
# name = classmates.pop()
# print(name)
# print(classmates)
# name = classmates.pop(1)
# print(name)
# print(classmates)
# L = ['Apple', 123, True]
# s = ['Python', 'java', ['asp', 'php'], 'scheme']
# print(len(s))

#tuple
# classmates = ('Michael', 'Bob', 'Tracy')

# print('please input a number')
# num = input()
# num = int(num)
# if num > 0:
#     print('Its absolute value is', num)
# else:
#     print('Its absolute value is', -num)

# def my_abs(num):
#     if not isinstance(num, (int, float)):
#         raise TypeError('bad operand type')
#     if num > 0:
#         return num
#     else:
#         return -num
# print(my_abs('a'))

# map函数使用
# def f(x):
#     return x * x

# r = map(f, [1,2,3,4,5,6,7,8,9])
# print(list(r))

# reduce函数使用
from functools import reduce #python3版本需要先引入
# # 求和
# def add(x, y):
#     return x + y
# result = reduce(add, [1, 3, 5, 7, 9])
# print(result)
# # 效果等同于sum()
# result = sum([1, 3, 5, 7, 9])
# print(result)

# 序列转整数
# def fn(x, y):
#     return x * 10 + y
# print(reduce(fn, [1, 3, 5, 7, 9]))

# str 转 int
# DIGITS = {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}
# def str2int(s):
#     def fn(x, y):
#         return x * 10 + y
#     def char2int(c):
#         return DIGITS[c]
#     return reduce(fn, map(char2int, s))
# result = str2int('9382022')
# print(result)

# 练习,大小写转换
# def normalize(name):
#     # return name.capitalize() #首字母大写，其他字母小写
#     # return name.upper() #小写字母转换为大写字母
#     # return name.lower() #大写字母转换为小写字符
#     # return name.title() #字符串中每个单词的首字母大写
# L1 = ['adam', 'LISA', 'barT']
# L2 = list(map(normalize, L1))
# print(L2)

# 练习，求积
# def prod(L):
#     def fn(x, y):
#         return x * y
#     return reduce(fn, L)
# print('3 * 5 * 7 * 9 =', prod([3, 5, 7, 9]))
# if prod([3, 5, 7, 9]) == 945:
#     print('测试成功!')
# else:
#     print('测试失败!')

# 练习，str 转 float
# def str2float(s):
#     index = s.find('.')
#     tempS = s[:index] + s[index + 1:]
#     result = str2int(tempS)
#     def fn(x):
#         y = 1
#         while(x > 0):
#             y = y * 10
#             x -= 1
#         return y
#     return result / fn(index)
# print('str2float(\'123.456\') =', str2float('123.456'))
# if abs(str2float('123.456') - 123.456) < 0.00001:
#     print('测试成功!')
# else:
#     print('测试失败!')

# filter 过滤器
# def _odd_iter():    #生成大于1的奇数
#     n = 1
#     while True:
#         n = n + 2
#         yield n
# def _not_divisble(n):   #不可整除
#     return lambda x: x % n > 0
# def primes():   #生成素数
#     yield 2
#     it = _odd_iter()    #初始序列
#     while True:
#         n = next(it)    #返回序列的第一个数字
#         yield n
#         it = filter(_not_divisble(n), it)
# for n in primes():  #打印出小于1000的素数
#     if n < 1000:
#         print(n)
#     else:
#         break

# import module
# module.test()

# class Student():
#     def __init__(self, name, score):
#         self.name = name
#         self.score = score
#     def print_score(self):
#         print('%s: %s' % (self.name, self.score))

# bart = Student('bart', 56)
# bart.print_score()

# class Student():
#     name = 'Student'
# s = Student()
# print(s.name)
# s.name = 'sName'
# print(s.name)
# print(Student.name)
# del(s.name)
# print(s.name)

class Student():
    def __init__(self, score):
        self.score = score
    @property
    def score(self):
        return self._score
    @score.setter
    def score(self, value):
        if not isinstance(value, int):
            raise ValueError('score must be a Integer!')
        if value < 0 or value > 100:
            raise ValueError('score must between 0 ~ 100')
        self._score = value
    def __str__(self):
        return 'Student object (score: %s)' % self.score
    __repr__ = __str__
s = Student(100)
print(s)
# s = Student()
# s.score = 10
# print(s.score)
# s.score = 101
