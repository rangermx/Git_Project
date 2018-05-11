#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'hello_web'

__author__ = 'Mingxin Liu'

def application(environ, strat_response):
    strat_response('200 OK', [('Content-Type', 'text/html')])
    return [b'<h1>Hello, web!</h1>']