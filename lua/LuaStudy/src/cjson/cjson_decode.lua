local cjson = require "cjson"

-- 创建实例
local cjson2 = cjson.new()

-- 布尔类型
local json_bool = "false"
local lua_bool = cjson2.decode(json_bool)
print(type(lua_bool))

-- 数组类型
local json_array = "[9, 8, 7, 6, 5, 4, 3, 2, 1]"
local lua_array = cjson2.decode(json_array)
print(type(lua_array))

for k, v in ipairs(lua_array) do
    print("k = " .. k)
    print("v = " .. v)
end


-- 数值类型
local json_number = "666.666"
local lua_number = cjson2.decode(json_number)
print(type(lua_number))
--[[
print(lua_number)
--]]

-- 字符串类型
local json_string = "\"I am test1280\""
local lua_string = cjson2.decode(json_string)
print(type(lua_string))
--[[
print(lua_string)
--]]

-- 对象类型
local json_object = "{\"name\":\"Jiang\",\"addr\":\"BeiJing\",\"age\":24,\"tel\":\"1569989xxxx\",\"email\":\"1569989xxxx@126.com\"}"
local lua_object = cjson2.decode(json_object)
print(type(lua_object))
--[[
for k, v in pairs(lua_object) do
    print("k = " .. k)
    print("v = " .. v)
end
--]]
---------------------
print(package.cpath)