#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'get hex string by num string'

__author__ = 'Mingxin Liu'

import sys


def start_working(param = 1):
    print('please enter your num string:')
    while True:
        # 第一步，输入字符串
        source_str = sys.stdin.readline()
        if source_str == 'quit\n' or source_str == 'exit\n':
            return
        # 第二步，字符串以空格为单位切割为字符数组
        str_list = source_str.split(' ')
        result_list = []
        # 第三步，每个字符前加上0x
        for num_str in str_list:
            if num_str == '\n' or num_str == '':  # 筛除末尾空格和连续空格被分割后产生的空字串
                continue
            if num_str.endswith('\n'):  # 去掉字串末尾的换行符
                num_str = num_str[:-1]

            if param == 1:  # 可选1，将长度超过2的字串，两个一组切片
                if len(num_str) > 2:
                    n = 0
                    while n < len(num_str):
                        if n + 2 <= len(num_str):
                            result_list.append('0x' + num_str[n:n+2])
                        else:
                            result_list.append('0x' + num_str[n:])
                        n = n + 2
                else:
                    result_list.append('0x' + num_str)

            elif param == 2:  # 可选2，将长度超过2的字串当成一个16进制数据处理
                result_list.append('0x' + num_str)

        # 最后，输出结果
        for num_str in result_list:
            if (num_str != result_list[-1]):
                print(num_str, end=", ")
            else:
                print(num_str)


if __name__ == '__main__':

    start_working()
