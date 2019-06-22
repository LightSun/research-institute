--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/22
-- Time: 上午 11:52
-- To change this template use File | Settings | File Templates.
--

local dir = "src/extra_require/?.lua";
package.path = dir..";"..package.path

local s2 = package.searchers[2];
package.searchers[2] = function( name )
    print("try to load: ", name)
    local result = s2(name);
    print("result", type(result)) --function
    return result
end

require "require_env"

package.upath = "src/extra_require/?.lua"

local myprint = print

local env = {
    print = function (...)
        myprint("hook", ...)
    end
}

local s = require "mymod"(env)

s.test "hello world"  -- hook hello world