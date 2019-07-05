--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 14:49
-- To change this template use File | Settings | File Templates.
--

-- 标识符： 开头- 下划线和字母。 其余下划线，字幕，数字

-- 数据类型： nil, boolean, number, string, function, table, thread, userdata
local function test()
    print("---------")
end

print(type(nil))
print(type(true))
print(type(10 * 5))
print(type("Hello lua"))
print(type(test))  -- 需要将函数,放在前面
print(type(print))

-- 任何一个未定义的都是nil
print(type(X)=="nil")  -- 比较时 加双引号
-- lua 将false 和nil看作假 ， 其他都是真 ---------------
if false or nil then
    print("至少有一个是 true")
else
    print("false 和 nil 都为 false!")
end

map = {a = "11", b= "22", "33", 55};
print(type(map))


arr = {1,2,3 }
print(type(arr))

---------------------------------------------------
for k,v in pairs(map) do
    print(k.. "__" ..v)
end
--[[
1__33
2__55
b__22
a__11
--]]