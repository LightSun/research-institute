
"""
TensorFlow 基本概念:  张量，指令，图，会话

============ 张量 ==================
概念： 任意维度的数组

标量： 0 维数组/0阶张量. eg: 5, 'hello'
矢量： 1 维数组/1阶张量  eg: [1,2,3,4,6] or [5]
矩阵:  2 维数组/2阶张量  eg: [[1,2,3][4,5,6]]

============== 指令 ======================
用于创建，销毁，操作张量

============== 图 (计算图/数据流图)======================
TensorFlow可以创建1个或者多个图.
  图的节点是指令。图的边是张量。
      张量流经图在每个节点由1个指令操控.
          节点的输出张量往往会变为后续的输入张量

TensorFlow 会实现延迟执行模型. 意味着系统只会根据相关节点的需求在需要时计算几点。
张量可以是常量/变量存储在图中。
图必须在会话（Session）中运行，会话存储了所运行的图的状态

常量定义(constant函数)：   x = tf.constant([5.2])
变量定义（Variable函数）：   y = tf.Variable([5])
变量赋值(assign)：
      y = tf.Variable([0])
      y = y.assign([5])
"""
import tensorflow as tf

# initialization = tf.global_variables_initializer()
c = tf.constant('Hello, world!')
y = tf.Variable([0])
y = y.assign([5])

with tf.Session() as sess:
    print(sess.run(c))
    print(y.eval(session=sess)) #评估，输出


