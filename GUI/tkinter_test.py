#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'uart gui test'

__author__ = 'Mingxin Liu'

from tkinter import *
from tkinter import ttk
from tkinter import scrolledtext

class LabelSwitch(object):
    def __init__(self, master, title='title', options=[], row=0, column=0):
        # label
        self.label = Label(master, text=title)
        self.label.grid(row=row, column=column, sticky=E, rowspan=1, columnspan=1)
        # combobox
        self.combobox = ttk.Combobox(master, width=8, textvariable=StringVar())
        self.combobox['values'] = options
        self.combobox.grid(row=row, column=1+column, sticky=W, rowspan=1, columnspan=1)
        self.combobox.current(0)
        self.combobox.config(state='readonly')

class App(object):
    def __init__(self):

        self.root = Tk()
        self.root.title('com-test')

        # 上部基础区域
        tFrame = Frame(self.root)
        # 串口号、波特率、校验位、停止位，配置区域，串口打开按钮
        ttFrame = Frame(tFrame)
        self.com_switch = LabelSwitch(
            ttFrame, '串口号', ['com1', 'com2', 'com3', 'com4', 'com5', 'com6'])
        self.baud_rate_switch = LabelSwitch(
            ttFrame, '波特率', ['2400', '4800', '9600', '115200'], 0, 2)
        self.check_switch = LabelSwitch(
            ttFrame, '校验位', ['奇校验', '偶校验', '无校验', '1校验', '0校验'], 0, 4)
        self.stop_bit_switch = LabelSwitch(
            ttFrame, '停止位', ['1位', '1.5位', '2位'], 0, 6)
        self.com_button = Button(ttFrame, text='打开串口')
        self.com_button.grid(row=0, column=8, sticky=E, rowspan=1)
        ttFrame.grid(row=0, column=0, sticky=W, rowspan=1, columnspan=4)
        # 接受、发送，历史数据框
        tcFrame = Frame(tFrame)
        self.display_info = scrolledtext.ScrolledText(
            tcFrame, width=90, height=15, wrap=WORD)
        self.display_info.grid(column=0, row=3, sticky='WE')
        # self.display_info = Listbox(tcFrame, width=80)
        # self.display_info.grid(row=0, column=0, sticky=W,rowspan=1, columnspan=4)
        tcFrame.grid(row=1, column=0, sticky=W, rowspan=1, columnspan=4)
        # 发送框
        tbFrame = Frame(tFrame)
        self.send_info = scrolledtext.ScrolledText(
            tbFrame, width=90, height=5, wrap=WORD)
        self.send_info.grid(column=0, row=3, sticky='WE')
        tbFrame.grid(row=2, column=0, sticky=W, rowspan=1, columnspan=4)
        tFrame.grid(row=0, column=0, sticky=W, rowspan=3, columnspan=4)

        # 下部条件回复、快捷回复区域
        bFrame = Frame(self.root)
        bFrame.grid(row=3, column=0, sticky=W)
    
    def run(self):
        self.root.mainloop()

def main_loop():
    root = App()
    root.run()
    
if __name__ == '__main__':

    main_loop()
