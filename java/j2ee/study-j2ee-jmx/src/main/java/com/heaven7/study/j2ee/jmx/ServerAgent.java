package com.heaven7.study.j2ee.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.jdmk.comm.HtmlAdaptorServer;

public class ServerAgent {
    public static void main(String[] args)
            throws MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException,
            MBeanRegistrationException, NotCompliantMBeanException {

        // create mbean server
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        // create object name
        ObjectName helloName = new ObjectName("jmxBean:name=hello");

        // create mbean and register mbean
        server.registerMBean(new HelloWorld(), helloName);

        // create adaptor, adaptor is just a form as show mbean. It has no relation to specific mbean.
        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
        // create adaptor name
        ObjectName adaptorName = new ObjectName("jmxAdaptor:name=adaptor,port=5050");
        // register adaptor and adaptor name
        server.registerMBean(adaptor, adaptorName);

        adaptor.setPort(9999);
        adaptor.start();
        System.out.println("....................server start....................");

        // JMXConnectorServer service
        try {
            // 这句话非常重要，不能缺少！注册一个端口，绑定url后，客户端就可以使用rmi通过url方式来连接JMXConnectorServer
            LocateRegistry.createRegistry(8888);
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8888/server");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
            System.out.println("....................begin rmi start.....");
            cs.start();
            System.out.println("....................rmi start.....");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
