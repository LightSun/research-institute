


main(){
  print(greetBob(Person('Kathy')));
  print(greetBob(Impostor()));
}



String greetBob(Person person) => person.greet('Bob');


abstract class Doer {
  // Define instance variables and methods...

  void doSomething(); // Define an abstract method.
}

class EffectiveDoer extends Doer {
  void doSomething() {
    // Provide an implementation, so the method is not abstract here...
  }
}

//-------------------------------------------------------------
// A person. The implicit interface contains greet().
class Person {
  // In the interface, but visible only in this library.
  final _name;

  // Not in the interface, since this is a constructor.
  Person(this._name);

  // In the interface.
  String greet(String who) => 'Hello, $who. I am $_name.';
}

// An implementation of the Person interface.
class Impostor implements Person {


  //接口形式腹复写方法时，不能用super.xxx() 方法
  String greet(String who) {
    return 'Hi $who. Do you know who I am?';
  }

  // TODO: implement _name
  // 相当于抹掉Person的构造函数在此类中的作用
  @override
  get _name => null;
}


