#include <Array.au3>
#include <WinAPIShPath.au3>

Local $aCmdLine = _WinAPI_CommandLineToArgv($CmdLineRaw)
_ArrayDisplay($aCmdLine)

;Log($CmdLine[0]) ; Contains the total number of items in the array.
;Log($CmdLine[1]) ; The first parameter.
;Log($CmdLine[2]) ; The second parameter.
;...... etc.

;autoit3 command_line_param.au3 param1 "This is a string parameter" 99