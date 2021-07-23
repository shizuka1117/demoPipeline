#!/usr/bin/python
# -*- coding: utf-8 -*-
import pandas as pd
import numpy as np
from sys import argv

# 需要修改读入路径
turn_left_x = pd.read_csv("D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\turnLeft_40.csv",header = None).values
turn_right_x = pd.read_csv('D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\turnRight_40.csv',header = None).values
go_straight_x = pd.read_csv('D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\goStraight_40.csv',header = None).values
# 将三者数据结合到一个数据结构list中
all_in_one = np.concatenate((turn_left_x, turn_right_x,go_straight_x), axis = 0)
all_in_one = all_in_one[:,:4]
sequences = list()
# 数据切分，每次以长度40，划分为一个样本
for i in range(0,3000):
    start = 40*i
    end = start+40
    sequences.append(all_in_one[start:end, :])

# 数据补齐
to_pad = 40 # 修改样本长度
new_seq = []
for one_seq in sequences:
    len_one_seq = len(one_seq)
    if len_one_seq != to_pad:
        n = to_pad - len_one_seq
        to_concat = np.repeat(one_seq[-1],n).reshape(4,n).transpose()
        new_one_seq = np.concatenate([one_seq, to_concat])
    else:
        new_one_seq = one_seq
    new_seq.append(new_one_seq)

np.save(file="D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\data.npy", arr=new_seq, fix_imports=True)#需要更改输出路径