--
--[[
__add: 对+进行重载

__sub: 对-进行重载

__mul: 对*进行重载

__div: 对/进行重载

__unm: 对相反数进行重载

__mod: 对%进行重载

__pow: 对^进行重载

__concat: 对连接操作符进行重载

__eq: 对==进行重载

__lt: 对<进行重载

__le: 对<=进行重载

__tostring: 类似于C++中对<<的重载 只要做了该重载，在使用print时就会使用对应的函数做处理后再输出
--]]
--
--元表  metatable
local myTabel={'Lua','Java','C','C++'}
local mymetatable={
    --自定义两个表相加，则将其中一个表的内容加入到另一个表的内容中去
    __add=function (tab,newtab)
        local i=0
        for k,v in pairs(tab) do
            if k>i then --获取最大索引号
                i=k
            end
        end
        for k,v in pairs(newtab) do
            i=i+1
            table.insert(tab,i,v)
        end
        return tab
    end
}
local newTabel={'Python','PHP','JavaScript'}
myTabel = setmetatable(myTabel, mymetatable)--设置这个表的元表

local t = myTabel + newTabel

for k,v in pairs(t) do
    print(tostring(k)..':'..tostring(v))
end


