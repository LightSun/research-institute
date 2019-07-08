--- heaven7
local utils = require("TableUtils")
local CF    = require("CollectionFamily")
local List  = require("List")
local Map   = require("Map")
local Set   = require("Set")
local m     = {}

function m.new(table)

    local TAB  = table
    local self = {}

    --- map the 'Collection' to normal list.
    --- @param func: the function to transform.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: a new list
    function self.map(func, context, func_break)
        local li = List.new()
        local function traveller(index , value)
            local result = func(context , index , value)
            if (result) then
                li.add(result)
            end
            if(func_break and type(func_break) == "function" and func_break(context , index , value)) then
                return true
            end
        end
        utils.travelTable(TAB , traveller)
        return li
    end

    --- map the 'Collection' to normal Set.
    --- @param func: the function to transform.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: a new Set
    function self.mapSet(func, context , func_break)
        return Set.new(self.map(func, context , func_break))
    end

    --- filter the 'Collection'.
    --- @param func: the function to transform.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: the raw type of table. List|Set|Map
    function self.filter(func, context , func_break)
        local li = {}
        local function traveller(index , value)
            local result = func(context , index , value)
            if (result) then
                li[index] = value;
            end
            if(func_break and type(func_break) == "function" and func_break(context , index , value)) then
                return true
            end
        end
        utils.travelTable(TAB , traveller)

        -- if list return list . if set return set . if map return map
        if (TAB.getCollectionType() == CF.COLLECTION_TYPE_LIST) then
            return List.new(li)
        end
        if (TAB.getCollectionType() == CF.COLLECTION_TYPE_SET) then
            return Set.new(li)
        end
        if (TAB.getCollectionType() == CF.COLLECTION_TYPE_MAP) then
            return Map.new(li)
        end
        return List.new(li)
    end

    --- pile the 'Collection' to another result.
    --- @param func: the function to transform.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: the pile result
    function self.pile(func, context , func_break)
        local r
        local function traveller(index , value)
            -- the type of result must be the same
            local result = func(context , index , value)
            if (result) then
                if (type(result) == "string") then
                    if (not r) then
                        r = result;
                    else
                        r = r .. result
                    end
                else
                    if (type(result) == "boolean") then
                        if (not r) then
                            r = result;
                        else
                            if (r == true and result == true) then
                                r = true;
                            else
                                r = false;
                            end
                        end
                    else
                        if (not r) then
                            r = result;
                        else
                            -- self object should overroad +
                            r = r + result
                        end
                    end
                end
            end
            if(func_break and type(func_break) == "function" and func_break(context , index , value)) then
                return true
            end
        end
        utils.travelTable(TAB , traveller)
        --
        if (type(r) == "boolean" and r == false) then
            return nil
        end
        return r
    end

    --- get the sum of the 'Collection'. same as pile.
    --- @param func: the function to transform.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: the pile result
    function self.sum(func, context , func_break)
        return self.pile(func, context , func_break)
    end

    --- get the average of the 'Collection'.
    --- @param func: the function to transform.
    --- @param func_pileMap: the function to map the pile result to the actually number.
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: the average
    function self.average(func, func_pileMap, context, func_break)
        local pl = self.pile(func, context, func_break);
        if(func_pileMap) then
            pl = func_pileMap(context, pl)
        end
        return pl / TAB.size()
    end

    --- map the 'Collection' to map
    --- @param key_func: the function to map result key
    --- @param value_func: the function to map result value
    --- @param context: the common parameter as context.
    --- @param func_break: the function to judge whether to break or not.
    --- @return: a new Map
    function self.mapMap(key_func, value_func, context, func_break)
        local li = Map.new()
        local function traveller(index , value)
            local k = key_func(context , index , value)
            local v = value_func(context , index , value)
            if (k) then
                li.put(k , v)
            end
            if(func_break and type(func_break) == "function" and func_break(context , index , value)) then
                return true
            end
        end
        utils.travelTable(TAB , traveller)
        return li
    end

    return self
end

return m;