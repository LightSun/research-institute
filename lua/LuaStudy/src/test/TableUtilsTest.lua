local dir    = "src/core/?.lua";
package.path = dir .. ";" .. package.path

local utils  = require("TableUtils")

-- test map map
local tab  = { ["p"] = 3,["v"] = 9 }
local func = function(cx, key, value)
    return tostring(key).."__"..tostring(value)
end
local resultTab = utils.mapTable(tab , nil , func, utils.MAP_TYPE_KEY)

utils.logTable(resultTab)


-- test map array ----
local array = { 1,  2, 3}
local func = function(cx, value)
    return tostring(value) .."__"
end
local newArray = utils.mapArray(array, nil, func);
utils.logArray(newArray)

-- test merge
local t1 = {1,2,3}
local t2 = {3,4,5}
local t = utils.mergeArray(nil, t1, t2);
utils.logArray(t)
-- 数组能准确的获取长度.
assert(#t == 6)


-- get map length can't use '#map'
local tab1 = {["a"] = 1, ["n"] = 2}
local tab2 = {["b"] = 1, ["c"] = 2}
local tabs = utils.mergeMap(nil, tab1, tab2);
utils.logTable(tabs)
print(#tabs) -- 0
print(#tab1)
-- key 不连续，无法获取map长度
--assert(#tab == 4)

local tab11 = {["a"] = 1, ["n"] = 2}
assert(utils.equals(tab1 ,tab11) == true)
local tab111 = {["a"] = 1, ["n"] = 2, ["c"] = 6}
assert(utils.containsAll(tab111, tab11))
assert(not utils.containsAll(tab11, tab111))
