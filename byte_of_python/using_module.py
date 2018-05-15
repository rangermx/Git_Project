#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''learning python by the book named byte of python
how to use module'''

__author__ = 'Mingxin Liu'

import sys

print('The command line arguments are:')
for i in sys.argv:
    print(i)

print('\n\nThe PYTHONPATH is', sys.path, '\n')

# from ... import 语句
# 一般来说，不推荐，因为容易与本地同名函数起冲突
from math import sqrt
print('Square root of 16 is', sqrt(16))


# 模块的__name__
# 通过__name__确定模块是被 导入来运行 还是 独立运行的
if __name__ == '__main__':
    print('This program is being run by itself')
else:
    print('I am being imoorted from another module')
