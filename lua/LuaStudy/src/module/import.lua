--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/21
-- Time: 上午 9:50
-- To change this template use File | Settings | File Templates.
--

local loaded = package.loaded
local searchpath = package.searchpath

-- local require = import and import(...) or require
function import(modname)
    print("module name: ", modname)
    if modname then
        local prefix = modname:match "(.*%.).*$" or (modname .. ".")
        return function(name)
            local fullname = prefix .. name
            print("fullname: ", fullname)
            print("name: ", name)
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
        print("super require")
        return require
    end
end

print("search_path: ", searchpath)
print("path: ", package.path)
print("cpath: ", package.cpath)
print("loaded: ", package.loaded)
print("preload: ", package.preload)
print("loaders: ", package.loaders)

-- 获取
local modname = "com.heaven7.modname"
local prefix = modname:match "(.*%.).*$" or (modname .. ".")
--print(prefix) -- com.heaven7.

--local require = import and import(...) or require

--[[local list = require("listImpl")
print(list)]]
local im = import("fff")
print(im("ddd"))
