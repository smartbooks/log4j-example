# -*- coding: utf-8 -*-

import sys

input_buffer = sys.stdin.readline().strip()

print("len=", len(input_buffer),"input_buffer=",input_buffer)

sys.stdout.write(input_buffer)
sys.stdout.flush()
