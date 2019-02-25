//mock the 'console.log("xxx")'
var console = exports;
console.log=function(msg){
    print(msg);
}