

main (){
  var callbacks = [];
  for (var i = 0; i < 2; i++) {
    //集合添加的是函数.
    callbacks.add((a) => print(a));
  }
  for(var cl in callbacks){
    cl(1);
  }

  testSwitch();
  testCatchException();
 // testRethrow();
}

void testRethrow(){
    try {
      dynamic foo = true;
      print(foo++); // Runtime error
    } catch (e) {
      print('misbehave() partially handled ${e.runtimeType}.');
      rethrow; // Allow callers to see the exception.
    }
}

void testCatchException() {
  try {
    testSwitch();
  } on FormatException {
    // A specific exception
    //do some
  } on Exception catch (e) {
    // Anything else that is an exception
    print('Unknown exception: $e');
  } catch (e) {
    // No specified type, handles all
    print('Something really unknown: $e');
  }
}

void testSwitch() {
  var command = 'OPEN';
  switch (command) {
    case 'CLOSED':
     // do something
      break;
    case 'PENDING':
    // do something
      break;
    case 'APPROVED':
    // do something
      break;
    case 'DENIED':
    // do something
      break;
    case 'OPEN':
    // do something
      break;
    default:
    // do something
  }

//continue inside switch
  switch (command) {
    case 'CLOSED':
    // do something
      continue nowClosed;
  // Continues executing at the nowClosed label.

    nowClosed:
    case 'NOW_CLOSED':
    // Runs for both CLOSED and NOW_CLOSED.
    // do something
      break;
  }
  //throw 'Out of llamas!'; // dart抛异常可以抛任意对象。
}