
bool topLevel = true;

//dart 支持方法嵌套.
void main() {
  var insideMain = true;

  void myFunction() {
    var insideFunction = true;

    void nestedFunction() {
      var insideNestedFunction = true;

      assert(topLevel);
      assert(insideMain);
      assert(insideFunction);
      assert(insideNestedFunction);
    }
  }
  Function fc1 = makeAdder(4);
  print(fc1); //function
  print(fc1(3));     //7
  print(fc1(3) == 5); // false.
  print(fc1(3) == 7); //true

  //Function.apply(fc1, [1,2,3], {#f: 4, #g: 5}); // error , function mismatch
}

/// Returns a function that adds [addBy] to the
/// function's argument.
/// 返回的是一个函数. Closure: (num) => num
Function makeAdder(num addBy) {
  return (num i) => addBy + i;
}