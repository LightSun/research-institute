--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 15:07
-- To change this template use File | Settings | File Templates.
-- and, or ,not ********************

-- lua数组所以从1开始
map = {a = "11", b= "22", "33", 55 , c ="ff"};

-- 使用ipairs 遍历
for key, value in pairs(map) do
    print(key.."___"..value)
end

print("--------------") --下标1开始，一直遍历到key不连续为止
for key, value in ipairs(map) do
    print(key.."___"..value)
end