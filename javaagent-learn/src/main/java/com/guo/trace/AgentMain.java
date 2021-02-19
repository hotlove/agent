package com.guo.trace;

import com.guo.agent.TimerInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Date: 2021/2/8 11:24
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class AgentMain {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("探针开始========================");

//        MethodInterceptor methodInterceptor = new MethodInterceptor();
//        System.out.println("类加载器："+methodInterceptor.getClass().getClassLoader().getClass().getName());
//
//        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
//            @Override
//            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
//
//                return builder
//                        .method(ElementMatchers.<MethodDescription>any())
//                        .intercept(MethodDelegation.to(methodInterceptor));
//            }
//        };
        // (com.badu.bdsaas.baseinfo.service.*|com.badu.btsaas.controller.*)
        new AgentBuilder
                .Default()
                .type(buildMatch())
//                .type(
//                        ElementMatchers
//                                .<TypeDescription>nameContains("com.badu")
//                                .and(not(nameContainsIgnoreCase("equals")))
//                                .and(not(nameContainsIgnoreCase("toString")))
//                                .and(not(isInterface()))
//                                .and(not(nameContainsIgnoreCase("io.spring")))
//                                .and(not(nameContainsIgnoreCase("util")))
//                                .and(not(nameContainsIgnoreCase("interceptor")))
//                                .and(not(nameContainsIgnoreCase("CGLIB")))
////                                .and(not(isSetter()))
////                                .and(not(isGetter()))
//                )
//                .transform(transformer)
                .transform((builder, typeDescription, classLoader, javaModule) ->
                                builder
                                .method(ElementMatchers.<MethodDescription>any())
                                .intercept(MethodDelegation.to(MethodInterceptor.class)))
                .installOn(inst);
    }

    public static ElementMatcher<? super TypeDescription> buildMatch() {
        ElementMatcher.Junction judge = new AbstractJunction<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return true;
            }
        };

        judge.and(not(isInterface()))
                .and(not(isSetter()))
//                .and(nameContainsIgnoreCase("io.spring"))
                .and(not(nameContainsIgnoreCase("util")))
                .and(not(nameContainsIgnoreCase("interceptor")))
                .and(not(nameContainsIgnoreCase("CGLIB")))
                .and(not(nameContainsIgnoreCase("toString")))
                .and(not(nameContainsIgnoreCase("equals")))
                .and(nameMatches("com.guo.springboot.controller.*|com.guo.springboot.service.*"));

        return new ProtectiveShieldMatcher<>(judge);
    }
}
