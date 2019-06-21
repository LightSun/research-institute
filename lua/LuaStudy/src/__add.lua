--
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


