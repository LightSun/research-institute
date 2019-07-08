
local dir = "src/core/?.lua";
package.path = dir..";"..package.path

local utils = require("TableUtils")
local it = require("Iterator")
local coll = require("Collection")

local module = {};

function module.new(list)
    -- check
    if( list and type(list) ~= "table") then
        error("param list must be table")
    end
    --- only need the first element
    local function create(...)
        return utils.getAt(0, ...)
    end
    local self = coll.new("List", create, list)

    function self.get(index)
        return (self)[index + 1]
    end

    function self.remove(e)
        if(type(e) == "table") then
            error("no support now")
        else if type(e) == "function" then
            error("no support now")
        else
            for k, v in pairs(self) do
                if(v == e) then
                    self.removeAt(k)
                    break
                end

            end
        end
        end
    end

    function self.removeAt(index)
        if(table.remove(self, index + 1)) then
            self.addSize(-1)
            return true
        else
            return false;
        end
    end

    function self.set(index , val)
        if index >= self.size() then
            error("index out of range")
        end
        (self)[index + 1] = val
    end

    function self.add2(index, val)
        local len = self.size();
        table.insert(self, index + 1, val)
        self.addSize(1)
       return self.size() > len
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
        local len = self.size();
        return self.add2(len, val)
    end

    -- ios
    function self.append(val)
        self.add(val)
    end

    function self.copy()
        local a = module.new(utils.copyTable(self))
        a.recomputeSize()
        return a;
    end

    function self.iterator()
        local tab = self;
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

    function self.indexOf(obj)
        for key, value in pairs(self) do
            if(value == obj)then
                return key - 1;
            end
        end
        return -1;
    end

    function self.lastIndexOf(obj)
        for i = #(self), 1, -1 do
            local val = (self)[i]
            -- print(type(val))
            if val == obj then
                return i - 1;
            end
        end
        return -1;
    end


    function self.subList(from, to)
        local tab = self;
        local size = #tab;
        if(to >= size) then
            error("wrong index of to = " + to)
        end

        local newList = module.new();
        for i = from + 1, to + 1 do
            local val = tab[i]
            -- print("subList. ", val)
            newList.add(val)
        end
        newList.recomputeSize()
        return newList;
    end

    function self.contains(e)
        return self.indexOf(e) >= 0
    end

    function self.containsAll(collection)
        if(type(collection) ~= "table") then
            return nil
        end
        return utils.containsAll(self, collection)
    end

    function self.map(context, func)
        return utils.mapArray(self, context, func)
    end

    function self.addAll(collection)
        local result = utils.mergeArrayWithFlags(self, 0, collection)
        result.recomputeSize()
        return result
    end

    self.recomputeSize()

    -- meta methods
    local meta = {
        __eq = function(t1, t2)
            return utils.equals(t1, t2);
        end
        ,__add = function(t1, t2)
            return t1.addAll(t2);
        end
    }
    setmetatable(self, meta)
    return self
end

setmetatable(module, {
    __call = function (cls, t)
        return module.new(t)
    end,
})
return module