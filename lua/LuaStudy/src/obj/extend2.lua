--[[--
--用function的方式实现继承.
--
-- table方式优势：
-       1, 创建基类对象比较快， 所有方法共享给对象
        2，使用table将会消耗更少的内存。
        3, 对于方法调用更加符合绝大多数面向对象的Lua代码
-
 closure方式优势：
       1, 可以有私有field. 访问私有field很快. (since they're upvalues, not table lookups.)
       2, 方法调用更快，因为他们不用 __index元方法
       3, .的方式更适合习惯了其他语言的开发者。
-
--]]

local function BaseClass(init)
    local self = {}

    local private_field = init

    function self.foo()
        return private_field
    end

    function self.bar()
        private_field = private_field + 1
    end

    function self.increase(var)
        private_field = private_field + var;
    end

    function self.getField()
        return private_field;
    end
    -- return the instance
    return self
end

local function DerivedClass(init, init2)
    local self = BaseClass(init)

    self.public_field = init2

    -- this is independent from the base class's private field that has the same name
    local private_field = init2

    -- save the base version of foo for use in the derived version
    local base_foo = self.foo
    function self.foo()
        return private_field + self.public_field + base_foo()
    end

    local base_increase = self.increase
    function self.increase(var)
        base_increase(var + 1)
    end

    -- return the instance
    return self
end

local i = DerivedClass(1, 2)
print(i.foo()) --> 5
i.bar()
print(i.foo()) --> 6

i.increase(7);
print(i.getField())

