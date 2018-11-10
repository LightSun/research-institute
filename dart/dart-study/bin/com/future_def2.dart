// Copyright (c) 2013, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

import 'dart:async';

Future<void> printDailyNewsDigest() {
final future = gatherNewsReports();
return future.then(print);
// You don't *have* to return the future here.
// But if you don't, callers can't await it.
}

main() {
printDailyNewsDigest();
printWinningLotteryNumbers();
printWeatherForecast();
printBaseballScore();
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
Future<String> gatherNewsReports() =>
Future.delayed(oneSecond, () => news);

// Alternatively, you can get news from a server using features
// from either dart:io or dart:html. For example:
//
// import 'dart:html';
//
// Future<String> gatherNewsReportsFromServer() => HttpRequest.getString(
//      'https://www.dartlang.org/f/dailyNewsDigest.txt',
//    );