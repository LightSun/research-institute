
local m = {}

function m.new()
    local self = {}

    function self.keys()
        return "func_keys"
    end
    return self
end

local var = m.new()
var["keys"]="a"

-- no function now.
print(var.keys())

return m