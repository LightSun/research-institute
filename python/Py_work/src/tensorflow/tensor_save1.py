
"""
模型的保存 和 恢复
"""

import tensorflow as tf

# Create some variables.
v1 = tf.get_variable("v1", shape=[3], initializer = tf.ones_initializer)
v2 = tf.get_variable("v2", shape=[5], initializer = tf.zeros_initializer)

inc_v1 = v1.assign(v1+1)
dec_v2 = v2.assign(v2-1)

# Add an op to initialize the variables.
init_op = tf.global_variables_initializer()

# Add ops to save and restore all the variables.
saver = tf.train.Saver(max_to_keep=1)

# Later, launch the model, initialize the variables, do some work, and save the
# variables to disk.
with tf.Session() as sess:
    sess.run(init_op)
    # Do some work with the model. 在这里你可以做你想做的
    inc_v1.op.run()
    dec_v2.op.run()
    print("pre-save inc_v1", inc_v1.eval())
    print("pre-save dec_v2", inc_v1.eval())
    # Save the variables to disk.
    save_path = saver.save(sess, "/tmp/model.ckpt")
    print("Model saved in path: %s" % save_path)

# ============== 恢复 tensor ===================
tf.reset_default_graph()
# Create some variables.
v1 = tf.get_variable("v1", [3], initializer = tf.zeros_initializer)
v2 = tf.get_variable("v2", [5], initializer = tf.zeros_initializer)

# Add ops to save and restore only `v2` using the name "v2"
saver = tf.train.Saver({"v2": v2})

# Use the saver object normally after that.
with tf.Session() as sess:
    # Initialize v1 since the saver will not.
    v1.initializer.run()
    saver.restore(sess, "/tmp/model.ckpt")

    print("v1 : %s" % v1.eval())
    print("v2 : %s" % v2.eval())
