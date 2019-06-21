--
-- http://lua-users.org/wiki/InheritanceTutorial
--

local dir = "src/module/?.lua";
package.path = dir .. ";" .. package.path
local listImpl = require("listImpl")



local list = listImpl:new();
local list2 = listImpl:new();
print(list)
print(list2)
list:add(1);
list:add(2);
list:add(3);

print(list:get(1))

local list2 = listImpl:new({4,5,6});
print(list2:get(2))
list2:remove(1)
assert(list2:size()==2)
print(type(list2.elements))
print(list2.elements[2])

local state, code = pcall(list2.elements[3])
-- print(state, code)