---

local m = {}


function m.new()
    local self = {}
    self.methods = {}
    self.parents = {}

    function self.setParent(name, pname)
        self.parents[name] = pname
    end
    function self.getParent(name)
        return self.parents[name]
    end
    function self.registerMethod(classname, methodName, methodSig, func)
        local a = self.methods[classname]
        -- not exit
        if not a then
            local pname = self.getParent(classname)
            a = self.methods[pname]
            -- if parent not exist
            if(not a) then
                a = {}
                self.methods[classname] = a
            end
        end
        if(not a[methodName..methodSig]) then
            a[methodName..methodSig] = func
        end
    end

    function self.unregisterMethod(classname, methodName, methodSig)
        local a = self.methods[classname]
        if(a ) then
            a[methodName..methodSig] = nil
        end
    end

    function self.callMethod(classname, methodName, ...)
        local a = self.methods[classname]
        if(not a) then
            local pname = self.getParent(classname)
            a = self.methods[pname]
            if(not a) then
                error("no method for class '"..classname.."' with method '"..methodName.."'")
            end
        end
        local sig = m.getMethodSig(...);
        local fun = a[methodName..sig]
        if(not fun) then
            error(string.format("no method for %s.%s(%s)", classname, methodName, sig))
        end
        return fun(...)
    end
    return self;
end

function m.getMethodSig(...)
    local p = {...}
    local str = ""
    for i = 1 , #p do
        -- support more
        if(i ~= 1) then
            str = str..","
        end
        str = str..type(p[i])
    end
    return str
end

--- test
--[[local mr = m.new();
mr.registerMethod("List" , "size" , m.getMethodSig() ,
          function()
                  return 1
                  end
)

mr.registerMethod("List" , "get" , "number" ,
                  function(n)
                      return n
                  end
)
mr.registerMethod("List" , "test" , "number,number" ,
                  function(a, b)
                      return a + b
                  end
)

local result  = mr.callMethod("List", "size")
print(result)

result  = mr.callMethod("List", "get", 5)
print(result)

result  = mr.callMethod("List", "test", 5, 2)
print(result)]]

------------------------------------
--[[function m.newTest()
    local self = {}
    self.methods = {}
    self.parents = {}

    local meta = {
        __gc = function(tab)
            print("__gc")
        end
    }
    setmetatable(self, meta)
    return self;
end]]

-- local a = m.newTest(); --  有 __gc方法
----------------------------------------------


return m