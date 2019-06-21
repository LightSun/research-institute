--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 上午 11:35
-- To change this template use File | Settings | File Templates.
--

function Object.prototyp:print()
    print(self);
end

function Object:createObject(name)
    local instance = self();
    instance.name = name;
    return instance;
end