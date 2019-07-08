--- heaven7

local m = {}

function m.new(key, value)
    local k = key;
    local v = value;

    local self = {}
    function self.getKey()
        return k;
    end

    function self.getValue()
        return v;
    end
    return self;
end

return m