
local cjson = require "cjson"

-- 创建实例
local cjson2 = cjson.new()

-- 布尔类型
local lua_bool = true
print(cjson2.encode(lua_bool))

-- 数组类型
local lua_array = {1, 2, 3, 4, 5, 6}
print(cjson2.encode(lua_array))

-- 数值类型
local lua_number = 6.66
print(cjson2.encode(lua_number))

-- 字符串类型
local lua_string = "I am test1280"
print(cjson2.encode(lua_string))

-- 对象类型
local lua_object = {
    ["name"] = "Jiang",
    ["age"] = 24,
    ["addr"] = "BeiJing",
    ["email"] = "1569989xxxx@126.com",
    ["tel"] = "1569989xxxx",
    "a"
}
print(cjson2.encode(lua_object))