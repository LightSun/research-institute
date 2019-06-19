--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 16:35
-- To change this template use File | Settings | File Templates.
--
-- 逻辑符号. and, or, not 不仅仅用于逻辑操作符。

-- 私有静态变量
local a = 1;
local b = 2;
local c = 3;

-- and or
if( d and a) then
    print("c and a")
else if( a or b ) then
    print("a or b")
else
    print("0")
end
end

-- not

if( not d) then
    print("not d")
end