#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'get 376.2 cs sum'

__author__ = 'Mingxin Liu'

import sys

def start_working(param = 1):

    print('please enter your 376.2 string without starting code "68", strlen, and the ending code "16"')
    while True:
        # 第一步，输入字符串
        source_str = sys.stdin.readline()
        if source_str == 'quit\n' or source_str == 'exit\n':
            return
        # 第二步，字符串以空格为单位切割为字符数组
        str_list = source_str.split(' ')
        hex_list = []
        # 第三步，将字符转换为16进制的数字
        for num_str in str_list:
            if num_str == '\n' or num_str == '':# 筛除末尾空格和连续空格被分割后产生的空字串
                continue
            if num_str.endswith('\n'):# 去掉字串末尾的换行符
                num_str = num_str[:-1]

            if param == 1:# 可选1，将长度超过2的字串，两个一组切片
	            if len(num_str) > 2:
	            	n = 0
	            	while n < len(num_str):
	            		if n + 2 <= len(num_str):
	            			hex_list.append(int(num_str[n:n+2], 16))
	            		else:
	            			hex_list.append(int(num_str[n:], 16))
	            		n = n + 2
	            else:
	            	hex_list.append(int(num_str, 16))

            elif param == 2:# 可选2，将长度超过2的字串当成一个16进制数据处理
                hex_list.append(int(num_str, 16))

        # 第四步，求和
        sum = 0x00
        for num in hex_list:
            sum = sum + num
        # 第五步，求与
        sum = sum & 0xFF
        # 最后，输出结果
        print('%#x' % sum)

if __name__ == '__main__':

    start_working()

