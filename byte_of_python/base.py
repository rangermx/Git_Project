#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'learning python by the book named byte of python'

__author__ = 'Mingxin Liu'

# 数字
# 两种类型 Integers 与 Floats

# 字符串
# String 是 字符 Characters 的序列
# 字符串是不可变的

# 单引号 & 双引号
# 用来指定字符串，单双引号完全相同

# 三引号
# 如：""" 或 ''',用来指定多行字符串，可以在三引号之间自由的使用单引号和双引号


# 字符串格式化方法 format()
age = 20
name = 'swaroop'
print('{0} was {1} years old when he worte this book'.format(name, age))
print('why is {0} playing with the python?'.format(name))
# 也可以简写为
print('{} was {} years old when he worte this book'.format(name, age))
print('why is {} play with python?'.format(name))
# 可以通过联立字符串来达到相同的效果:(丑陋且易出错)
name + 'is' + str(age) + 'years old'
# 可以有更详细的格式，如：
# 对浮点数'0.333'保留小数点(.)后三位
print('{0:.3f}'.format(1.0/3))
# 基于关键词输出 'Swaroop wrote A Byte of Python'
print('{name} wrote {book}'.format(name='Swaroop', book='A Byte of Python'))


# print 打印
# 默认以\n结尾，可以通过end指定结尾
# 指定空格/空白为结尾
print('a', end='')
print('b', end=' ')
print('c')


# 运算符
# 非常见运算符
# **    乘方
# //    整除
# %     取模(取余)
# <<    左移(二进制左移)
# >>    右移
# &     按位与
# |     按位或
# ^     按位异或
# ~     按位取反

# !=    比较两个对象是否不相等
# not   (布尔非)
# and   (布尔与)
# or    (布尔或)


# 函数
x = 50
def func(x):
    print('x is', x)
    x = 2
    print('changed local x to', x)

func(x)
print('x is still', x)
# 局部变量 与 全局变量
# 可以通过 global 语句对全局变量赋值，但是该做法不推荐
def func():
    global x
    
    print('x is', x)
    x = 2
    print('changed global x to', x)

func()
print('Value of x is', x)
# 函数默认参数
def say(message, times=1):
    print(message * times)
say('Hello')
say('World', 5)
# 注意字符串用 * 运算符的结果
# 使用参数关键字赋值，不用考虑参数的顺序
def func(a, b=5, c=10):
    print('a is', a, 'and b is', b, 'and c is', c)

func(3, 7)
func(25, c=24)
func(c=50, a=100)
# 可变参数
def total(a=5, *numbers, **phonebook):
    print('a', a)

    # 遍历元组中的数据
    for single_item in numbers:
        print('single_item', single_item)

    # 遍历字典中的所有项目
    for first_part, second_part in phonebook.items():
        print(first_part, second_part)

print(total(10, 1, 2, 3, jack=1123, john=2231, Inge=1560))
# 文档字符串
# 函数的第一个逻辑行中的字符串是该函数的文档字符串，文档字符串也适用于模块(Modules) 与类(Class)
def print_max(x, y):
    '''打印两个数值中的最大数。
    
    这两个数都应该是整数'''
    # 如果可能，将其转换至整数类型
    x = int(x)
    y = int(y)

    if x > y:
        print(x, 'is maximum')
    else:
        print(y, 'is maximum')
print_max(3, 5)
print(print_max.__doc__)

