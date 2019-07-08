
require("src.core.init")
local Map = require("Map")


local map = Map.new();
map.put("a", 1)
assert(map.size() == 1)

map.put("b", 2)
map.put("c", 3)
assert(map.get("a") == 1)
assert(map.get("b") == 2)
assert(map.containsKey("b"))
assert(map.containsKey("a"))
assert(map.containsKey("c"))
assert(map.containsValue(1))
assert(map.containsValue(2))
assert(map.containsValue(3))

assert(map.remove("b") == 2)
assert(map.size() == 2)
-- "a" = 1, "c" = 3

local t = {["g"] = 9, ["k"] = 13}
local map2 = Map.new(t);
map2.putAll(map)
assert(map2.size() == 4)
assert(map2.keySet().size() == 4)
assert(map2.values().size() == 4)
assert(map2.entrySet().size() == 4)

map2 = map.copy();
assert(map2.size() == 2)
assert(map2.equals(map))
assert( Map.new(t).equals(t))

map2.clear()
assert(map2.size() == 0)

-- test meta
assert( map.copy() == map)
map2 = Map.new(t);
map2 = map2 + map
assert(map2.size() == 4)
assert(map2.keySet().size() == 4)
assert(map2.values().size() == 4)
assert(map2.entrySet().size() == 4)