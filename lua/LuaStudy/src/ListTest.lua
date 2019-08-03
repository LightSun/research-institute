
local dir = "src/?.lua";
package.path = dir..";"..package.path

local List = require("List")

local a = List(1, 2)

print(a)