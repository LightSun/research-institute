--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 下午 14:10
-- To change this template use File | Settings | File Templates.
--

local module ={}

-- module.NOT_TABLE = 1;
function module.travelTable(tab, func)
    if not tab then
        return nil
    end
    -- not table
    if(type(tab) ~= "table") then
        error("tab is not table")
    end

    for index, value in pairs(tab) do
        if(type(value) == "table") then
            module.travelTable(value, func)
        else
            func(index, value)
        end
    end
end

function module.copyTable(tab)
    local new_table = {}
    module.travelTable(tab,
        function(index, value)
            if(type(value) == "table") then
                new_table[index] = module.copyTable(value)
            else
                new_table[index] = value;
            end
        end
        )
    return new_table
end

function module._logTable(tab, log_func)
    local str = "{ "
    local i = 1;
    local function travel(key, value)
        print("key-value: ",key, value)
        if(i ~= 1) then
            str = str..", "
        end
        i = i + 1
        str = str..key.."="..value;
    end
    module.travelTable(tab, travel)
    str = str.." }"
    log_func(str);
end

function module.logTable(tab)
    module._logTable(tab, print)
end

-- lua 不支持方法重载
function module._logArray(arr, log_func)
    local str = "{ "
    for i = 1, #arr do
        if(i ~= 1) then
            str = str..", "
            end
        str = str..arr[i];
    end
    str = str.." }"
    log_func(str);
end

function module.logArray(arr)
    module._logArray(arr, print)
end

--[[local testA =  {1,2,3,4,5 }
logArray(testA)

local testB = testA
testB[2] = "22"

local testC = copyTable(testA)
testC[2] = "222"

for k , v  in ipairs (testA) do
    print("testA",k, v)
end

print("============================")

for k , v  in ipairs (testC) do
    print("testC",k, v )
end]]

--[[ 这里捕获不了error. why?
-- local a = 1;
local status, err = pcall(copyTable(a))
if(not status) then
    assert(err == module.NOT_TABLE)
    end]]

return module