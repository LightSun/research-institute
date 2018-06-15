"""
张量的变形, 训练:
      改变两个张量的形状，使其能够相乘
"""
import tensorflow as tf

# 张量的变形 (必须保证元素个数不变 --- 维度可以变)
with tf.Graph().as_default():
    # 1维数组
    mat1 = tf.constant([5, 3, 2, 7, 1, 4], dtype=tf.int32)
    # 1维数组
    mat2 = tf.constant([4, 6, 3], dtype=tf.int32)

    mat2_13 = tf.reshape(mat2, [1, 3])
    mat1_32 = tf.reshape(mat1, [3, 2])

    result = tf.matmul(mat2_13, mat1_32)

    with tf.Session() as sess:
        print("mat1_32", mat1_32.eval())
        print("mat2_13", mat2_13.eval())
        print("result", result.eval())
