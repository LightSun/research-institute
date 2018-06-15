"""
张量的变量类型的初始化.
  tensorflow 的特性：  变量初始化不是自动进行的.
"""

import tensorflow as tf

with tf.Graph().as_default():
    v = tf.Variable([3])
    # 使用分布初始化变量 with mean 1 and standard deviation 0.35.
    w = tf.Variable(tf.random_normal([1], mean=1.0, stddev=0.35))

    with tf.Session() as sess:
        try:
            v.eval()
        except tf.errors.FailedPreconditionError as e:
            print("Caught expected error: ", e) # 没有初始化所以异常

    # 变量的值会保存在会话中， 新的会话需要重新初始化或者赋值
    with tf.Session() as sess:
        init = tf.global_variables_initializer()
        # 初始化变量. 也可以通过tf.assign函数赋值
        sess.run(init)
        print(v.eval())
        print(w.eval())