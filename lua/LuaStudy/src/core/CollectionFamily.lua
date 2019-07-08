
require("src.core.init")
local obj    = require("Object")

--- heaven7
local m = {}
m.COLLECTION_TYPE_LIST = 1
m.COLLECTION_TYPE_SET  = 2
m.COLLECTION_TYPE_MAP  = 3

function m.new(type, typeName, func_create , ...)
    local self = obj.new(typeName , func_create , ...);

    local t = type;

    function self.size()
        return 0;
    end

    function self.recomputeSize()
    end

    function self.getCollectionType()
        return t
    end

    function self.getDataModule()
        return self;
    end

    return self;
end

return m