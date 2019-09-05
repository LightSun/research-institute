--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 15:16
-- To change this template use File | Settings | File Templates.
--

----------------------------------------------------------------

local list = {}
local meta = {
    __call = function (tab, ...)
        local p = {...}
        for i = 1 , #p do
            print("arg"..i.." = "..tostring(p[i]))
        end
    end
}
setmetatable(list, meta)

local a = list(1, 2, 3)

---------------------