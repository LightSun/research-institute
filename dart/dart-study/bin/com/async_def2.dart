import 'dart:async';
import 'dart:isolate';

main() {
  //使用new Future 把任务加入队列
  new Future(() => 21).then((v) => v * 2).then((v) => print(v));

  testFuture2();
}



void testFuture2() {
  // 一秒以后将任务添加至event队列
  new Future.delayed(const Duration(seconds: 1), () {
    //任务具体代码
    print("runned after 1 second.");
  });

  /* 虽然上面这个例子中一秒后向event队列添加一个任务，但是这个任务想要被执行的话必须满足一下几点：

  1, main方法执行完毕
  2, microtask队列为空
  3 该任务前的任务全部执行完毕
  所以该任务真正被执行可能是大于1秒后。
  */
}
