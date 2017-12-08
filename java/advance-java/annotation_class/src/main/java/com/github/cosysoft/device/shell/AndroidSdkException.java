package com.github.cosysoft.device.shell;

import com.github.cosysoft.device.exception.NestedException;

public class AndroidSdkException extends NestedException {
  private static final long serialVersionUID = 5431510243540521938L;

  public AndroidSdkException(String message) {
    super(message);
  }

  public AndroidSdkException(Throwable t) {
    super(t);
  }

  public AndroidSdkException(String message, Throwable t) {
    super(message, t);
  }
}
