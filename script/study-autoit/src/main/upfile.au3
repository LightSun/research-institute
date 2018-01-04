
;focus on
ControlFocus("Open", "", "Edit1")

;wait 10 seconds for the upload window to appear
Local $window = WinWait("[CLASS:#32770]","",10)

;set file name text on the edit field
;ControlSetText("Open", "", "Edit1", "E:\123.txt");
;ControlCommand("Sign-On", "", "[CLASS:Edit;INSTANCE:1]", "Edit1", "E:\123.txt")
$Result = ControlSetText($window, "", "Edit1", "E:\123.txt")

Local $text = ControlGetText($window, "" , "Edit1")
Log("The text in Edit1 is:  "& $text)
;MsgBox($MB_SYSTEMMODAL, "", "The text in Edit1 is:  "& $text)

;If $Result = 0 Then
;	Log("SetText of file path failed.");
;EndIf

Sleep(2000)
;Click on the open button
ControlClick($window, "", "Button1")