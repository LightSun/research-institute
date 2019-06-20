--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 下午 17:56
-- To change this template use File | Settings | File Templates.
--
local Iterator = {};

function Iterator.new()
    local self = {}

    function self.hasNext()
        return false;
    end

    function self.next()
    end

    return self;
end

local tab = {1,2,3};
function Iterator.ListIterator()
    local self = Iterator.new()
    self.__index = self;
    self.index = -1;
    self.size = #tab;
    function self.hasNext()
        return self.index + 1 < self.size;
    end

    function self.next()
        self.index = self.index + 1
        return tab[self.index + 1]
    end
    return self;
end

--[[local it = Iterator.ListIterator()
while(it.hasNext()) do
    print(it.next())
    end]]


return Iterator;