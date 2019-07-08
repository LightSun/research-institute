require("src.core.init")

local O      = require("Object")
local utils      = require("TableUtils")

function create(list)

    local function create(...)
        local p = {...}
        return p[1];
    end
    local self = O.new("List" , create, list)

    return self;
end

local t    = { "a","b" }
local list = create(t)
assert(list.getType() == "List")
assert(list == t)
utils.logArray(list)
