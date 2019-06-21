--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 下午 18:00
-- To change this template use File | Settings | File Templates.
--

local info = debug.getinfo(1, "S") -- 第二个参数 "S" 表示仅返回 source,short_src等字段， 其他还可以 "n", "f", "I", "L"等 返回不同的字段信息

for k,v in pairs(info) do
    print(k, ":", v)
end

local path = info.source

path = string.sub(path, 2, -1) -- 去掉开头的"@"

path = string.match(path, "^.*/") -- 捕获最后一个 "/" 之前的部分 就是我们最终要的目录部分

print("dir=", path)
--[[local input = io.open(path.."config.txt", "w+")
print(input)

io.close(input)]]

