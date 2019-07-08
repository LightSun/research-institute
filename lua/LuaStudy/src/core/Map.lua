-- heaven7
require("src.core.init")

local utils = require("TableUtils")
local obj = require("Object")

local module = {}

--map, filter,sum,avg,
function module.new(map)

    local self = obj.new1("Map", map)
    local size = 0;

    function self.size()
        return size;
    end

    function self.put(key, value)
        local old = self[key]
        self[key] = value;
        if not old then
            size = size + 1;
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

    function self.containsValue(key)
        return self[key] ~= nil
    end

    function self.remove(key)
        local val = self[key];
        self[key] = nil
        if val then
            size = size - 1
        end
        return val;
    end

    function self.map(mapFunc)
        --todo wait
        utils.travelTable(self, mapFunc);
    end


    return self;
end

return module;
