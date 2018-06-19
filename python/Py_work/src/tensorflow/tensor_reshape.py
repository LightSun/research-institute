"""
tf.reshape函数
"""
import tensorflow as tf

with tf.Graph().as_default():
    # 总体元素个数 3*4*5 = 60.所有根据这个重构的张量 元素个数必须满足这个。
    rank_three_tensor = tf.ones([3, 4, 5])
    matrix = tf.reshape(rank_three_tensor, [6, 10])  # Reshape existing content into
    # a 6x10 matrix
    matrixB = tf.reshape(matrix, [3, -1])  # Reshape existing content into a 3x20
    # matrix. -1 tells reshape to calculate
    # the size of this dimension.
    matrixAlt = tf.reshape(matrixB, [4, 3, -1])  # Reshape existing content into a
    # 4x3x5 tensor

    # Note that the number of elements of the reshaped Tensors has to match the
    # original number of elements. Therefore, the following example generates an
    # error because no possible value for the last dimension will match the number
    # of elements.
    # yet_another = tf.reshape(matrixAlt, [13, 2, -1])  # ERROR! 无法整除 13 * 2 * any 都不能得到60. 所以不行。

    # tensor数据类型变换
    float_tensor = tf.cast(tf.constant([1, 2, 3]), dtype=tf.float32)
    # placeholder
    p = tf.placeholder(tf.float32)
    t = p + 1.0
    # tensor评估 (必须在session中激活. 并且必须在对应的graph中赋值)
    with tf.Session() as sess:
        print(float_tensor.eval())
        # print("placeholder", p.eval()) # failed.

        # 用2.0 代替之前定义的placeholder p. 得出结果
        print("placeholder2", t.eval(feed_dict={p: 2.0}))

        # 在评估result时，因为依赖 Print(). 所以。会打印
        result = tf.Print(float_tensor, [float_tensor, matrixAlt]) # 打印tensor列表
        print(result.eval())
