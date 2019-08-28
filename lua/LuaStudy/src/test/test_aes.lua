local Array       = require("lockbox.util.array");
local Stream      = require("lockbox.util.stream");
local ECBMode     = require("lockbox.cipher.mode.ecb");
local XTEACipher  = require("lockbox.cipher.xtea");
local ZeroPadding = require("lockbox.padding.zero");
local AES256Cipher = require("lockbox.cipher.aes256");
local AES128Cipher = require("lockbox.cipher.aes128");

local base64      = require("src.aes.base64");

local function replaceAll(str, pattern, repl, count)
    local re, _= string.gsub(str, pattern, repl, count)
    return re;
end
local function bytes2Str(bytes)
    --string.char(unpack(bytes))
    local str = ""
    for i = 1 , #bytes do
        str = str..string.char(bytes[i]) --utf8
    end
    return str;
end
local function add_to_16(value)
    while (#value % 16 ~= 0) do
        value = value .. "\0"
    end
    return value;
end

local arg = {};
arg[1]    = "E:\\study\\github\\research-institute\\lua\\LuaStudy\\data\\transition_0_0.json"
arg[2]    = "$*xzKNJnQVlg61tbsB9GWpkw$wWV&E@h"
arg[3]    = "E:\\study\\github\\research-institute\\lua\\LuaStudy\\data\\transition_0_0_out.json"
if (#arg < 2) then
    print("Usage: filedecrypt.lua [file] [password] > decryptedfile\n");
    print("Do not use for real decryption, because the password is easily viewable while decrypting.");
    return 1;
end
local key = add_to_16(arg[2])
print(key)

local function testAesDec()
    local file   = assert(io.open(arg[1] , "r"));
    local cipher = file:read("*all");
    file:close();
    cipher         = base64.dec(cipher)
    print("cipher", cipher)

    local decipher = ECBMode.Decipher()
                            .setKey(Array.fromString(key))
                            .setBlockCipher(AES256Cipher)
                            .setPadding(ZeroPadding);

    local plain    = decipher.init()
                             .update(Stream.fromString(""))
                             .update(Stream.fromString(cipher))
                             .finish()
                             .asBytes();
    plain = bytes2Str(plain)
    if (plain == nil) then
        print("Invalid password.");
    else
        file = io.open(arg[3] , "w+")
        file:write(plain);
        print("testAesDec",plain)
        file:close();
    end
end

local function testAesDec2(content)
    local decipher = ECBMode.Decipher()
                            .setKey(Array.fromString(key))
                            .setBlockCipher(AES256Cipher)
                            .setPadding(ZeroPadding);

    local plain    = decipher.init()
                             .update(Stream.fromString(""))
                             .update(Stream.fromString(content))
                             .finish()
                             .asBytes();
    plain = replaceAll(bytes2Str(plain), "\0", "")
    if (plain == nil) then
        print("Invalid password.");
    else
        local file = io.open(arg[3] , "w+")
        file:write(plain);
        print("testAesDec2", plain)
        file:close();
    end
    return plain;
end

local function testAesEnc()
    local filename = "D:\\Users\\WeChat Files\\studyheaven7\\FileStorage\\File\\2019-08\\aes_py\\des\\transition_0_0.json";
    local file   = assert(io.open(filename , "r"));
    local cipher = file:read("*all");
    file:close();

    local enc = ECBMode.Cipher()
                            .setKey(Array.fromString(key))
                            .setBlockCipher(AES256Cipher)
                            .setPadding(ZeroPadding);

    local plain    = enc.init()
                             .update(Stream.fromString(""))
                             .update(Stream.fromString(cipher))
                             .finish()
    plain = bytes2Str(plain.asBytes())
    --plain = plain.asHex()
    print(plain)
    return plain;
end

local enc_r1 = testAesEnc();
local cs = "n7mHWuk9qKON/WL1KxkeODYOz6xX4nBXANizh5tJSfxdBmWn4RjkPXkLfW+oyrpLQ3lsP+resNJutxayxlHt+4Xhe2DjFs5j3xotFFYtEfblXTNSIE4SxKGPrpnYlrtCVz1i8yy+0BvfItCw5/dJbN3yYUY4F4iipwLavf4951Wrfbp0IKQemUl9eiujWWwXN5wWrYgLwGaMPyIqDcjiI6pL57XBPdsmQyyVRtmnws12hljghbVMCRqwbzzFg57ZibDqbe5gX2E0G3iLJ4LTiGPHbOFfImjIhLcmYOQH8CRpc1188ETGT4quEj5ZAehO7snR9pinKw8hR2GHAxy70ZwzmyaovYvgbuL7xMliXvuUbxIL4IIpGCoCVt/9k0I6TxWN36Aa2Bo9QNHHk0hmOnqXwDire3u83MpZntRUbSaR7ZNweAtZNaGoZKiRvW4Ko5osgvBv/bFoGQ4Zd2xESyCVEszHGhqCowPEy2MLycwEIE8XdMp/OSpd8PmgZPURtlhVepw4V8NCiJ0t42P9RcHaiUv+uVh2PH6kjZZBDEOHARD/MjtsVzHC1cx95sDW"
print(base64.dec(cs) == enc_r1)
testAesDec2(enc_r1)
print("--------------")
testAesDec()

--[[
local a = string.byte("12345", 2,4);
print(a)]]
