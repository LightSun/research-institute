


main(){
  dynamic emp = new Person("4");
  if (emp is Person) {
// Type check
    emp.firstName = 'Bob';
  }
  (emp as Person).firstName = 'dffff';

  var oth = new Person("4");
  assert (emp == oth);

  var o = "f";
  assert (emp != o);

  Object oth2 = new Person("6");
  assert (emp != oth2);


  Object def = null;
  def ??= o;  // 如果def = null, 就将o赋值给def.否则不变
  print(def);
  
  print(playerName(null));

  //级联操作符.  可访问内部成员.
  final addressBook = (AddressBookBuilder()
    ..name = 'jenny'
    ..email = 'jenny@example.com'
    ..phone = (PhoneBuilder()
      ..number = '415-555-0100'
      ..label = 'home')
        .build())
      .build();

  print(addressBook);

  //级联操作符无敌。。
  var sb = StringBuffer();
  sb..write('foo')
    ..write('bar');
  print(sb);

  Person p = Person("6");
  p?.firstName = "a";
  print(p?.firstName); // ?. 同kotlin. 不为null时访问.

  p = null;
  print(p?.firstName);
}

//如果name 是null, 就返回Guest.
String playerName(String name) => name ?? 'Guest';



class AddressBookBuilder{
   String name;
   String email;
   Phone phone;

   AddressBook build (){
     return new AddressBook(name, email, phone);
   }
}

class AddressBook{
  final String name;
  final String email;
  final Phone phone;

  const AddressBook(this.name, this.email, this.phone);

  @override
  String toString() {
    return 'AddressBook{name: $name, email: $email, phone: $phone}';
  }
}


class PhoneBuilder{
  String number;
  String label;

  Phone build(){
    return new Phone(number, label);
  }
}

class Phone{
  final String number;
  final String label;
  const Phone(this.number, this.label);

  @override
  String toString() {
    return 'Phone{number: $number, label: $label}';
  }
}



class Person{
  final String name;
  String firstName;

  //const 构造必须用于final修饰的field.
  // 并且所有field都必须是final. 否则应该取消const修饰
  /*const*/ Person(this.name);

  @override
  String toString() {
    return 'Person{name: $name, firstName: $firstName}';
  }

  @override
  bool operator ==(other) {
    return other is Person && other.name == this.name;
  }

}