import tkinter as tk
from tkinter import Frame, Entry, Button, messagebox
from src.libs import taobao

# http://yshblog.com/blog/148
class Application(Frame):
    def __init__(self, master=None):
        Frame.__init__(self, master)
        self.pack(fill=tk.X, padx=100)
        self.createWidgets()

    def createWidgets(self):
        self.nameInput = Entry(self)
        self.nameInput.pack(fill=tk.X, pady=10)
        self.nameInput.pack(fill=tk.X, padx=5)
        self.alertButton = Button(self, text=(' Please Input Website '), command=self.FUN)
        self.alertButton.pack(fill=tk.X, padx=30)
        self.alertButton.pack(fill=tk.X, pady=10)

    def FUN(self):
        name = self.nameInput.get() or 'blank'
        if name == 'blank':
            messagebox.showinfo('Message', 'Website is invalid!')
        else:
            taobao.DownloadImg(name)
            messagebox.showinfo('Message', 'Success: see \"save\" folder')



app = Application()
app.master.title('DownLoad IMG From Web : [LSX]')
app.mainloop()
