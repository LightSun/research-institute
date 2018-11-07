
class Test{

}
class Test2{
  static const initialCapacity = 16;
}

//with 可用于无参构造.
//dart 没有interface关键字。所有类都可以继承和 实现。
class Rect extends Object with Test, Test2 implements Invocation{

  int width;
  int height;
  int left;
  int top;

  int get size => width * height;
  set w1 (w) => (
      width = w
  );


  void setWidth(int width){
    this.width = width;
  }

  @override
  String toString() {
    return 'Rect{width: $width, height: $height}';
  }

  @override
  dynamic noSuchMethod(Invocation invocation) {
    print(invocation);
  }
}