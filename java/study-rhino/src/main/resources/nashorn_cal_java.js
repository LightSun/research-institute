
var MyJavaClass = Java.type('nashorn.MyJavaClass');

var result = MyJavaClass.fun1('John Doe');
print(result);

MyJavaClass.fun3({
    foo: 'bar',
    bar: 'foo'
});

