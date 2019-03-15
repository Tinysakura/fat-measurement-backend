import keras
from tensorflow.contrib.keras.api.keras.preprocessing.image import ImageDataGenerator,img_to_array
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation, Flatten
from keras.layers.advanced_activations import PReLU
from keras.layers.convolutional import Conv2D, MaxPooling2D,ZeroPadding2D
from keras.preprocessing.image import load_img, img_to_array
from keras.optimizers import  SGD, Adam
import cv2
#import tensorflow as tf
import numpy as np
import sys

def __CNN__():
    model = Sequential()#218*178*3
    # model.add(Conv2D(32, (3, 3), input_shape=(254, 309, 3)))
    model.add(Conv2D(32, (3, 3), input_shape=(112, 249, 3)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(32, (3, 3)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(64, (3, 3)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(64, (3, 3)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(64, (3, 3)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Flatten())
    model.add(Dense(128))
    model.add(Activation('relu'))

    model.add(Dense(128))
    model.add(Activation('relu'))
    model.add(Dropout(0.5))

    model.add(Dense(64))
    model.add(Activation('relu'))
    model.add(Dropout(0.5))

    # model.add(Dense(32))
    # model.add(Activation('relu'))
    # model.add(Dropout(0.5))

    model.add(Dense(1))
    model.summary()
    return model
model1 = __CNN__()
model1.load_weights('CNN_model_final_zhifang.h5')
#imageData = cv2.imread('ACB88.BMP')
imageData = cv2.imread(sys.argv[1])
imageData = np.asarray(imageData)
#print('img.shape', imageData.shape)
height = len(imageData)
width = len(imageData[0])
imageData = imageData[15:  127, 30 : width-30]
#print(imageData.shape)
imageData = imageData.reshape(1,112, 249, 3)
#print(imageData.shape)
a = model1.predict(imageData,batch_size=1,verbose=0)
print(a[0][0])
f = open("zhifang.txt","w")
f.write(str(a[0][0]))
f.close()
