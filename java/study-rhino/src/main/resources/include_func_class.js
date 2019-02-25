function Animal (name) {
  // 属性
  this.name =  name;
  // 实例方法
  this.sleep = function(){
    print(this.name + '正在睡觉！');
  }
}

function Person(name){
   var ins = new Animal();
   ins.name = name;

   ins.eat=function(str){
    print(this.name + " eat " + str)
   }
   return ins;
}

exports.Person = Person;
exports.Animal = Animal;