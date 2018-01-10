#include <Constants.au3>

; dialog
MsgBox($MB_SYSTEMMODAL, "My First Script!", "Hello World!")
test1()

Func test1()
	Return MsgBox($MB_SYSTEMMODAL, "My First Script!", "Hello World from Func !")
	EndFunc