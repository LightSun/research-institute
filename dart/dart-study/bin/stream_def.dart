import 'dart:async';
import 'dart:math' show Random;

main() async {
  print('Compute π using the Monte Carlo method.');
  await for (var estimate in computePi(batch: 20).take(5/*00*/)) { // take 5表示最多获取5次流中的数据. 如果流不满足则可能少于5
    print('π ≅ $estimate');
  }
}

/// Generates a stream of increasingly accurate estimates of π.
/// '{}' 表示可选参数，并且，传值的时候智能显示的传. like  'computePi(batch: 6)'.
Stream<double> computePi({int batch: 10/*0000*/}) async* {
  print("computePi: batch = $batch");
  var total = 0;
  var count = 0;
  var index = 0; //used to see the loop
  while (true) {
    print("computePi: index = $index");
    index ++;

    var points = generateRandom().take(batch);
    var inside = points.where((p) => p.isInsideUnitCircle);
    total += batch;
    count += inside.length;
    var ratio = count / total;
    // Area of a circle is A = π⋅r², therefore π = A/r².
    // So, when given random points with x ∈ <0,1>,
    // y ∈ <0,1>, the ratio of those inside a unit circle
    // should approach π / 4. Therefore, the value of π
    // should be:
    yield ratio * 4;
  }
}

//'[]'表示可选参数. dart中所有数据默认值都是null.  这点不同于java]
Iterable<Point> generateRandom([int seed]) sync* {
  print("generateRandom: seed = $seed");
  final random = Random(seed);
  var index = 0;
  while (true) {
    print("generateRandom: $index");
    index ++;
    yield Point(random.nextDouble(), random.nextDouble());
  }
}

class Point {
  final double x, y;
  //构造函数/
  const Point(this.x, this.y);
  //是否在单位圆中
  bool get isInsideUnitCircle => x * x + y * y <= 1;
}