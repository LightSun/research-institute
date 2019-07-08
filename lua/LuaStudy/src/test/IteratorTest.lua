local dir      = "src/core/?.lua";
package.path   = dir .. ";" .. package.path

local Iterator = require("Iterator")

local tab      = { 1,2,3 };
function Iterator.ListIterator()
    local self   = Iterator.new()
    self.__index = self;
    self.index   = -1;
    self.size    = #tab;
    function self.hasNext()
        return self.index + 1 < self.size;
    end

    function self.next()
        self.index = self.index + 1
        return tab[self.index + 1]
    end
    return self;
end

local it = Iterator.ListIterator()
while (it.hasNext()) do
    print(it.next())
end