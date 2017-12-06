package com.heaven7.java.study.util;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * just wrap filer write source -> class
 */
public class ClassFiler implements Filer {

    private final Filer base;

    public ClassFiler(Filer base) {
        this.base = base;
    }

    @Override //just
    public JavaFileObject createSourceFile(CharSequence name, Element... originatingElements) throws IOException {
        return base.createSourceFile(name, originatingElements);
    }

    @Override
    public JavaFileObject createClassFile(CharSequence name, Element... originatingElements) throws IOException {
        return base.createClassFile(name, originatingElements);
    }

    @Override
    public FileObject createResource(JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName, Element... originatingElements) throws IOException {
        return base.createResource(location, pkg, relativeName, originatingElements);
    }

    @Override
    public FileObject getResource(JavaFileManager.Location location, CharSequence pkg, CharSequence relativeName) throws IOException {
        return base.getResource(location, pkg, relativeName);
    }
}
