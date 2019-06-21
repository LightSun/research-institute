--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 15:16
-- To change this template use File | Settings | File Templates.
--

--元表  metatable
local myTabel={'Lua','Java','C','C++'}
local mymetatable={
    --当把表当做方法来调用的时候，会执行__call对应的方法
    __call = function (tab, arg)
        print(arg)
        return '调用了__call'
    end
}
local newTabel={'Python','PHP','JavaScript'}
myTabel = setmetatable(myTabel, mymetatable)--设置这个表的元表

local v = myTabel(55)
print(v)
---------------------