--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 16:40
-- To change this template use File | Settings | File Templates.
--

-- 元类
Rect = {area = 0, length = 0, width = 0 }

--
function Rect:new(o, length, width)
    o = o or {}
    length = length or 0;
    width = width or 0;
    setmetatable(o, self)

    self.__index = self;
    self.length = length;
    self.breadth = width;
    self.area = length * width;
    return o;
end

-- 给Rect添加方法。 这个跟c++很像
function Rect:printArea()
    print(tostring(self))
    print("Rect 矩形面积为: ",self.area)
end

r = Rect:new(nil, 10, 20)
print(r.length)
-- 调用方法需要用:
r:printArea()


----------------------------------------
-- 继承
Rect2 = Rect:new();
function Rect2:new(o, length, width)
    o = o or Rect:new(o, length, width);
    setmetatable(o, self)
    self.__index = self;
    self.aa = 123;
    return o;
end

-- 重写函数
function Rect2:printArea()
    print("Rect2 矩形面积为: ",self.area)
end

r = Rect2:new(nil, 10, 20)
print(r.aa)
r:printArea()


