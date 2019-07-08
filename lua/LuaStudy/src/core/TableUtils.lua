local dir    = "src/core/?.lua";
package.path = dir .. ";" .. package.path

local module ={}

module.MAP_TYPE_KEY   = 1;
module.MAP_TYPE_VALUE = 2;
module.MAP_TYPE_PAIR  = 3;
module.MERGE_INCLUDE_METHODS       = 1;
module.MERGE_INCLUDE_OBJECT_MEMBER = 2;
module.MERGE_INCLUDE_TABLE         = 4;

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
        --elseif type(value) == "function" then
        else
            func(index, value)
        end
    end
end

---map table to another table.
---@param tab
---@param context
---@param func:the func with one or two func
---@param mapType: the map type. see module.MAP_TYPE_KEY, MAP_TYPE_VALUE, MAP_TYPE_PAIR
---@return :mapped table
function module.mapTable(tab, context, func, mapType)
    if not tab then
        return nil
    end
    -- not table
    if(type(tab) ~= "table") then
        error("tab is not table")
    end

    local resultMap = {}
    -- prepare switch
    local switch = {}
    switch[module.MAP_TYPE_KEY] = function(cx, key, value, f)
        local newKey = f(cx, key, value);
        if not newKey then
            error("new key can't be nil");
        end
        resultMap[newKey] = value;
    end
    switch[module.MAP_TYPE_VALUE] = function(cx, key, value, f)
        local newValue = f(cx, key, value);
        resultMap[key] = newValue;
    end
    switch[module.MAP_TYPE_PAIR] = function(cx, key, value, f)
        if(type(f) ~= "table") then
            error("for map pair of key-value. f must be two functions. [1] is key-func, [2] is value-func");
        end
        local newKey = f[1](cx, key, value);
        if not newKey then
            error("new key can't be nil");
        end
        local newValue = f[2](cx, key, value);
        resultMap[newKey] = newValue;
    end

    for key, value in pairs(tab) do
            switch[mapType](context, key, value, func)
    end
    return resultMap;
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
        if(type(value) == "function") then
            return
        end
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
    if arr then
        for i = 1, #arr do
            if(i ~= 1) then
                str = str..", "
            end
            str = str..arr[i];
        end
    end
    str = str.." }"
    log_func(str);
end

function module.mapArray(arr, context, func)
    if not arr then
        return nil
    end
    local newArr = {}
    if arr then
        for i = 1, #arr do
            newArr[i] = func(context, arr[i])
        end
    end
    return newArr;
end

function module.logArray(arr)
    module._logArray(arr, print)
end

--- merge tables like map ---
function module.mergeMap(receiver, ...)
    local result = receiver or {}
    local tabs = {...}
    if not tabs then
        return result
    end
    for i = 1, #tabs do
        if tabs[i] then
            for k, v in pairs(tabs[i]) do
                result[k] = v;
            end
        end
    end
    return result
end

function module.mergeArray(receiver, ...)
    local result = receiver or {}
    local tabs = {...}
    if not tabs then
        return receiver
    end
    for i = 1, #tabs do
        if tabs[i] then
            for k, v in pairs(tabs[i]) do
                table.insert(result, v);
            end
        end
    end
    return result
end

function module.mergeArrayWithFlags(receiver, flags, ...)
    local result = receiver or {}
    local tabs = {...}
    if not tabs then
        return receiver
    end
    for i = 1, #tabs do
        if tabs[i] then
            for k, v in pairs(tabs[i]) do
                local t = type(v);
                if(t == "function" and (flags & module.MERGE_INCLUDE_METHODS == module.MERGE_INCLUDE_METHODS)) then
                    table.insert(result, v);
                else if(t == "table" and (flags & module.MERGE_INCLUDE_TABLE == module.MERGE_INCLUDE_TABLE)) then
                    table.insert(result, v);
                end
                end

                if(k == "__type") then
                    if(flags & module.MERGE_INCLUDE_OBJECT_MEMBER == module.MERGE_INCLUDE_OBJECT_MEMBER) then
                        table.insert(result, v);
                    end
                else
                    table.insert(result, v);
                end
            end
        end
    end
    return result
end

--- judge the two table is equals or not
--- @param tab1: table 1
--- @param tab2: table 2
---@return: true if equals. nil other wise
function module.equals(tab1, tab2)
    if( not tab1 ) then
        if(not tab2) then
            return true
        else
            return nil
        end
    end
    if(not tab2) then
        return nil
    end
    for k, v in pairs(tab1) do
        local t = type(v);
        if(t == "table") then
            if(not tab2[k] or type(tab2[k]) ~= "table" or not module.equals(v, tab2[k])) then
                return nil
            end
        else
            if(t == "function")   then
               -- ignore this.
        else
            if(t == "thread" or t == "userdata")   then
               error("can't compare thread and userdata")
        else
                if(not tab2[k] or v ~= tab2[k])  then
                    return nil
                end
        end
        end
        end

    end
    return true;

end

--- whether the tab1 contains all tab2's elements
function module.containsAll(tab1, tab2)
    if( not tab1 or not tab2) then
        return nil
    end

    for k, v in pairs(tab2) do
        local t = type(v);
        if(t == "table") then
            if(not tab1[k] or type(tab1[k]) ~= "table" or not module.equals(v, tab1[k])) then
                return nil
            end
        else
            if(t == "function")   then
                -- ignore this.
        else
            if(t == "thread" or t == "userdata")   then
                error("can't compare thread and userdata")
        else
            if(not tab1[k] or v ~= tab1[k])  then
                return nil
            end
        end
        end
        end
    end

    return true
end

--- get a table value from variable params which as a table.
---@param index: start from 0.
function module.getAt(index, ...)
    local p = {...}
    return p[index + 1];
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

--[[local tab    = { ["p"] = 3,["v"] = 9 }

local func = function(cx, key, value)
    return tostring(key).."__"..tostring(value)
end
local resultTab = module.mapTable(tab , nil , func, module.MAP_TYPE_KEY)

module.logTable(resultTab)]]

return module