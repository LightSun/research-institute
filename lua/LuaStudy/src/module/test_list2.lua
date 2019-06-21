--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 16:28
-- To change this template use File | Settings | File Templates.
--

local dir = "src/module/?.lua";
package.path = dir .. ";" .. package.path
local List = require("listImpl2")
local utils = require("TableUtils")

local list = List();
list:add(1)
list:add(2)
list:add(3)
utils.logTable(list)



