package com.heaven7.test;

import org.mozilla.javascript.GeneratedClassLoader;
import org.mozilla.javascript.SecurityUtilities;

/** @author heaven7 */
public class TestClassLoader extends ClassLoader implements GeneratedClassLoader {

    public TestClassLoader() {
        this.parentLoader = getClass().getClassLoader();
    }

    public TestClassLoader(ClassLoader parentLoader) {
        this.parentLoader = parentLoader;
    }

    public Class<?> defineClass(String name, byte[] data) {
        // Use our own protection domain for the generated classes.
        // TODO: we might want to use a separate protection domain for classes
        // compiled from scripts, based on where the script was loaded from.
        return super.defineClass(
                name, data, 0, data.length, SecurityUtilities.getProtectionDomain(getClass()));
    }

    public void linkClass(Class<?> cl) {
        resolveClass(cl);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cl = findLoadedClass(name);
        if (cl == null) {
            if (parentLoader != null) {
                cl = parentLoader.loadClass(name);
            } else {
                cl = findSystemClass(name);
            }
        }
        if (resolve) {
            resolveClass(cl);
        }
        return cl;
    }

    private final ClassLoader parentLoader;
}
