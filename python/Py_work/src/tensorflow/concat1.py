"""
tf.cancat 函数
tf.concat(values, concat_dim, name='concat')

concat_dim = 0 .表示行
concat_dim = 1 .表示列
"""

import tensorflow as tf

with tf.Graph().as_default():
    # 2行3列
    t1 = [[1, 2, 3], [4, 5, 6]]
    # 2行3列
    t2 = [[7, 8, 9], [10, 11, 12]]
    # 0表示 行数合并. 变成4行3列
    result1 = tf.concat([t1, t2], 0)
    # 0表示 列数合并. 变成2行6列
    result2 = tf.concat([t1, t2], 1)

    with tf.Session() as sess:
        print(result1.eval())
        print(result2.eval())
# [[ 1  2  3]
#  [ 4  5  6]
# [ 7  8  9]
# [10 11 12]]
# [[ 1  2  3  7  8  9]
#  [ 4  5  6 10 11 12]]