#!/usr/bin/env python3
# -*- coding: utf-8 -*-
'''learning python by the book named byte of python
备份文件'''

__author__ = 'Mingxin Liu'

import os
import time

# 1. 需要备份的文件与目录将被指定在一个列表中
# 例如在Window下:
# source = ['"C:\\My Documents"', 'C:\\Code']
# 又例如在Mac OS 与 Linux下:
source = ['/Users/mingxin/Documents/program_data']
# 在这里要注意到我们必须在字符串中使用双引号
# 用以括起来其中包含空格的名称

# 2. 备份文件目录
target_dir = '/Users/mingxin/Documents/program_data'

# 3. 备份文件将打包成zip文件

# 4. zip压缩文件的文件名由当前日期与实践构成
# os.sep 根据操作系统给出相应的分隔符
target = target_dir + os.sep + time.strftime('%Y%m%d%H%M%S') + '.zip'

# 如果目标目录不存在，则进行创建
if not os.path.exists(target_dir):
    os.mkdir(target_dir) # 创建目录

# 5. 我们使用zip命令将文件打包成zip格式
zip_command = 'zip -r {0} {1}'.format(target, ' '.join(source))

# 运行备份
print('Zip command is:')
print(zip_command)
print('Running:')
if os.system(zip_command) == 0:
    print('Successful backup to', target)
else:
    print('Backup FAILED')