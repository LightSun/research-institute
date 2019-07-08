---heaven7

require("src.core.init")

local it    = require("Iterator")
local CF    = require("CollectionFamily")

local module = {}

function module.new(tt, typeName , func_create , ...)
    local self = CF.new(tt, typeName , func_create , ...);
    local size = 0;

    function self.size()
        return size
    end

    function self.add(e)
        return nil;
    end
    function self.remove(e)
        return nil;
    end

    function self.contains(e)
        return nil;
    end

    function self.addAll(collection)
    end

    function self.containsAll(collection)
        return nil
    end

    function self.clear()
    end

    function self.iterator()
        return nil;
    end

    function self.copy()
        return nil
    end

    function self.toList()
        return self
    end

    function self.toSet()
        return self
    end

    function self.recomputeSize()
        local s = 0
        local ita = self.iterator();
        if not ita or ita.getType() ~= it.TYPE then
            error("must be iterator.")
        end
        while ita.hasNext() do
            local val = ita.next();
            if(type(val) ~= "function") then
                s = s + 1;
            end
        end
        size = s;
        return s;
    end
    -- protect methods ---

    function self.setSize(s)
        size = s
    end
    function self.addSize(delta)
        size = size + delta;
    end

    return self;
end

return module;