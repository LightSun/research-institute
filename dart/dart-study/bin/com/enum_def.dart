enum Color { red, green, blue }

main(){
  assert(Color.red.index == 0);
  assert(Color.green.index == 1);
  assert(Color.blue.index == 2);

  List<Color> colors = Color.values;
  assert(colors[2] == Color.blue);
}