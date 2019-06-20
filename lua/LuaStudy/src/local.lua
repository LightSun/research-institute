--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 上午 9:43
-- To change this template use File | Settings | File Templates.
--
local function test1()
    print("hello test1")
end

-- 本地的函数必须声明在前
function test()
    test2()
    test1()
end

function test2()
    print("hello test2")
end

test()

