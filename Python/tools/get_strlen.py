#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'get string length'

__author__ = 'Mingxin Liu'

import sys


def start_working(param=1):
    print('please enter your string:')
    while True:
        # 第一步，输入字符串
        source_str = sys.stdin.readline()
        if source_str == 'quit\n' or source_str == 'exit\n':
            return

        # 最后，输出结果
        print(len(source_str) - 1) # 删除回车的长度


if __name__ == '__main__':

    start_working()
