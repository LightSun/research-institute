--
-- Created by IntelliJ IDEA.
-- User: Administrator
-- Date: 2019/6/20
-- Time: 下午 18:58
-- To change this template use File | Settings | File Templates.
--

-- without the below statement you can't use something like:
-- import('main') in main.lua
if not package.loading then package.loading = {} end

-- a chatty version of the actual import function above
function import(x)
    if package.loading[x] == nil then
        package.loading[x]=true
        print('loading started for ' .. x)
        require(x)
        print('loading ended for ' .. x)
        package.loading[x]=nil
    else
        print('already loading ' .. x)
    end
end
import "a"
print("second attempt")
import 'a'