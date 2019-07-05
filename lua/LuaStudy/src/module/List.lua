
local dir = "src/module/?.lua";
package.path = dir..";"..package.path

local utils = require("TableUtils")
local it = require("Iterator")

function List(list)
    local self = {}
    self.elements =  list or {}

    function self.size()
        return #(self.elements)
    end

    function self.get(index)
        return (self.elements)[index + 1]
    end

    function self.remove(index)
        return table.remove(self.elements, index + 1);
    end

    function self.set(index , val)
        if index >= self.size() then
            error("index out of range")
        end
        (self.elements)[index + 1] = val
    end

    function self.add2(index, val)
        local len = self.size();
        table.insert(self.elements, index + 1, val)
        return self.size() > len;
    end

    function self.insert(val, index)
        return self.add2(index, val);
    end

    function self.addMulti(...)
        local arg = {...};
        for i = 1 ,#arg do
            self.add(arg[i])
        end
    end

    function self.add(val)
        local len = self.size(self);
        return self.add2(len, val)
    end

    -- ios
    function self.map(mapFunc)

    end

    function self.append(val)
        self.add(val)
    end

    function self.copy()
        local a = List(utils.copyTable(self.elements))
        return a;
    end

    function self.iterator()
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

    function self.indexOf()
        for key, value in pairs(self.elements) do
            if(value == obj)then
                return key - 1;
            end
        end
        return -1;
    end

    function self.lastIndexOf(obj)
        for i = #(self.elements), 1, -1 do
            local val = (self.elements)[i]
            -- print(type(val))
            if val == obj then
                return i - 1;
            end
        end
        return -1;
    end


    function self.subList(from, to)
        local tab = self.elements;
        local size = #tab;
        if(to >= size) then
            error("wrong index of to = " + to)
        end

        local newList = List();
        for i = from + 1, to + 1 do
            local val = tab[i]
            -- print("subList. ", val)
            newList.add(val)
        end
        return newList;
    end

    return self
end