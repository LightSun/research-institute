local dir    = "src/core/?.lua";
package.path = dir .. ";" .. package.path

local utils  = require("TableUtils")

local tab    = { ["p"] = 3,["v"] = 9 }

local func = function(cx, key, value)
    return tostring(key).."__"..tostring(value)
end
local resultTab = utils.mapTable(tab , nil , func, utils.MAP_TYPE_KEY)

utils.logTable(resultTab)


local array = { 1,  2, 3}
local func = function(cx, value)
    return tostring(value) .."__"
end
local newArray = utils.mapArray(array, nil, func);
utils.logArray(newArray)