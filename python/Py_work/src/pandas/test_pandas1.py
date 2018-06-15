
"""
学习目标：

pandas 库主要结构：  DataFrame 和 Series 数据结构
存取和处理 DataFrame 和 Series 中的数据
将 CSV 数据导入 pandas 库的 DataFrame
对 DataFrame 重建索引来随机打乱数据
"""
import pandas as pd
print(pd.__version__)

# 创建 2个 Series对象
city_names = pd.Series(['San Francisco', 'San Jose', 'Sacramento'])
population = pd.Series([852469, 1015785, 485199])
print(city_names)
print(population)

# 如果Series 长度不一致，会自动用NA/NAN 填充
data_frame = pd.DataFrame({'City name': city_names, 'Population': population})
print(data_frame)
print(type(data_frame['City name']))
print(type(data_frame['City name'][1]))
print(type(data_frame[0:2]))


california_housing_dataframe = pd.read_csv("https://storage.googleapis.com/mledu-datasets/california_housing_train.csv", sep=",")
describe = california_housing_dataframe.describe()
print(describe)

# 显示前几个记录
print(california_housing_dataframe.head())
# 一个列的中值分布
hist = california_housing_dataframe.hist("housing_median_age")
