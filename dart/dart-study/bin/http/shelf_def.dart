


import 'package:shelf/shelf.dart' as shelf;
import 'package:shelf/shelf_io.dart' as io;

void main() {
  var handler = const shelf.Pipeline().addMiddleware(shelf.logRequests())
      .addHandler(_echoRequest);

  io.serve(handler, 'localhost', 8080).then((server) {
    print('Serving at http://${server.address.host}:${server.port}');
  });
}

shelf.Response _echoRequest(shelf.Request request) {
  //request.url 是相对路径。 比如 http://localhost:8080/hello.  那么request.url就是hello
  //request.requestedUri 是完整的路径
  return new shelf.Response.ok('Request for "${request.url}" ${request.requestedUri}');
}

