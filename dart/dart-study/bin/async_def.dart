
import 'dart:async';

import 'dart:async';

Future checkVersion() async {
  var version = await lookUpVersion();
  // Do something with version
  return Future.value(version);
}

Future<int> lookUpVersion() async {
  var list = Stream0();
  var sta = Stream.fromIterable([1,2,3]);
  await for (int a in sta) {
    print(a);
  }
  return 1;
}

//_开始的命名，表示私有。 函数，变量，类名都可以。
void _test(){
   print("_test");
}

class Stream0 implements Stream<num>{
  @override
  Future<bool> any(bool Function(num element) test) {
    // TODO: implement any
  }

  @override
  Stream<num> asBroadcastStream({void Function(StreamSubscription<num> subscription) onListen, void Function(StreamSubscription<num> subscription) onCancel}) {
    // TODO: implement asBroadcastStream
  }

  @override
  Stream<E> asyncExpand<E>(Stream<E> Function(num event) convert) {
    // TODO: implement asyncExpand
  }

  @override
  Stream<E> asyncMap<E>(FutureOr<E> Function(num event) convert) {
    // TODO: implement asyncMap
  }

  @override
  Stream<R> cast<R>() {
    // TODO: implement cast
  }

  @override
  Future<bool> contains(Object needle) {
    // TODO: implement contains
  }

  @override
  Stream<num> distinct([bool Function(num previous, num next) equals]) {
    // TODO: implement distinct
  }

  @override
  Future<E> drain<E>([E futureValue]) {
    // TODO: implement drain
  }

  @override
  Future<num> elementAt(int index) {
    // TODO: implement elementAt
  }

  @override
  Future<bool> every(bool Function(num element) test) {
    // TODO: implement every
  }

  @override
  Stream<S> expand<S>(Iterable<S> Function(num element) convert) {
    // TODO: implement expand
  }

  // TODO: implement first
  @override
  Future<num> get first => null;

  @override
  Future<num> firstWhere(bool Function(num element) test, {num Function() orElse}) {
    // TODO: implement firstWhere
  }

  @override
  Future<S> fold<S>(S initialValue, S Function(S previous, num element) combine) {
    // TODO: implement fold
  }

  @override
  Future forEach(void Function(num element) action) {
    // TODO: implement forEach
  }

  @override
  Stream<num> handleError(Function onError, {bool Function(Error) test}) {
    // TODO: implement handleError
  }

  // TODO: implement isBroadcast
  @override
  bool get isBroadcast => null;

  // TODO: implement isEmpty
  @override
  Future<bool> get isEmpty => null;

  @override
  Future<String> join([String separator = ""]) {
    // TODO: implement join
  }

  // TODO: implement last
  @override
  Future<num> get last => null;

  @override
  Future<num> lastWhere(bool Function(num element) test, {num Function() orElse}) {
    // TODO: implement lastWhere
  }

  // TODO: implement length
  @override
  Future<int> get length => null;

  @override
  StreamSubscription<num> listen(void Function(num event) onData, {Function onError, void Function() onDone, bool cancelOnError}) {
    // TODO: implement listen
  }

  @override
  Stream<S> map<S>(S Function(num event) convert) {
    // TODO: implement map
  }

  @override
  Future pipe(StreamConsumer<num> streamConsumer) {
    // TODO: implement pipe
  }

  @override
  Future<num> reduce(num Function(num previous, num element) combine) {
    // TODO: implement reduce
  }

  // TODO: implement single
  @override
  Future<num> get single => null;

  @override
  Future<num> singleWhere(bool Function(num element) test, {num Function() orElse}) {
    // TODO: implement singleWhere
  }

  @override
  Stream<num> skip(int count) {
    // TODO: implement skip
  }

  @override
  Stream<num> skipWhile(bool Function(num element) test) {
    // TODO: implement skipWhile
  }

  @override
  Stream<num> take(int count) {
    // TODO: implement take
  }

  @override
  Stream<num> takeWhile(bool Function(num element) test) {
    // TODO: implement takeWhile
  }

  @override
  Stream<num> timeout(Duration timeLimit, {void Function(EventSink<num> sink) onTimeout}) {
    // TODO: implement timeout
  }

  @override
  Future<List<num>> toList() {
    // TODO: implement toList
  }

  @override
  Future<Set<num>> toSet() {
    // TODO: implement toSet
  }

  @override
  Stream<S> transform<S>(StreamTransformer<num, S> streamTransformer) {
    // TODO: implement transform
  }

  @override
  Stream<num> where(bool Function(num event) test) {
    // TODO: implement where
  }

}

