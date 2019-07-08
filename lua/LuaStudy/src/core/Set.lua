--- heaven7
require("src.core.init")

local utils  = require("TableUtils")
local it     = require("Iterator")
local coll   = require("Collection")
local CF   = require("CollectionFamily")

local module = {};

function module.newFromSet(set)
    local keys = {}
    -- for set.
    local function traveller(index , value)
        table.insert(keys , #keys + 1 , index)
    end
    utils.travelTable(set , traveller)
    return module.new(keys)
end

function module.new(list)
    if (list and type(list) ~= "table") then
        error("must be table set")
    end

    local function create(...)
        local p = { ... }
        if (not p[1]) then
            return nil
        end
        local set = {}
        function traveller(index , value)
            if (type(value) ~= "function") then
                set[value] = true
            end
        end
        utils.travelTable(p[1] , traveller)
        return set;
    end

    local self = coll.new(CF.COLLECTION_TYPE_SET, "Set" , create , list)
    ---- methods ---------------
    function self.add(e)
        local old = self[e]
        self[e]   = true
        -- pre not exist
        if (not old) then
            self.addSize(1)
            return true
        end
        return nil
    end

    function self.remove(e)
        local old = self[e]
        self[e]   = nil
        -- pre exist
        if (old) then
            self.addSize(-1)
            return true
        end
        return nil
    end

    function self.contains(e)
        return self[e] ~= nil
    end

    function self.addAll(collection)
        if (not collection or type(collection) ~= "table") then
            return nil
        end
        local state , code = pcall(collection.getType)
        local handled
        if (state) then
            if (code == "List") then
                local list = collection
                for i = 0 , list.size() - 1 do
                    self.add(list.get(i))
                end
                handled = true;
            else
                if (code == "Set") then
                    local ita = collection.iterator()
                    while (ita.hasNext()) do
                        self.add(ita.next());
                    end
                    handled = true;
                end
            end
        else
            -- default.
        end

        if (not handled) then
            -- collection can be normal array
            local function traveller(index , value)
                self[value] = true
            end
            utils.travelTable(collection , traveller)
            self.recomputeSize()
            handled = true;
        end

        return handled;
    end

    function self.containsAll(collection)
        if (not collection or type(collection) ~= "table") then
            return nil
        end
        local state , code = pcall(collection.getType)
        local handled
        if (state) then
            if (code == "List") then
                local list = collection
                for i = 0 , list.size() - 1 do
                    local v = list.get(i);
                    if (self[v] == nil) then
                        return nil
                    end
                end
                handled = true;
            else
                if (code == "Set") then
                    local ita = collection.iterator()
                    while (ita.hasNext()) do
                        local v = ita.next();
                        if (self[v] == nil) then
                            return nil
                        end
                    end
                    handled = true;
                end
            end
        else
            -- default.
        end

        if (not handled) then
            for i = 1 , #collection do
                local v = collection[i];
                if (type(v) ~= "function") then
                    if (self[v] == nil) then
                        return nil
                    end
                end
            end
            handled = true;
        end

        return handled;
    end

    function self.clear()
        local function traveller(index , value)
            self[index] = nil
        end
        utils.travelTable(self , traveller)
        self.setSize(0)
    end

    function self.toList()
        local keys = {}
        -- for set.
        local function traveller(index , value)
            table.insert(keys , #keys + 1 , index)
        end
        utils.travelTable(self , traveller)
        return keys;
    end

    function self.iterator()
        local keys = self.toList()

        local function listIterator()
            local self   = it.new()
            self.__index = self;
            self.index   = -1;
            self.size    = #keys;
            function self.hasNext()
                return self.index + 1 < self.size;
            end

            function self.next()
                self.index = self.index + 1
                return keys[self.index + 1]
            end
            return self;
        end
        return listIterator();
    end

    function self.copy()
        return module.newFromSet(self)
    end

    function self.equals(other)
        if(not other or type(other) ~= "table") then
            return nil
        end
        -- cast to set
        local oth = other;
        local state, code = pcall(other.getType)
        if(state) then
            if(code == "List") then
                oth = other.toSet()
            end
        else
            oth = module.new(other)
        end

        -- start travel
        local equ = true;
        local function traveller(index, value)
            if(oth[index] == nil) then
                equ = nil;
                return true;
            end
            return nil
        end
        utils.travelTable(self, traveller)
        return equ;
    end

    self.recomputeSize()

    -- meta
    local meta = {
        __eq = function(t1, t2)
            local state, _ = pcall(t1.equals, t2)
            return state;
        end
    ,__add = function(t1, t2)
            if(t1.addAll(t2)) then
                return t1
            end
            return nil
        end
    }
    setmetatable(self, meta)
    return self;
end

return module;

