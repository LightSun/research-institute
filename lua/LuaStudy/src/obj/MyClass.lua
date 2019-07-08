--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 12:30
-- To change this template use File | Settings | File Templates.
--

local MyClass = {} -- the table representing the class, which will double as the metatable for the instances
MyClass.__index = MyClass -- failed table lookups on the instances should fallback to the class table, to get methods

-- syntax equivalent to "MyClass.new = function..."
function MyClass.new(init)
    --  we create the instance (an empty table), give it the metatable
    local self = setmetatable({}, MyClass)
    self.value = init
    self.elements = {}
    return self
end

function MyClass.set_value(self, newval)
    self.value = newval
end

function MyClass.get_value(self)
    -- self.set_value(self, 1)
    return self.value
end

local i = MyClass.new(5)
-- tbl:name(arg) is a shortcut for tbl.name(tbl, arg), except tbl is evaluated only once
print(i:get_value()) --> 5
i:set_value(6)
print(i:get_value()) --> 6

local j = MyClass.new(99)
print(i:get_value())
print(j:get_value())