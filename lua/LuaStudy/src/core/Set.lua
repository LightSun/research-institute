--- heaven7
local dir = "src/core/?.lua";
package.path = dir..";"..package.path

local utils = require("TableUtils")
local it = require("Iterator")
local coll = require("Collection")

local module = {};

function module.new(set)
    if(set and type(set) ~= "table") then
        error("must be table set")
    end

    local function create(...)
        return utils.getAt(0, ...)
    end
    local self = coll.new("Set", create, set)

    function self.add(e)
        local old = self[e]
        self[e] = true
        -- pre not exist
        if(not old) then
            self.addSize(1)
            return true
        end
        return nil
    end

    function self.remove(e)
        local old = self[e]
        self[e] = nil
        -- pre exist
        if(old) then
            self.addSize(-1)
            return true
        end
        return nil
    end

    function self.contains(e)
        return self[e] ~= nil
    end

    function self.addAll(collection)
        local state, code = pcall(collection.getType)
        local handled
        if(state) then
            if(code == "List") then
                -- todo
                handled = true;
            else if(code == "Set") then
                handled = true;
            end
            end
        else
            -- default.
        end

        if(not handled) then

        end
    end

    return self;
end

return module;

