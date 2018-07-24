import tensorflow as tf
from tensorflow.python.client import device_lib


# print(device_lib.list_local_devices())
# print(len(device_lib.list_local_devices()))

def isGpu():
    devs = device_lib.list_local_devices()
    return devs[len(devs) - 1].device_type.find("GPU") >= 0


def configGpuProp():
    if isGpu():
        config = tf.ConfigProto()
        config.gpu_options.allow_growth = True
        return tf.Session(config=config)


# print(isGpu())
# print(configGpuProp())  # None