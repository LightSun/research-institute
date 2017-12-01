package com.heaven7.java.study;

import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.java.study.agent.ClassRootFinder;
import com.heaven7.java.study.agent.IdeaPatcher;
import com.heaven7.java.study.agent.LiveInjector;

import java.util.HashMap;

public class AgentTest {

    private static final String TAG = "AgentTest";

    public static void main(String[] args){
        LiveInjector injector = new LiveInjector();
        String agentSourceFile = ClassRootFinder.findClassRootOfClass(IdeaPatcher.class);
        agentSourceFile = agentSourceFile + "/guava-19.0.jar";
        DefaultPrinter.getDefault().warn(TAG, "main", "agentSourceFile = " + agentSourceFile);
        if (!injector.isInjectable(agentSourceFile)) {
            DefaultPrinter.getDefault().warn(TAG, "main", "unable to inject Agent");
            return;
        }

        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("key", "value");
            injector.inject(agentSourceFile, param);
        } catch (Exception ex) {
            ex.printStackTrace();
            DefaultPrinter.getDefault().warn(TAG, "main", "Error injecting Agent");
        }
    }

}
