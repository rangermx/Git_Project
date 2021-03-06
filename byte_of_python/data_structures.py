#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''learning python by the book named byte of python
data structures include List Tuple Dictionary Set'''

__author__ = 'Mingxin Liu'

# List 列表
shoplist = ['apple', 'mango', 'carrot', 'banana']
# len() 返回列表中元素的个数
print(len(shoplist))
# append() 向列表中添加元素
shoplist.append('rice')
print(shoplist)
# sort() 排序
shoplist.sort()
print(shoplist)
# del 删除元素
del shoplist[0]
print(shoplist)

# Tuple 元组
# len() 返回元素个数
zoo = ('python', 'elephant', 'penguin')
print(len(zoo))
# 另外一种创建方式,可以不加括号，zoo是new_zon的一个元素
new_zoo = 'monkey', 'camel', zoo
print(new_zoo, len(new_zoo))
# 直接调用new_zoo中zoo的元素
print(new_zoo[2][2])

# Dict 字典
ab = {
    'Swaroop': 'swaroop@swaroopch.com',
    'Larry': 'larry@wall.org',
    'Matsumoto': 'matz@ruby-lang.org',
    'Spammer': 'spammer@hotmail.com'
}
# 根据 键 输出 值
print(ab['Swaroop'])
# 根据 键 删除 键值对
del ab['Spammer']
print(len(ab))
# 遍历输出
for name, address in ab.items():
    print('Contact {} at {}'.format(name, address))
# 添加一对键值对
ab['Guido'] = 'gudio@python.org'
if 'Guido' in ab:
    print(ab['Guido'])

# Set 集合
bri = set(['brazil', 'russia', 'india'])
# 判断集合是否包含元素
print('india' in bri)
print('usa' in bri)
# 集合拷贝
bric = bri.copy()
bric.add('china')
# 集合1是否是集合2的父集
print(bric.issuperset(bri))
# 删除元素
bric.remove('russia')
# 交集
print(bri & bric)
# 等价于
print(bri.intersection(bric))

# 字符串处理
name = 'Swaroop'

if name.startswith('Swa'):
    print('Yes, the string starts with "Swa"')

if 'a' in name:
    print('Yes, it contains the string "a"')

if name.find('war') != -1:
    print('Yes, it contains the string "war"')

delimiter = '_*_'
mylist = ['Brazil', 'Russia', 'India', 'China']
print(delimiter.join(mylist))