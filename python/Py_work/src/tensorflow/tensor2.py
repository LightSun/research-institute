"""
张量的形状： 行列大小
      广播
      矩阵的乘法 （函数 matmul）
      改变形状（改变行列的大小reshape）
"""
import tensorflow as tf

with tf.Graph().as_default():
    # 空向量
    scalar = tf.zeros([])
    # 3个元素的vector
    vector = tf.zeros([3])
    # 2行3列的矩阵
    matrix = tf.zeros([2, 3])

    with tf.Session() as sess:
        print("scalar has shape.", scalar.get_shape(), 'and value:\n', scalar.eval())
        print("vector has shape.", vector.get_shape(), 'and value:\n', vector.eval())
        print("matrix has shape.", matrix.get_shape(), 'and value:\n', matrix.eval())

# 类似 numpy的广播 https://docs.scipy.org/doc/numpy-1.10.1/user/basics.broadcasting.html
with tf.Graph().as_default():
    # Create a six-element vector (1-D tensor).
    primes = tf.constant([2, 3, 5, 7, 11, 13], dtype=tf.int32)

    # Create a constant scalar with value 1.
    ones = tf.constant(1, dtype=tf.int32)

    # Add the two tensors. The resulting tensor is a six-element vector.
    just_beyond_primes = tf.add(primes, ones)

    with tf.Session() as sess:
        print(just_beyond_primes.eval())

# 矩阵的乘法( 第一个的列数必须等于第2个的行数)
with tf.Graph().as_default():
    # Create a matrix (2-d tensor) with 3 rows and 4 columns.
    x = tf.constant([[5, 2, 4, 3], [5, 1, 6, -2], [-1, 3, -1, -2]],
                    dtype=tf.int32)

    # Create a matrix with 4 rows and 2 columns.
    y = tf.constant([[2, 2], [3, 5], [4, 5], [1, 6]], dtype=tf.int32)

    # Multiply `x` by `y`.
    # The resulting matrix will have 3 rows and 2 columns.
    matrix_multiply_result = tf.matmul(x, y)

    with tf.Session() as sess:
        print(matrix_multiply_result.eval())

# 张量的变形 (必须保证元素个数不变 --- 维度可以变)
with tf.Graph().as_default():
    # 8行2列
    matrix = tf.constant([[1, 2], [3, 4], [5, 6], [7, 8],
                          [9, 10], [11, 12], [13, 14], [15, 16]], dtype=tf.int32)
    # reshape 取决于。元素个数。 一定要保证元素个数相等，否则失败.
    mat_28 = tf.reshape(matrix, [2, 8])
    mat_44 = tf.reshape(matrix, [4, 4])
    # 上升或者下降维度
    mat_224 = tf.reshape(matrix, [2, 2, 4])
    mat_16 = tf.reshape(matrix, [16])

    with tf.Session() as sess:
        print("raw matrix", matrix.eval())
        print("matrix_28", mat_28.eval())
        print("matrix_44", mat_44.eval())
        print("mat_224", mat_224.eval())
        print("mat_16", mat_16.eval())
