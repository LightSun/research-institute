-- heaven7
require("src.core.init")

local utils = require("TableUtils")
local List = require("List")
local Set = require("Set")
local Entry = require("Entry")
local CF = require("CollectionFamily")

local module = {}

--map, filter,sum,avg,
function module.new(map)

    local function create(...)
        return utils.getAt(0, ...)
    end

    local self = CF.new(CF.COLLECTION_TYPE_MAP, "Map", create, map)
    local SIZE = 0;

    function self.size()
        return SIZE;
    end

    function self.put(key, value)
        if(not value) then
            return nil;
        end
        local old = self[key]
        self[key] = value;
        if not old then
            SIZE = SIZE + 1;
        end
        return old;
    end

    function self.get(key)
        return self[key];
    end

    function self.isEmpty()
        return self.size() == 0
    end

    function self.containsKey(key)
        return self[key] ~= nil
    end

    function self.containsValue(value)
        local result;
        local function traveller(index, val)
            if(val == value) then
                result = true;
                return true;
            end
        end
        utils.travelTable(self, traveller)
        return result
    end

    function self.remove(key)
        local val = self[key];
        self[key] = nil
        if val then
            SIZE = SIZE - 1
        end
        return val;
    end

    function self.putAll(map2)
        local function traveller(index, val)
            self.put(index, val)
        end
        utils.travelTable(map2, traveller)
    end

    function self.clear()
        local function traveller(index, val)
            self[index] = nil;
        end
        utils.travelTable(self, traveller)
        SIZE = 0
    end
    function self.keySet()
        local l = {}
        local function traveller(index, val)
            table.insert(l, #l + 1, index)
        end
        utils.travelTable(self, traveller)
        return Set.new(l)
    end
    function self.values()
        local l = {}
        local function traveller(index, val)
            table.insert(l, #l + 1, val)
        end
        utils.travelTable(self, traveller)
        return List.new(l)
    end

    function self.entrySet()
        local l = {}
        local function traveller(index, val)
            table.insert(l, #l + 1, Entry.new(index, val))
        end
        utils.travelTable(self, traveller)
        return Set.new(l)
    end

    function self.equals(map2)
        local m2
        local state, code = pcall(map2.getType)
        if(state and code == "Map") then
            m2 = map2
        else
            m2 = module.new(map2);
        end
        -- first check size
        if(self.size() ~= m2.size()) then
            return nil;
        end
        -- travel
        local result = true;
        local function traveller(index, val)
            if(val ~= m2.get(index)) then
                result = nil;
                return true
            end
        end
        utils.travelTable(self, traveller)
        return result;
    end

    function self.copy()
        local l = {}
        local function traveller(index, val)
             l[index] = val;
        end
        utils.travelTable(self, traveller)
        return module.new(l)
    end

    function self.recomputeSize()
        SIZE = self.keySet().size()
    end

    self.recomputeSize()

    -- meta
    local meta = {
        __eq = function(t1, t2)
            local state, _ = pcall(t1.equals, t2)
            return state;
        end
    ,__add = function(t1, t2)
            local state, _ = pcall(t1.putAll, t2)
            if(state) then
                return t1
            end
            return nil;
        end
    }
    setmetatable(self, meta)
    return self;
end

return module;
