--- heaven7
require("src.core.init")
local CF = require("CollectionFamily")
local List = require("List")
local Set = require("Set")
local Map = require("Map")
local Transform = require("Transform")

-- test list transform
-- map
local list = List.new({ "a", "b", "c" })

local ts = Transform.new(list)
local function map(context, index, value)
    return tostring(index)..tostring(value)
end
local listResult = ts.map(map)
assert(listResult.size() == 3)

local function map2(context, index, value)
    return tostring(context)..tostring(index)..tostring(value)
end
listResult = ts.map(map2, "XX")
assert(listResult.size() == 3)
-- mapSet
listResult = ts.mapSet(map2, "XX")
assert(listResult.getCollectionType() == CF.COLLECTION_TYPE_SET)

-- filter
local function filter(context, index, value)
    if(index == 1) then
        return true;
    end
end
listResult = ts.filter(filter)
assert(listResult.size() == 1)

-- sum
local function sum(context, index, value)
    return value
end
local sumResult = ts.sum(sum)
assert(sumResult == "abc")

-- avg
listResult = Transform.new(List.new({ 1,2,3 })).average(sum)
assert(listResult == 2)

-- map map
local function keyFunc(context, index, value)
    return tostring("Google__")..tostring(index)
end
local function valueFunc(context, index, value)
    return value
end
listResult = Transform.new(list).mapMap(keyFunc, valueFunc)
-- Google__1: a ,  Google__2: b,  Google__3: c
assert(listResult.size() == 3)
assert(listResult.containsKey("Google__1"))
assert(listResult.containsKey("Google__2"))
assert(listResult.containsKey("Google__3"))
assert(listResult.containsValue("a"))
assert(listResult.containsValue("b"))
assert(listResult.containsValue("b"))