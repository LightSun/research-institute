--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 14:05
-- To change this template use File | Settings | File Templates.
--

local MyClass = {}
MyClass.__index = MyClass

-- 设置元方法. 使得  local instance = MyClass(5) 不用.new了
-- cls 代表当前的table
setmetatable(MyClass, {
    __call = function (cls, ...)
        --local var = {...};
        --print(cls, var)
        return cls.new(...)
    end,
})

function MyClass.new(init)
    local self = setmetatable({}, MyClass)
    self.value = init
    return self
end

-- the : syntax here causes a "self" arg to be implicitly added before any other args
--隐式添加self
function MyClass:set_value(newval)
    self.value = newval
end

function MyClass:get_value()
    return self.value
end

function MyClass:getA()
    return self:get_value()
end

local instance = MyClass(5)
local instance2 = MyClass(3)
-- do stuff with instance...
print(instance)
print(instance2)

print(instance:get_value())
print(instance2:get_value())
print(instance:getA())

-- -------------------------------
