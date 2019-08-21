
---  coroutine.yield可以传任意对象给 coroutine.resume 的结果。
---而 coroutine.resume可以传任意对象给 coroutine.yield 的结果。

local function foo()
    print("foo" , 1)
    local val = coroutine.yield(1)  -- yield会使得线程挂起， 可以传递任意参数
    print("yield result" , val)
    print("foo" , 2)
end

-- co 是thread. 线程的结束状态由 创建传递的函数foo 执行完成时。
-- 创建thread时。线程默认suspended 状态
local co = coroutine.create(foo)
print(coroutine.status(co)) -- suspended

-- value 由 yield传递
local errorfree , value = coroutine.resume(co , 5)
print(errorfree , value)
errorfree , value = coroutine.resume(co , 5)
print(errorfree , value)

print(coroutine.status(co))