--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 16:22
-- To change this template use File | Settings | File Templates.
--

if a or b then
    print(" a or b")
else if c then
    print("c")
else
    print("unknown")
end
end

local a = 1;
while( a < 10)
do
    print(a.."")
    a = a + 1 -- 不支持 ++ +=.
end

-- 从10 变化到1，并且步长为-1
print("------------- 1")
for i = 10, 1, -1 do
    print(i)
end

print("------------- 2")
-- 从2 到 10 include.
for i = 2, 10 do
    print(i)
end

print("------------- 3")
for i = 5, -5, -2 do
    print(i)
    end