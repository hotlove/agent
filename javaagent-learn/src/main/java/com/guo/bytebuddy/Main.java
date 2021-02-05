package com.guo.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;

import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @Date: 2021/2/4 15:30
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class Main {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
//        String helloWrold = new ByteBuddy()
//                .subclass(DB.class)
//                .method(named("hello"))
//                .intercept(MethodDelegation.to(DbInterceptor.class))
//                .make()
//                .load(ClassLoader.getSystemClassLoader(), INJECTION)
//                .getLoaded()
//                .newInstance()
//                .hello("test");
//        System.out.println(helloWrold);

        // 指定参数
        String helloWrold2 = new ByteBuddy()
                .subclass(DB.class)
                .method(named("hello"))
                .intercept(MethodDelegation
                        .withDefaultConfiguration() // 这里配置可以调用方法时传参
                        .withBinders(
                                Morph.Binder.install(OverrideCallable.class)
                        ).to(new DbInterceptor2()))
                .make()
                .load(ClassLoader.getSystemClassLoader(), INJECTION)
                .getLoaded()
                .newInstance()
                .hello("test");
        System.out.println(helloWrold2);
    }
}
