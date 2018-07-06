"""
使用tensorflow 的基本步骤:

   learn target:
      学习基本的tensorflow 概念
      使用LinearRegressor类并基于单个输入特征预测各城市街区的房屋价值中位数
      通过使用均方根误差（RMSE）评估模型预测的准确率
      调整模型超参数（eg: 学习速率）提高模型准确率
"""

# setting
from __future__ import print_function
import math

from IPython import display
from matplotlib import cm, gridspec
from matplotlib import pyplot as plt

import numpy as np
import pandas as pd

from sklearn import metrics

import tensorflow as tf
from tensorflow.python.data import Dataset

# base set
tf.logging.set_verbosity(tf.logging.ERROR)
pd.options.display.max_rows = 10
pd.options.display.float_format = '{:.1f}'.format

# load data ,  https://storage.googleapis.com/mledu-datasets/california_housing_train.csv
california_housing_dataframe = pd.read_csv("california_housing_train.csv", sep=",")
california_housing_dataframe = california_housing_dataframe.reindex(
    np.random.permutation(california_housing_dataframe.index))
california_housing_dataframe["median_house_value"] /= 1000.0
# print(california_housing_dataframe)
# print(california_housing_dataframe.describe()) # 输出摘要信息

# 1, 定义特征，并配置特征列
my_feature = california_housing_dataframe[["total_rooms"]] # 一维数组
feature_columns = [tf.feature_column.numeric_column("total_rooms")] # _NumericColumn

print(my_feature)
print(feature_columns)

# 2, 定义目标(标签) 为什么targets用nd array.
targets = california_housing_dataframe["median_house_value"]
print(type(targets)) # pandas.core.series.Series
# print(targets.to_string()) # ok but no

# 3, 配置LinearRegressor
'''
使用 LinearRegressor 配置线性回归模型，并使用 GradientDescentOptimizer（它会实现小批量随机梯度下降法 (SGD)）训练该模型。
learning_rate 参数可控制梯度步长的大小。
'''
my_optimizer = tf.train.GradientDescentOptimizer(learning_rate=0.0000001)
# 通过 clip_gradients_by_norm 将梯度裁剪应用到我们的优化器
my_optimizer = tf.contrib.estimator.clip_gradients_by_norm(my_optimizer, 5.0)

linear_regressor = tf.estimator.LinearRegressor(
    feature_columns=feature_columns,
    optimizer=my_optimizer
)

# 4, 定义输入函数
'''
通过输入函数：告诉tensorflow如何对数据进行预处理。 以及模型训练期间如何批处理.
steps:
    Pandas数据特征 -> dataset -> multi batch data(batch_size) -> 周期数（num_epochs）
'''
def my_input_fn(features, targets, batch_size=1, shuffle=True, num_epochs=None):
    """Trains a linear regression model of one feature.  训练线性回归模型 of 1个特征
    Args:
      features: pandas DataFrame of features
      targets: pandas DataFrame of targets
      batch_size: Size of batches to be passed to the model
      shuffle: True or False. Whether to shuffle the data.
      num_epochs: Number of epochs for which data should be repeated. None = repeat indefinitely
    Returns:
      Tuple of (features, labels) for next data batch
    """
    # convert pandas data into a dict of np arrays.
    print("pre features: ", features)
    # {'total_rooms': array([4260., 2397., 2850., ..., 2542., 2448.,  352.])}
    features = {key: np.array(value) for key, value in dict(features).items()}
    print("after features: ", features)

    # construct a dataset, and configure batching/repeating
    ds = Dataset.from_tensor_slices((features, targets)) # warning 2Gb limit
    ds = ds.batch(batch_size).repeat(num_epochs)

    # shuffle the data, if specified
    if shuffle:
        ds = ds.shuffle(buffer_size=10000)

    # return the next batch of data.
    features, labels = ds.make_one_shot_iterator().get_next()
    return features, labels


# 5, train module
_ = linear_regressor.train(
    input_fn = lambda:my_input_fn(my_feature, targets),
    steps=100
)


# 6, 评估模型
prediction_input_fn = lambda :my_input_fn(my_feature, targets, num_epochs=1, shuffle=False)

predictions = linear_regressor.predict(input_fn=prediction_input_fn)
print("predictions 1", type(predictions))

predictions = np.array([item["predictions"][0] for item in predictions])
print("predictions 2", type(predictions))
# 均方误差(MSE),
mean_squared_error = metrics.mean_squared_error(predictions, targets)
# 均方根误差 (RMSE)
root_mean_squared_error = math.sqrt(mean_squared_error)
print("Mean Squared Error (on training data): %0.3f" % mean_squared_error)
print("Root Mean Squared Error (on training data): %0.3f" % root_mean_squared_error)

# 比较 RMSE 与最大最小值的差距
min_house_value = california_housing_dataframe["median_house_value"].min()
max_house_value = california_housing_dataframe["median_house_value"].max()
min_max_difference = max_house_value - min_house_value

print("Min. Median House Value: %0.3f" % min_house_value)
print("Max. Median House Value: %0.3f" % max_house_value)
print("Difference between Min. and Max.: %0.3f" % min_max_difference)
print("Root Mean Squared Error: %0.3f" % root_mean_squared_error)