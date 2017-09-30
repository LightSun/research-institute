
编译已有的java文件
E:\study\github\research-institute\base-compile>
       javac -encoding UTF-8 -d out/production/ src/main/java/com/heaven7/java/compile/apt/*.java

接着执行注解处理器:
javac -encoding UTF-8 -cp out\production\ -processor com.heaven7.java.compile.apt.HelloProcessor -d out\production -s src/ src/main/java/com/heaven7/java/compile/apt\*.java


3. javac
这里稍微解释一下 javac 命令, IDE 用多了, 写的时候都忘得差不多了 (:зゝ∠)
javac 用于启动 java 编译器, 格式为 javac <options> <source files>, 其中 <options> 的格式为 -xx xxxx, 都是配对出现的, 用于指定一些信息.

这里 <options> 的位置并没有讲究, 只要在 javac 后面就行了, 在两个 xxx.java 之间出现也是可以的, 比如: javac -d out\production\ src\apt\HelloProcessor.java -encoding UTF-8 src\apt\Hello.java 正常执行.

一些 <option>

    -cp <路径>
          和 -classpath <路径> 一样, 用于指定查找用户类文件和注释处理程序的位置
    -d <目录>
          指定放置生成的类文件的位置
    -s <目录>
          指定放置生成的源文件的位置
    -processorpath <路径>
          指定查找注释处理程序的位置, 不写的话会使用 -cp 的位置
    -processor <class1>[,<class2>,<class3>...]
          要运行的注释处理程序的名称; 绕过默认的搜索进程


编译java文件的命令都知道，是javac  aaa.java .
但是，如果你这个java文件中，引入其他的jar包，需要用到-cp命令。
加载单个jar包。需要下载用到的jar包，并引入目录。
javac -cp C:\Users\whr-pc\Desktop\classpath\img\json-lib-2.4-jdk15.jar Xml2Json.java
加载多个jar包，中级用   ； 符号隔开。
javac -cp C:\Users\whr-pc\Desktop\classpath\img\json-lib-2.4-jdk15.jar;jdom-1.0.jar;spring-json-1.3.1.jar Xml2Json.java

生成class文件后，执行class文件，也需要用 -cp  命令加载jar包。就可以了

gradle 文章
http://blog.csdn.net/maosidiaoxian/article/details/55683474