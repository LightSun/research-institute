
import 'dart:math';

main(){
  var p2 = Rect.fromJson({'width': 1, 'height': 2});
  print("p2.width = ${p2.width}, p2.height = ${p2.height}");
  //获取运行时类型
  print(p2.runtimeType);

  testNonDefaultConstructor();
}

void testNonDefaultConstructor(){
  var emp = new Employee.fromJson({});

  // Prints:
  // in Person
  // in Employee
  if (emp is Person) {
    // Type check
    emp.firstName = 'Bob';
  }
  (emp as Person).firstName = 'Bob';
}


class Test{

}
class Test2{
  static const initialCapacity = 16;
}

//with:  可用于无参构造. 'Mixins 'are a way of reusing a class’s code in multiple class hierarchies.
//dart 没有interface关键字。所有类都可以继承和 实现。
//
class Rect extends Object with Test, Test2 implements Invocation{

  int width;
  int height;
  int left;
  int top;

  //getter and setter
  num get right => left + width;
  set right(num value) => left = value - width; //当给right赋值时会调用
  num get bottom => top + height;
  set bottom(num value) => top = value - height;

  int get size => width * height;
  set w1 (w) => (
      width = w
  );

  Rect(){}

  Rect.origin(){
     width = 0;
     height = 0;
  }

  static Rect fromJson(Map<String, int> json){
    Rect rect = Rect.origin();
    rect.width = json['width'];
    rect.height = json['height'];
    return rect;
  }

  Rect.fromJson2(Map<String, int> json): width = json['width'], height = json['height']{
     print("dsfdsfs");
  }

  Rect.fromJson3(Map<String, int> json):assert(json.length > 0), width = json['width'], height = json['height'];

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

class Person {
  String firstName;

  Person.fromJson(Map data) {
    print('in Person');
  }
}

class Employee extends Person {
  // Person does not have a default constructor;
  // you must call super.fromJson(data).
  //调用父类构造函数
  Employee.fromJson(Map data) : super.fromJson(data) {
    print('in Employee');
  }
}