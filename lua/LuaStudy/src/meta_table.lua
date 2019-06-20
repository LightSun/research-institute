--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 16:42
-- To change this template use File | Settings | File Templates.
--

-- 元表 --
--[[
setmetatable(table,metatable): 对指定 table 设置元表(metatable)，如果元表(metatable)中存在 __metatable 键值，setmetatable 会失败。
getmetatable(table): 返回对象的元表(metatable)。  【如果元表(metatable)中存在__metatable键值，当返回__metatable的值】
--]]

-- setmetatable函数： 设置table的元表.
local mytable = setmetatable({key1 = "value1"}, {
    -- 如果存在 __index则会调用。第一个参数为table. 第二个参数为key.
    __index = function(mytable, key)
        if key == "key2" then
            return "meta_table_value"
        else
            return nil
        end
    end
})

print(mytable.key1, mytable.key2)

local map = {}
metaMap = setmetatable({key1 = "value1" }, {
    __newindex  = map
})

print(">> map: "..metaMap.key1)
-- 给元表 metaMap 赋值(metaMap不存在newKey)。实际上是给 map赋值了。
metaMap.newKey = "newValue2"
print(metaMap.newKey, map.newKey)

-- 给元表 metaMap 赋值(metaMap存在key1)。
metaMap.key1="new_value1"
print(metaMap.key1, map.key1);


print(getmetatable("lua")) -->table: 002F19B8
print(getmetatable(10))    -->nil