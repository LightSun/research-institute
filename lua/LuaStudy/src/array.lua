--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 15:45
-- To change this template use File | Settings | File Templates.
--

local function printArray(arr)
    print("---------- printArray ------------");
    for i = 1, #arr do
        print(arr[i]);
    end
end

local array = {}

-- lua 数组从1开始。 但是可以指定从0和负数开始
--[[for i = -2, 2 do
    array[i] = i * 2
end]]

array = { 11, 33, 22}
printArray(array);

-- crud

-- add
table.insert(array, 100);
table.insert(array, 3, 300);
printArray(array);

-- remove
-- array[4] = nil 使用这个删除后，如果这个元素索引不是最后一个，则会导致后面的元素一起被删除.
table.remove(array, 3) -- 只删除指定索引的元素。
printArray(array);

-- update
-- query

-- sort
local function sortArr(a, b)
    return a >= b; -- 降序
end
table.sort(array, sortArr);
printArray(array);

-- sort2
local arr = {"11", "3a", "22" }
table.sort(arr)
printArray(arr)
