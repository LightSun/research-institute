--- heaven7
---
require("src.core.init")
local M = require("MethodRecord")

local m = {}
_G.methodTables = M.new()
local mt = _G.methodTables;

function m.register(t, name, pname)
    mt.setParent(name, pname)
    local meta = {
        __index = function(tab, key)
            if(type(key) == "function") then
                if(tostring(key) == "define") then
                    local function reg()

                    end
                    return reg
                end
            end
        end,
    }
end
-- a = {}
-- register(a)
-- a.define("a", "b", function(a, b));

return m