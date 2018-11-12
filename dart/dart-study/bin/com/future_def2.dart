// Copyright (c) 2013, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

import 'dart:async';

/*Future<void> printDailyNewsDigest() {
  final future = gatherNewsReports();
  return future.then(print);
// You don't *have* to return the future here.
// But if you don't, callers can't await it.
}*/

Future<void> printDailyNewsDigest() {
  final future = gatherNewsReports();
  //_ 表示不在意参数
  return future
      .then((_) => print('All reports printed.'))
      .catchError(() => //catchError to handle error
          print("on error"));
// You don't *have* to return the future here.
// But if you don't, callers can't await it.
}

main() {
  printDailyNewsDigest();
  printWinningLotteryNumbers();
  printWeatherForecast();
  printBaseballScore();

  //Microtask 的优先级比 future高。因为micro人物队列优先级高。处理完后才会处理event队列（future的都是在event队列中）
  scheduleMicrotask(() => print('microtask #1 of 2'));

  //测试等待多个任务完成
  testWaitMultiTaskDone();

  scheduleMicrotask(() => print('microtask #2 of 2'));
}

/*Future<num> get animationFrame {
  var completer = new Completer<num>.sync();
  requestAnimationFrame((time) {
    completer.complete(time);
  });
  return completer.future;
}*/


//future 任务必须返回future，否则调用的Main方法结束后，就不等待此方法完成
Future<void> testWaitMultiTaskDone() {
  Future<String> f1 = Future.delayed(Duration(seconds: 1), () => "a");
  Future<String> f2 = Future.delayed(Duration(seconds: 5), () => "b");
  Future<String> f3 = Future.delayed(Duration(seconds: 3), () => "c");

  void handleListResult(List dynamic) {
    dynamic.forEach(print);
    print("dynamic.toString() = $dynamic");
  }
  return Future.wait([f1, f2, f3])
      .then((List dynamic) => handleListResult(dynamic))
      .catchError((_) => print("error"));
}

printWinningLotteryNumbers() {
  print('Winning lotto numbers: [23, 63, 87, 26, 2]');
}

printWeatherForecast() {
  print("Tomorrow's forecast: 70F, sunny.");
}

printBaseballScore() {
  print('Baseball score: Red Sox 10, Yankees 0');
}

const news = '<gathered news goes here>';
const oneSecond = Duration(seconds: 1);

// Imagine that this function is more complex and slow. :)
Future<String> gatherNewsReports() => Future.delayed(oneSecond, () => news);

// Alternatively, you can get news from a server using features
// from either dart:io or dart:html. For example:
//
// import 'dart:html';
//
// Future<String> gatherNewsReportsFromServer() => HttpRequest.getString(
//      'https://www.dartlang.org/f/dailyNewsDigest.txt',
//    );
