

main(){
  //dart 允许class  像方法那样调用 . 使用call function.
  var wf = new WannabeFunction();
  var out = wf("Hi","there,","gang");
  print('$out');
}

class WannabeFunction {
  call(String a, String b, String c) => '$a $b $c!';
}