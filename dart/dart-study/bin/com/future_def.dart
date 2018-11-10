import 'dart:async';

Future<void> printDailyNewsDigest() async {
  try {
    var newsDigest = await gatherNewsReports();
    print(newsDigest);
  } catch (e) {
  // Handle error...
  }
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
Future<String> gatherNewsReports() => Future.delayed(oneSecond, () => news);

// Alternatively, you can get news from a server using features
// from either dart:io or dart:html. For example:
//
// import 'dart:html';
//
// Future<String> gatherNewsReportsFromServer() => HttpRequest.getString(
//      'https://www.dartlang.org/f/dailyNewsDigest.txt',
//    );