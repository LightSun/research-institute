--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 上午 11:37
-- To change this template use File | Settings | File Templates.
--

-- pcall 用于捕获函数调用的结果以及异常

local status, err = pcall(function () error({code=121}) end)
print(err.code)  -->  121
print(status)  -- false. 代表失败
