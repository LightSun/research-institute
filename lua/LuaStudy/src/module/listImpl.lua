--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 下午 13:50
-- To change this template use File | Settings | File Templates.
--
local dir = "src/module/?.lua";
package.path = dir..";"..package.path
local utils = require("TableUtils")
local it = require("Iterator")

--[[local it2 = it.ListIterator();
print(it2)]]

-- make a list like java.  for lua inde start from 1, but here we start from 0
local List = {elements = {} }
function List:new(array)
    local arg = array or {}
    setmetatable(arg, self)
    self.__index = self
    self.elements = arg;
    -- print(self, arg)
    -- self 指当前文件table.
    return arg;
end

function List:size()
    return #(self.elements);
end

function List:get(index)
    return (self.elements)[index + 1];
end

function List:remove(index)
    return table.remove(self.elements, index + 1);
end

function List:set(index , val)
    if index >= self:size() then
        error("index out of range")
    end
    (self.elements)[index + 1] = val
end

function List:add2(index, val)
    local size = self:size();
    table.insert(self.elements, index + 1, val)
    return self:size() > size;
end

function List:addMulti(...)
    local arg = {...};
    for i = 1 ,#arg do
        self:add(arg[i])
    end
end

function List:add(val)
    local size = self:size();
    return self:add2(size, val)
end

function List:copy()
    local a = List:new(utils.copyTable(self.elements))
    return a;
end

function List:iterator()
    local tab = self.elements;
    local function listIterator()
        local self = it.new()
        self.__index = self;
        self.index = -1;
        self.size = #tab;
        function self.hasNext()
            return self.index + 1 < self.size;
        end

        function self.next()
            self.index = self.index + 1
            return tab[self.index + 1]
        end
        return self;
    end
    return listIterator();
end

function List:indexOf(obj)
    for key, value in pairs(self.elements) do
        if(value == obj)then
            return key - 1;
        end
    end
    return -1;
end

function List:lastIndexOf(obj)
    for i = #(self.elements), 1, -1 do
        local val = (self.elements)[i]
        -- print(type(val))
        if val == obj then
            return i - 1;
        end
    end
    return -1;
end


function List:subList(from, to)
    local tab = self.elements;
    local size = #tab;
    if(to >= size) then
        error("wrong index of to = " + to)
    end

    local newList = List:new();
    for i = from + 1, to + 1 do
        local val = tab[i]
        print("subList: ", val)
        newList:add(val)
    end
    return newList;
end

--[[local list = List:new();
list:add("8")
list:add("32424")

print(utils)
print(list)
print(type(list.elements[1]))
utils.logTable(list.elements)

local copyList = list:copy();
utils.logArray(copyList.elements)

print(list:get(1))
print(list:indexOf("32424"))
print(list:indexOf("1"))

list:add("8")
print(list:size());
print(list:lastIndexOf("8"))

print("-------- start subList ------")
utils.logArray(list.elements)
local subList = list:subList(1,2)
utils.logArray(subList.elements)

print("-------- start set() ------")
subList:set(1, 200)
utils.logArray(subList.elements)

print("-------- start addMulti() ------")
subList:addMulti(5,6,7)
utils.logArray(subList.elements)

print("-------- start iterator() ------")
local ita = subList:iterator();
while(ita.hasNext()) do
    print(ita.next())
    end]]

return List;