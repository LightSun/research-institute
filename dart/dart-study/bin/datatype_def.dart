import 'dart:math'; //jenkins_smi_hash.dart 'bobo jenkens ' 提出的多个基于字符串的hash算法
//内置7种数据类型。
//numbers, strings, booleans, lists, maps, runes, symbols.

//numbers: int (由平台决定 DartVM - 64位, 编译js-54位) , double(64位)
//dart 支持操作符重载

int x = 1;
int hex = 0x1111;
double e2 = 1.24e5;

var list = [1, 2, 3];

var map = {
  1: ["1"],
  2: "2",
  3: "3",
};

void iterator() {
  for (int i = 0; i < 10; i++) {}
}

int fibonacci(int n) {
  if (n == 0 || n == 1) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}

void testMethod() {
  print("testMethod");
}

//comments :同java
//循环同java
//import
/**
    核心库: import 'dart:math';
    普通: import 'xxx.dart';
 */

var test_list$ = ["1", "turn_1", "3turn3"];
//test_list$.where((name) => name.contains('turn')).forEach(print);
DateTime dt =  DateTime(1977, 9, 5);