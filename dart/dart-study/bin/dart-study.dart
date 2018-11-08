
import 'var_def.dart';
import 'datatype_def.dart';
import 'class_def.dart';
import 'async_def.dart';

external void testMethod();

void main() {
  print('Hello, World!' + name);
  testMethod();

  Rect rec = Rect();
  rec.top = 1;
  rec.left = 5;
  rec.height = 2;
  rec.width = 4;
  
  print(rec.size);
  print(rec);

  var value = Invocation.getter(new Symbol(name));
  print(value);
  checkVersion();
}
