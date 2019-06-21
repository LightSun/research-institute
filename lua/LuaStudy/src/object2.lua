--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 上午 11:35
-- To change this template use File | Settings | File Templates.
--

-- lua 引入prototyp, 每个导出的类型都带有这个属性，其包含了类型的实例方法和属性定义。
--如果需要扩展某个类型的实例方法和属性，则可通过修改prototype来实现
function Object.prototyp:print()
    print(self);
end

function Object:createObject(name)
    local instance = self();
    instance.name = name;
    return instance;
end
function Object.prototype:init ()
    print ("object created!");
end

function Object.prototype:destroy()
    print ("object destroy!");
end

Object:subclass("MyClass");

-- 重写类型的初始化方法
function MyClass:init ()
    print("MyClass instance initialize...");
end

-- 定义类型属性
MyClass.prototype.propertyName = 0;

local instance = MyClass();
print (instance.propertyName);