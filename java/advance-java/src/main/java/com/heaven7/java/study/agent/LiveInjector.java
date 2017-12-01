package com.heaven7.java.study.agent;

import com.heaven7.java.study.utils.StringUtils;
import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to inject a java agent 'on the fly' into your VM. This feature only works on sun-derived VMs, such as the openJDK, sun JVMs,
 * and apple's VMs.
 * <p>
 * While an agent can be injected in both 1.5 and 1.6 VMs, class reloading is only supported on 1.6 VMs, so if you want to be 1.5 compatible,
 * and want to transform classes, you'll need to ensure you inject the agent before the class you're interested in gets loaded. Otherwise, you can
 * just reload these classes from within the injected agent.
 * <p>
 * As a convenience, you can choose to inject this class (which is also an agent), but as a java agent <b>has</b> to come in a jar file, this only
 * works if this class has been loaded from a jar file on the local file system. If it has indeed been loaded that way, then this class can take care
 * of injecting itself. A common scenario is to distribute your patching application together with {@code lombok.patcher} in a single jar file.
 * That way, you can use the live injector, and your injected agent can then run patchscripts that call into your own code, and all your code's dependencies
 * will be available because they, too, are in this unified jar file.
 */
public class LiveInjector {

  /**
   * Injects the jar file that contains the code for {@code LiveInjector} as an agent. Its your job to make sure this jar is in fact an agent jar.
   *
   * @throws IllegalStateException If this is not a sun-derived v1.6 VM.
   */
  public void injectSelf() throws IllegalStateException {
    inject(ClassRootFinder.findClassRootOfSelf());
  }

  public boolean isSupportedEnvironment() {
    try {
      Class.forName("com.sun.tools.attach.VirtualMachine");
    } catch (ClassNotFoundException e) {
      return false;
    }

    return true;
  }

  public boolean isInjectable(String jarFile) {
    File f = new File(jarFile);

    return f.isFile();
  }

  /**
   * Injects a jar file into the current VM as a live-loaded agent. The provided jar will be loaded into its own separate class loading context,
   * and its manifest is checked for an {@code Agent-Class} to load. That class should have a static method named {@code agentmain} which will
   * be called, with an {@link java.lang.instrument.Instrumentation} object that you're probably after.
   *
   * @throws IllegalStateException If this is not a sun-derived v1.6 VM.
   */
  public void inject(String jarFile) throws IllegalStateException {
    this.inject(jarFile, Collections.<String, String>emptyMap());
  }

  public void inject(String jarFile, Map<String, String> options) throws IllegalStateException {

    List<String> optionsList = new ArrayList<String>();

    for (Map.Entry<String, String> entry : options.entrySet()) {
      String option = entry.getKey() + "=" + entry.getValue();
      optionsList.add(option);
    }
    String optionString = StringUtils.join(optionsList, ",");

    if (!this.isInjectable(jarFile)) {
      throw new IllegalArgumentException("Live Injection is not possible unless the classpath root to inject is a jar file.");
    }
    injectInternal(jarFile, optionString);
  }

  private void injectInternal(String jarFile, String options) throws IllegalStateException {

    String ownPidS = ManagementFactory.getRuntimeMXBean().getName();
    ownPidS = ownPidS.substring(0, ownPidS.indexOf('@'));
    int ownPid = Integer.parseInt(ownPidS);

    try {
      VirtualMachine vm = VirtualMachine.attach(String.valueOf(ownPid));
      vm.loadAgent(jarFile, options);
    } catch (Throwable exception) {
      throw new IllegalStateException("agent injection not supported on this platform due to unknown reason", exception);
    }
  }
}
