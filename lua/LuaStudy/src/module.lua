--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 下午 15:26
-- To change this template use File | Settings | File Templates.
--

local loaded = package.loaded
local searchpath = package.searchpath

function import(modname)
    if modname then
        local prefix = modname:match "(.*%.).*$" or (modname .. ".")
        return function(name)
            local fullname = prefix .. name
            local m = loaded[fullname] or loaded[name]
            if m then
                return m
            end
            if searchpath(fullname, package.path) then
                return require(fullname)
            else
                return require(name)
            end
        end
    else
        return require
    end
end

