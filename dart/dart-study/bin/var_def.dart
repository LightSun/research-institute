
var name = 'heaven7'; //类型自动推断
dynamic name1 = "sdfsdf"; //类型可变
String name2 = "dsfsd"; //确定的类型
int lineCount; //所有的变量，包括数字类型，如果没有初始化，默认值是null

//final and const. const 是编译时常量。 final可以用变量声明， const不能.
//也就是说，const 修饰的变量在编译期值就已经确定， final 的则是惰性初始化。
final name3 = "dfdsf"; //同java . 并且自动推断
final String nick  = "sdfdsf";

const bar = "";
const String bar2 = "";

//[] is an list. ' const []'标识不可变的list.
//{} 标识是map
var foo = const [];

final bar4 = const [1,2,3];
const list = [1, 2, 3];
const list2 = const [1, 2, 3];

//const list3 = const [new DateTime.now(), 2, 3]; //error new DateTime.now() 不是常量

//字符串可以用 '' 或者“”。 也可以用+拼接.

//runes  标识unicode字符