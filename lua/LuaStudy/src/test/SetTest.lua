--- heaven7
require("src.core.init")
local Set = require("Set")

local set = Set.new({1, 2, 3});

print(set.size())
assert(set.size() == 3);
assert(not set.add(1));
assert(set.size() == 3);
assert(set.getType() == "Set")


assert(not set.remove(5))
assert(set.remove(1))
-- 2, 3
assert(set.size() == 2)
assert(set.contains(2))
assert(set.contains(3))

local set2 = set.copy()
assert(set2.addAll({4,5,6}))
assert(set2.size() == 5)
assert(set2.contains(4))
assert(set2.contains(5))
assert(set2.contains(6))


set2 = set.copy()
assert(set2.addAll( Set.new({4,5,6})))
assert(set2.size() == 5)
assert(set2.contains(4))
assert(set2.contains(5))
assert(set2.contains(6))

set2 = set.copy()
local nset = {[4] = true, [5] = true, [6] = true}
assert(set2.addAll(Set.newFromSet(nset)))
assert(set2.size() == 5)
assert(set2.contains(4))
assert(set2.contains(5))
assert(set2.contains(6))

assert(set2.containsAll(Set.new({2, 3})))
assert(not Set.new({2, 3, 5}).containsAll(set2))
set2.clear()
assert(set2.size() == 0)

set2 = set.copy()
assert(set2.equals(set))
assert(set2 == set)


set2 = set.copy()
set2 = set2 + Set.new({4,5,6})
assert(set2.size() == 5)
assert(set2.contains(4))
assert(set2.contains(5))
assert(set2.contains(6))