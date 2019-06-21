--
--

-- clone
function clone( base_object, clone_object )
    if type( base_object ) ~= "table" then
        return clone_object or base_object
    end
    clone_object = clone_object or {}
    clone_object.__index = base_object
    return setmetatable(clone_object, clone_object)
end

-- 是否有继承关系
function isa( clone_object, base_object )
    local clone_object_type = type(clone_object)
    local base_object_type = type(base_object)
    if clone_object_type ~= "table" and base_object_type ~= table then
        return clone_object_type == base_object_type
    end
    local index = clone_object.__index
    local _isa = index == base_object
    while not _isa and index ~= nil do
        index = index.__index
        _isa = index == base_object
    end
    return _isa
end

local object = clone( table, { clone = clone, isa = isa } )

----------------------------------
-- testing "isa"
local foo = object:clone()
local bar = object:clone()
local baz = foo:clone()

print( foo:isa(object) )
print( bar:isa(foo) )
print( baz:isa(foo) )

--[[ output:
true
false
true
]]

--testing prototype delegation

foo = object:clone()
bar = foo:clone()

function foo:speak()
    print(self.thoughts or "foo has no thoughts")
end

bar:speak()

--[[ output:
foo has no thoughts
]]

bar.thoughts = "I may be a clone, but I'm an individual!"
bar:speak()

--[[ output:
I may be a clone, but I'm an individual!
]]
