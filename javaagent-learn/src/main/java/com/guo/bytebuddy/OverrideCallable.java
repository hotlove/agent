package com.guo.bytebuddy;

import java.util.concurrent.Callable;

public interface OverrideCallable {
    Object call(Object[] args);
}
