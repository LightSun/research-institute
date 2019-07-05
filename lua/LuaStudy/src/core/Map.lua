-- heaven7
local dir = "src/core/?.lua";
package.path = dir..";"..package.path

local utils = require("TableUtils")

local module = {}

--map, filter,sum,avg,
function module.new(map)
    local self = map or {};

    function self.size()
        return #self;
    end

    function self.map(mapFunc)
        utils.travelTable(self, mapFunc);
    end


    return self;
end

return module;
