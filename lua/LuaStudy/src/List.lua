
local m = {}

local meta = {
    __call = function(tab, ...)
        local args = {...}
       local self = {}
--[[        for i = 1 , #args do
            table.insert(self, #self + 1, args)
        end ]]
        self.args = args;
        return self;
    end
}
setmetatable(m, meta)



return m;