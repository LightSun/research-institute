require("src.core.init")

local List = require("List")
local utils = require("TableUtils")

local list = List.new();
list.add(1)
list.add(2)
list.add(3)

assert(list.get(0) == 1)
assert(list.get(1) == 2)
assert(list.get(2) == 3)


print("-------- start copy ------")
local copyList = list.copy();
assert(list == copyList)
utils.logArray(copyList)
assert(copyList.size() == 3)
assert(copyList.get(0) == 1)
assert(copyList.get(1) == 2)
assert(copyList.get(2) == 3)

-- 2, 3
assert(copyList.removeAt(0))
assert(copyList.size() == 2)
assert(copyList.get(0) == 2)
assert(copyList.get(1) == 3)

-- 10, 3
copyList.set(0, 10);
assert(copyList.get(0) == 10)

-- 10, 888, 3
copyList.add2(1, 888)
assert(copyList ~= list)
assert(copyList.size() == 3)
assert(copyList.get(0) == 10)
assert(copyList.get(1) == 888)
assert(copyList.get(2) == 3)

-- 10, 888, 3, 8, 9
copyList.addMulti(8,9);
assert(copyList.size() == 5)
assert(copyList.get(3) == 8)
assert(copyList.get(4) == 9)

-- 1, 2, 3
print("-------- start iterator() ------")
local ita = list.iterator();
while(ita.hasNext()) do
    print(ita.next())
    end

-- test meta
local list2 = list +  List.new({4,5})
assert(list2.size() == 5)
assert(list2.get(0) == 1)
assert(list2.get(1) == 2)
assert(list2.get(2) == 3)
assert(list2.get(3) == 4)
assert(list2.get(4) == 5)

local state, result = pcall(list2.size)
print(type(result))