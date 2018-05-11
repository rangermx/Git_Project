#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'async/await'

__author__ = 'Mingxin Liu'

# Python从3.5版本开始为asyncio提供了async和await的新语法；

# 注意新语法只能用在Python 3.5以及后续版本，如果使用3.4版本，则仍需使用上一节的方案。

import threading
import asyncio

async def hello():
    print('Hello world! (%s)' % threading.currentThread())
    await asyncio.sleep(1)
    print('Hello again! (%s)' % threading.currentThread())

loop = asyncio.get_event_loop()
tasks = [hello(), hello()]
loop.run_until_complete(asyncio.wait(tasks))
loop.close()