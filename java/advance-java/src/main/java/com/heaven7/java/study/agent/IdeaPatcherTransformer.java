package com.heaven7.java.study.agent;

import java.lang.instrument.ClassFileTransformer;

public interface IdeaPatcherTransformer extends ClassFileTransformer {

  boolean supported();

  boolean canRetransform();
}