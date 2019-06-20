--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 上午 10:23
-- To change this template use File | Settings | File Templates.
--

file="src/note.txt"

local BUFSIZE = 2^13     -- 8K
local f = io.input(file)   -- open input file
local cc, lc, wc = 0, 0, 0   -- char, line, and word counts
while true do
    local lines, rest = f:read(BUFSIZE, "*line")
    print(lines, " >>> ", rest)
    if not lines then break
    end
    if rest then lines = lines .. rest .. '\n'
    end
    cc = cc + string.len(lines)
    -- count words in the chunk
    local _,t = string.gsub(lines, "%S+", "")
    wc = wc + t
    -- count newlines in the chunk
    _,t = string.gsub(lines, "\n", "\n")
    lc = lc + t
end
print(lc, wc, cc)


local f = io.input("src/numbers.txt")
while true do
    -- 读取3个数字
    local rec1, rec2, rec3 = f:read("*number" , "*number", "*number");
    if( not rec1) then break end;
    print(rec1, rec2, rec3)
end


