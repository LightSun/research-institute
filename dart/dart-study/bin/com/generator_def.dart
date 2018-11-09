
import 'dart:async';

/**
 * dart 支持2种生成器：
 *   Synchronous 生成器。返回  Iterable类型对象  关键字'sync*'
 *   Asynchronous 生成器。返回 Stream 类型对象。 关键字 'async*'
*/

main(){
  //dart 中函数表达式 中 '=>' 不是 '->' (java)
  naturalsTo(10).forEach((i) => print(i));

  /*StreamController<int> controller = new StreamController<int>(
      sync: false,
      onListen: () {
         print("onListen");
      },
      onPause: () {
        print("onPause");
      },
      onResume: () {
        print("onResume");
      },
      onCancel: () {
        print("onCancel");
      });
  Stream<int> str = asynchronousNaturalsTo(10);
  Future fu = str.pipe(controller);
  var onValue = (dynamic value) {
     print("StreamController: $value");
  };
  fu.then(onValue);*/
  asynchronousNaturalsTo(10).forEach((val) => print("str.forEach: $val"));
}

Iterable<int> naturalsTo(int n) sync* {
  int k = 0;
  while (k < n) yield k++; //yield 同python.
}

Stream<int> asynchronousNaturalsTo(int n) async* {
  int k = 0;
  while (k < n) yield k++;
}

//递归 generator.
Iterable<int> naturalsDownFrom(int n) sync* {
  if (n > 0) {
    yield n;
    yield* naturalsDownFrom(n - 1);
  }
}