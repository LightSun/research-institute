"""
张量的变量类型的初始化, 练习

练习 2：模拟投掷两个骰子 10 次。
创建一个骰子模拟，在模拟中生成一个 10x3 二维张量，其中：

列 1 和 2 均存储一个骰子的一次投掷值。
列 3 存储同一行中列 1 和 2 的值的总和。
例如，第一行中可能会包含以下值：

列 1 存储 4
列 2 存储 3
列 3 存储 7
"""

import tensorflow as tf
import random as ran


def genRandomInt():
    return ran.randint(0, 100)


# 效率低
def set_value(matrix, x, y, val):
    # 提取出要更新的行
    row = tf.gather(matrix, x)
    # 构造这行的新数据
    new_row = tf.concat([row[:y], [val], row[y + 1:]], axis=0)
    # 使用 tf.scatter_update 方法进正行替换
    matrix.assign(tf.scatter_update(matrix, x, new_row))


# 再构建一个差值张量然后做个加法
def set_value2(matrix, x, y, val):
    # 得到张量的宽和高，即第一维和第二维的Size
    w = int(matrix.get_shape()[0])
    h = int(matrix.get_shape()[1])
    # 构造一个只有目标位置有值的稀疏矩阵(tf.SparseTensor)，其值为目标值于原始值的差
    val_diff = val - matrix[x][y]
    diff_matrix = tf.sparse_tensor_to_dense(tf.SparseTensor(indices=[x, y], values=[val_diff], dense_shape=[w, h]))
    # 用 Variable.assign_add 将两个矩阵相加
    matrix.assign_add(diff_matrix)


with tf.Graph().as_default():
    # 10 行 1列
    dice1 = tf.Variable(tf.random_uniform([10, 1], minval=1, maxval=7, dtype=tf.int32))
    # 10 行 1列
    dice2 = tf.Variable(tf.random_uniform([10, 1], minval=1, maxval=7, dtype=tf.int32))
    # 10 行 1列
    dice_sum = tf.add(dice1, dice2)

    # merge 按列合并。 10行3列
    result = tf.concat(values=[dice1, dice2, dice_sum], axis=1)

    with tf.Session() as sess:
        init = tf.global_variables_initializer()
        sess.run(init)
        print(result.eval())
