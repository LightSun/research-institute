--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/22
-- Time: 上午 11:51
-- To change this template use File | Settings | File Templates.
--

local package = package
local debug = debug

local function load_env(filename)
    local f,err = loadfile(filename)
    if f == nil then
        return err
    end
    return function()
        return function(env)
            if env then
                debug.setupvalue(f, 1, env)
            end
            return f(filename)
        end
    end
end

local function searcher_env(name)
    print("searcher_env: ","name")
    local filename, err = package.searchpath(name, package.upath)
    if filename == nil then
        return err
    else
        return load_env(filename)
    end
end

-- 已失效
table.insert(package.searchers, searcher_env)
--print("insert")
