import 'dart:async';
import 'dart:isolate';

/**
    start: 发送给宿主isolate
    end: 发送给宿主isolate
    sendReceive: foo
    listen data: foo
    received foo ... String
 */
main() async {
  var receivePort = new ReceivePort();
  await Isolate.spawn(echo, receivePort.sendPort);

  // 'echo'发送的第一个message，是它的SendPort
  var sendPort = await receivePort.first;

  var msg = await sendReceive(sendPort, "foo");
  print('received $msg ... ${msg.runtimeType}');
/*  msg = await sendReceive(sendPort, "bar");
  print('received $msg');*/
}


/// 新isolate的入口函数
echo(SendPort sendPort) async {
  print("echo>>> SendPort: ${sendPort.hashCode}");
  // 实例化一个ReceivePort 以接收消息
  var port = new ReceivePort();

  // 把它的sendPort发送给宿主isolate，以便宿主可以给它发送消息
  print("start: 发送给宿主isolate");
  sendPort.send(port.sendPort);
  print("end: 发送给宿主isolate");

  // 监听消息
  await for (var msg in port) {
    var data = msg[0];
    print("listen data: " + data);
    SendPort replyTo = msg[1];
    print("echo>>> replyTo SendPort: ${replyTo.hashCode}");
    replyTo.send(data);
    if (data == "bar") port.close();
  }
}

/// 对某个port发送消息，并接收结果
Future sendReceive(SendPort port, msg) {
  print("sendReceive >>> SendPort: ${port.hashCode}");
  print("sendReceive: " + msg);
  ReceivePort response = new ReceivePort();
  port.send([msg, response.sendPort]);
  return response.first;
}
