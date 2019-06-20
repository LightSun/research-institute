--
-- http://www.lua.org/pil/22.1.html
--

local serverTime = 1536722753 -- 2018/09/12 11:25

print(os.time()) -- mill-seconds

-- 用本地的时间和utc事件比较得到diff
function getTimeZone()
    local now = os.time()
    return os.difftime(now, os.time(os.date("!*t", now))) -- utc 时间
end

-- 8 hour * 3600 seconds = 28800 seconds
local timeZone = getTimeZone()/ 3600

print("timeZone : " .. timeZone)


local timeInterval = os.time(os.date("!*t", serverTime))
        + timeZone * 3600
        + (os.date("*t", time).isdst and -1 or 0) * 3600

local timeTable = os.date("*t", timeInterval)

--[[
for k, v in pairs(timeTable) do
    print(k .. ":" .. tostring(v))
end
]]

print(timeTable.year .. "/" .. timeTable.month .. "/" .. timeTable.day .. " " .. timeTable.hour .. ":" .. timeTable.min .. ":" .. timeTable.sec)

