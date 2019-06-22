--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/22
-- Time: 下午 15:51
-- To change this template use File | Settings | File Templates.
--

local tA = {}
local mt = {}
getmetatable(tA, mt)
mt.__metatable = "lock"
setmetatable(tA, mt)
print(getmetatable(tA))  -->lock