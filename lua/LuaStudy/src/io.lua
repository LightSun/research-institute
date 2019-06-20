--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 17:49
-- To change this template use File | Settings | File Templates.
--


--[[
file = io.open (filename [, mode])
mode:
r, 读
w,  写
a,  append的含义
 r+, 可读写打开，文件必须存在
  w+ , 可读写，存在则清空。不存在则创建
  a+, 可读可写
   b, 二进制模式
    + , 读和写

io.read(...)
"*all"	reads the whole file
"*line"	reads the next line
"*number"	reads a number
num	reads a string with up to num characters 读取指定长度

--]]


wrongFile = io.open("xxx.lua", "r+");
-- null if error
if wrongFile then
    -- iuput方法参数可以为filename，file*
    local input2 = io.input(wrongFile)
    print("wrongFile >>> input result code: ", input2);
    wrongFile:close()
    io.close(input2);
end

-- 以只读方式打开文件
file = io.open("src/array.lua", "r")
print(type(file)) -- userdata
-- input default is file . if error .return error.
input = io.input(file)
print("input result code: ", input);

-- *all 读取所有内容 *number读取数字
t = io.read("*all")
print(t)
io.close(input)


-- 假设有一行内容:  6.0       -3.23     15e12
-- io.read("*number", "*number", "*number") 直接读取多个
