--
-- http://www.lua.org/pil/22.1.html
--

--print(os.time({year=1970, month=1, day=1, hour=0}))
--[[
--也就是说 “!*t” 得到的是一个 UTC 时间，为0度的经线（子午线），亦称本初子午线，通常将它与GMT视作等同（但是UTC更为科学和精确）。
--]]


local timeShift = 3 * 60 * 60  -- +3 hours
result = os.date('%Y-%m-%d %H:%M:%S', os.time() + timeShift)
print(result)