--[[--
--It's also possible to make objects using closures.
-- Instances are slower to create and use more memory,
 - - but there are also some advantages (like faster instance field access)
--]]

local function MyClass(init)
    -- the new instance
    local self = {
        -- public fields go in the instance table
        public_field = 0
    }

    -- private fields are implemented using locals
    -- they are faster than table access, and are truly private, so the code that uses your class can't get them
    local private_field = init

    function self.foo()
        return self.public_field + private_field
    end

    function self.bar()
        private_field = private_field + 1
    end

    -- return the instance
    return self
end

local i = MyClass(5)
print(i.foo()) --> 5
i.public_field = 3
i.bar()
print(i.foo()) --> 9

