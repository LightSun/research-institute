
import 'dart:html'; //导入系统库
//import 'package:test/test.dart' as test1; \\导入第三方库
//import 'package:lib1/lib1.dart' show foo; 导入库的一部分
//import 'package:lib2/lib2.dart' hide foo; 导入库，除了foo
//import 'package:greetings/hello.dart' deferred as hello; 懒加载一个库

/*Future greet() async {     //用的时候需要用 loadLibrary()加载.
  await hello.loadLibrary(); //await 导致暂停执行，直到 loadLibrary（）调用完
  hello.printGreeting();
}*/
/**
 * 泛型 。集合
 */
main(){
  var names = List<String>();
  names.addAll(['Seth', 'Kathy', 'Lars']); //匿名list
  //names.add(42); // Error

  //create a set from list elements.
  var nameSet = Set<String>.from(names);
  print(nameSet);

  var views = Map<int, String>();

  //在java中的泛型是编译型泛型，运行时会擦除，dart中则是运行时的泛型
  print(names is List<String>); // true
}

class A{

}
//泛型限定
class Foo<T extends A> {
  // Implementation goes here...
  String toString() => "Instance of 'Foo<$T>'";
}

//泛型方法... java是把泛型类型写在前面
T first<T>(List<T> ts) {
  // Do some initial work or error checking, then...
  T tmp = ts[0];
  // Do some additional checking or processing...
  return tmp;
}

