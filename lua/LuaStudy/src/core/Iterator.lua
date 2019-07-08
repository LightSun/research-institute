require("src.core.init")

local obj    = require("Object")

local Iterator = {};
Iterator.TYPE = "Iterator";

function Iterator.new()
    local self = obj.new(Iterator.TYPE, nil)

    function self.hasNext()
        return false;
    end

    function self.next()
    end
    return self;
end

return Iterator;