--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/19
-- Time: 下午 16:19
-- To change this template use File | Settings | File Templates.
--

-- 排序迭代器
function order_pairs(tbl)
    local names_buffer = {}
    for name, _ in pairs(tbl) do
        table.insert(names_buffer, name)
    end
    table.sort(names_buffer, function(a, b) return tostring(a) < tostring(b) end)

    local table_index = 0
    local function iterator()
        table_index = table_index + 1
        local key = names_buffer[table_index]
        return key, tbl[key]
    end

    return iterator
end

local function print_table(tbl)
    for k, v in pairs(tbl) do
        print(k .. " = " .. v)
    end
    print "========"
end

local function print_table_order(tbl)
    for k, v in order_pairs(tbl) do
        print(k .. " = " .. v)
    end
    print "========"
end

local t = { [4] = "test", "order", b = "pairs", [2] = "another", a = "string" }

print_table(t)

print_table_order(t)

