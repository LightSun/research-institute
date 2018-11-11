import 'dart:io';

/**
 * file and FileSystemEntity
 */
main(){
/*  File myFile = new File('myFile.txt');
  myFile.rename('yourFile.txt').then((_) => print('file renamed'));*/

  String myPath = ".";
  FileSystemEntity.isDirectory(myPath).then((isDir) {
    if (isDir) {
      print('$myPath is a directory');
    } else {
      print('$myPath is not a directory');
    }
  });

}