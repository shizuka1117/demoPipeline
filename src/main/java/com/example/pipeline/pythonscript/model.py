import numpy as np
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
import tensorflow as tf
from keras import backend as K
from sys import argv

#修改作为参数
print("hello")
new_seq = np.load(file="D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\data.npy", allow_pickle=True)

y = [[1,0,0]]*1000 + [[0,0,1]]*1000 + [[0,1,0]]*1000
# 同时打乱 记录随机数种子状态 -> 打乱new_seq -> 设置之前记录的随机数种子 -> 打乱y
state = np.random.get_state()
np.random.shuffle(new_seq)
np.random.set_state(state)
np.random.shuffle(y)

# 播撒随机数种子
np.random.seed(7)

#创建模型
model = Sequential()
model.add(LSTM(64, input_shape = (40, 4)))
model.add(Dense(3, activation = 'softmax'))

X = tf.convert_to_tensor(new_seq, dtype=tf.float64)
Y = tf.convert_to_tensor(y, dtype=tf.float64)

# 定义metrics
# 召回率
def recall_m(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    possible_positives = K.sum(K.round(K.clip(y_true, 0, 1)))
    recall = true_positives / (possible_positives + K.epsilon())
    return recall
# 精确率
def precision_m(y_true, y_pred):
    true_positives = K.sum(K.round(K.clip(y_true * y_pred, 0, 1)))
    predicted_positives = K.sum(K.round(K.clip(y_pred, 0, 1)))
    precision = true_positives / (predicted_positives + K.epsilon())
    return precision
# F1
def f1_m(y_true, y_pred):
    precision = precision_m(y_true, y_pred)
    recall = recall_m(y_true, y_pred)
    return 2*((precision*recall)/(precision+recall+K.epsilon()))

# 编译模型
split = float(argv[1])
epochs = int(argv[2])
batch_size = int(argv[3])
model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['acc',f1_m,precision_m, recall_m])
history =  model.fit(X, Y, validation_split=split, epochs=epochs, batch_size=batch_size)
model.save('D:\\java\\source\\imdb\\src\\main\\resources\\static\\files\\my_model.h5') #返回
