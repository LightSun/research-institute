--
-- http://lua-users.org/wiki/InheritanceTutorial
--

local dir = "E:/study/github/research-institute/lua/LuaStudy/src/module/listImpl.lua";
package.path = dir .. ";" .. package.path
local listImpl = require("listImpl")


local List = {};
function List.new(array)
    local super = listImpl:new(array);
    local self = setmetatable({}, super)
    return self;
end


local list = listImpl:new();
list:add(1);
list:add(2);
list:add(3);

print(list:get(1))

list2 = List.new();
print(list2:size())
print(list2:get(1))
