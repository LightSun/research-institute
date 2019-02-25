var console = require("console");
//var fs = require("fs");  // doesn't support fs
//var net = require("net"); //not
//var process= require("process"); //not
//var http= require("http");      //not
//var timers= require("timers");  //not
//var util= require("util");  //not
//var url= require("url");    //not
//var events= require("events");    //not
//var path= require("path");    //not
//var stream= require("stream");    //not
//var querystring= require("querystring");    //not
//var punycode= require("punycode");    //not
//var assert= require("assert");    //not

//正则表达式
var re = new RegExp("\\w+");

//support date,
var mydate = new Date();
console.log("---------" + mydate);

//------------------ json support ------------
var json = [];
var row1 = {};
row1.id= "1";
row1.name = "jyy";

var row2 = {id:'2',name:'abc'}
json.push(row1);
json.push(row2);
var jsonStr = JSON.stringify(json);
var jobj = JSON.parse(jsonStr);
console.log(jsonStr);
//解析 json
var objs = eval(json);
//或者var objs = eval(jsonStr);
for(var j = 0;j<objs.length;j++){
   console.log(objs[j].id);
   console.log(objs[j].name);
  }


//const a = 1; //not
//let b = 2; //not
