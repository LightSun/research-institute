--[[
__add: 对+进行重载

__sub: 对-进行重载

__mul: 对*进行重载

__div: 对/进行重载

__unm: 对相反数进行重载

__mod: 对%进行重载

__pow: 对^进行重载

__concat: 对连接操作符进行重载

__eq: 对==进行重载

__lt: 对<进行重载

__le: 对<=进行重载

__tostring: 类似于C++中对<<的重载 只要做了该重载，在使用print时就会使用对应的函数做处理后再输出
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

------------------------------------
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


--print(getmetatable("lua")) -->table: 002F19B8
--print(getmetatable(10))    -->nil