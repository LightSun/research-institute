
"""
一个冒号
参数大于零：表示从左向右数的下标
参数小于零：表示从右向左数倒数第几个数（不包括该数）

一个冒号
参数大于零：表示从左向右数的下标
参数小于零：表示从右向左数倒数第几个数（不包括该数）

两个冒号
前两个参数的作用和上面一样，但三个参数是间距
大于零：从左向右返回数组
小于零：从右向左返回数组
"""
array=[0,1,2,3,4,5,6,7,8,9]
print(array[1])     # 第一个元素
print(array[1:3])   # start-index to end-index
print(array[0:-2])  # index为0的第一个元素（include）开始 ---> 倒数第2个元素（exclude）
print(array[2:-5]) # index为2的第一个元素(include)开始 -> 倒数第5个元素（exclude）。

print(array[::3])   # 正向输出,index 间距为3
print(array[::-1])  # 反向输出，间距为1
print(array[::-2])  # 反向输出，间距为2
print(array[:-1])   # 正向输出 ,index为0的倒数第一个元素（exclude）开始。所有。
print(array[:3])    # 正向输出 ,index为3的倒数第一个元素（exclude）开始。所有。
print(array[3:])    # 正向输出 ,index为3的第一个元素（include）开始。所有。