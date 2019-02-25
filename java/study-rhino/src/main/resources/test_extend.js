

// 定义一个动物类
function Animal (name) {
  // 属性
  this.name = name || 'Animal';
  // 实例方法
  this.sleep = function(){
    print(this.name + '正在睡觉！');
  }
}
// 原型方法
Animal.prototype.eat = function(food) {
  print(this.name + '正在吃：' + food);
};

function Cat(){
}
Cat.prototype = new Animal();
Cat.prototype.name = 'cat';

//　Test Code
var cat = new Cat();
print(cat.name);
print(cat.eat('fish'));
print(cat.sleep());
print(cat instanceof Animal); //true
print(cat instanceof Cat); //true
