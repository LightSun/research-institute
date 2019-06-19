--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 15:14
-- To change this template use File | Settings | File Templates.
--

-- 高版本移除了。table.maxn()等函数
tbtest1 = {
    a = "a",
    [1] = "1",
    b = "b",
    c = "c",
    [2] = "2",
    [4] = "4",
}

print("---------- pairs(tbtest1)") -- 遍历所有元素。
for key, value in pairs(tbtest1) do
    print(tostring(key)..":"..tostring(value))
end

print("---------- ipairs(tbtest1)") -- 从1开始, 遍历数字索引的元素，直到不连续
for key, value in ipairs(tbtest1) do
    print(tostring(key)..":"..tostring(value))
end

-- 用于数组型table。且下表连续. 不连续则value为nil
-- #(table) 表示获取长度
print("--------- i=1,#(tbtest1)")
for i=1, #tbtest1 do
    print(tostring(i)..":"..tostring(tbtest1[i]))
end

-- 操作map. crud
print("LUA>>>>>>the maxn of table tabTest :", #tbtest1, "\n") -- 获取长度
-- add
print(tostring(8)..":"..tostring(tbtest1[8]))
tbtest1[8] = 999
print(tostring(8)..":"..tostring(tbtest1[8]))

-- remove
tbtest1[8] = nil;
print(tostring(8)..":"..tostring(tbtest1[8]))

-- update
tbtest1[8] = 1299;
print(tostring(8)..":"..tostring(tbtest1[8]))

-- find/query