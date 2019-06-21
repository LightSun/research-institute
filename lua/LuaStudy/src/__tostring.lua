--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 15:45
-- To change this template use File | Settings | File Templates.
--

--元表  metatable
local myTabel = {'Lua','Java','C#','C++'}
local mymetatable = {
    --当把表当做字符串输出的时候会调用这个方法，必须要有返回值
    __tostring = function (tab)
        local str =''
        for k,v in pairs(tab) do
            str = str..v..','
        end
        return str
    end,
}
local myTabel = setmetatable(myTabel, mymetatable)--设置这个表的元表
print(myTabel)