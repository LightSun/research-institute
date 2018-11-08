class Vector {
  final int x, y;

  Vector(this.x, this.y);

  Vector operator +(Vector v) => Vector(x + v.x, y + v.y);
  Vector operator -(Vector v) => Vector(x - v.x, y - v.y);

  bool operator ==(other) => other is Vector && (x ==other.x && y ==other.y);

// Operator == and hashCode not shown. For details, see note below.
// ···

//if you override ==, you should also override Object’s hashCode getter. For an example of overriding == and hashCode,

  @override
  void noSuchMethod(Invocation invocation) {
    print('You tried to use a non-existent member: ' +
        '${invocation.memberName}');
  }

  @override
  String toString() {
    return 'Vector{x: $x, y: $y}';
  }

}

void main() {
  final v = Vector(2, 3);
  final w = Vector(2, 2);

  print(v + w);
  assert(v + w == Vector(4, 5));
  assert(v - w == Vector(0, 1));

  var list = [1,2];
  print(list.runtimeType); //list<int>
}