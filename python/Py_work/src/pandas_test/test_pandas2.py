
"""
pandas操作数据
"""
import pandas as pd
import numpy as np

city_names = pd.Series(['San Francisco', 'San Jose', 'Sacramento'])
population = pd.Series([852469, 1015785, 485199])

# 如果Series 长度不一致，会自动用NA/NAN 填充
data_frame = pd.DataFrame({'City name': city_names, 'Population': population})

print(population / 1000)
np.log(population)

# 单列变化。通过函数表达式。转化为bool
series = population.apply(lambda val: val > 1000000)
print(series)


# 添加新的数据列
data_frame['Area square miles'] = pd.Series([46.87, 176.53, 97.92]) #面积
data_frame['Population density']=data_frame['Population']/data_frame['Area square miles']

print(data_frame)
print(data_frame.to_excel)


# 练习1
data_frame['test_bool'] = data_frame['City name'].apply(lambda val: val.startswith('San')) &\
                          data_frame['Area square miles'] > 50

print(data_frame['test_bool'])

# 重建索引.排列各行的顺序. return 新的DataFrame
data_frame = data_frame.reindex([2, 0, 1])
print(data_frame)

# 用numpy 随机函数.
data_frame = data_frame.reindex(np.random.permutation(data_frame.index))
# data_frame = data_frame.reindex(np.random.permutation(data_frame.__len__()))
print(data_frame)
print(np.random.permutation(data_frame.__len__()))

data_frame = data_frame.reindex([0,3,5,7, 4,8,2])
print(data_frame)