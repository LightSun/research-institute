
-- use table like set. but. size is not determinate. must loop all elements
-- table 传递是引用传递

local items = {}

-- add some items to the set
items["foo"] = true
items[123] = true

-- is "foo" in the set?
if items["foo"] then
    -- do stuff
    print("foo in the set")
end

-- print(#items) --wrong
-- remove item from the set
items[123] = nil

