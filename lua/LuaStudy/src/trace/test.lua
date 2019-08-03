---
local dir = "src/trace/?.lua";
package.path = dir..";"..package.path

local trace = require "trace"

local function factorial(n)
    if n <= 1 then
        return 1
    end
    return factorial(n-1) * n
end

local function foo(n)
    trace.trace("n s t.k1.k2",n)
    local s =  factorial(100)
    local t = {k1={k2="hello"}}
    t.k1.k2 = "world"
    return s
end

local function hello()
    print "hello"
end

foo(3)
--[[hello()
foo()]]
