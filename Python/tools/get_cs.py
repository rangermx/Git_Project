#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'get 376.2 cs sum'

__author__ = 'Mingxin Liu'

import sys

def start_working():
    while True:
        print('please enter your 376.2 string without starting code "68", strlen, and the ending code "16"')
        # 第一步，输入字符串
        source_str = sys.stdin.readline()
        if source_str == 'quit\n' or source_str == 'exit\n':
            return
        # 第二步，字符串以空格为单位切割为字符数组
        str_list = source_str.split(' ')
        hex_list = []
        # 第三步，将字符转换为16进制的数字
        for num_str in str_list:
            hex_list.append(int(num_str, 16))
        # 第四步，求和
        sum = 0x00
        for num in hex_list:
            sum = sum + num
        # 第五步，求与
        sum = sum & 0xFF
        # 最后，输出结果
        print('%#x' % sum)

start_working()

