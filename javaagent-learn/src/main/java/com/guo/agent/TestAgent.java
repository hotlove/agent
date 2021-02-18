package com.guo.agent;

import com.guo.bytebuddy.DbInterceptor2;
import com.guo.bytebuddy.OverrideCallable;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @Date: 2021/2/2 17:19
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class TestAgent {

    public static void premain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
//        System.out.println("this is a java agent with two args");
//        System.out.println("参数:" + agentArgs + "\n");

        //注册一个Transformer，该Transformer在类加载时被调用
//        inst.addTransformer(new Transformer(), true);
//        inst.retransformClasses(TestClass.class);
//        System.out.println("premain done");

        // byte buddy
        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {

                return builder
                        .method(ElementMatchers.<MethodDescription>any())
                        .intercept(MethodDelegation.to(TimerInterceptor.class));
            }
        };
        System.out.println("探针开始========================");

        //
        // "(com.badu.bdsaas.baseinfo.service.*|com.badu.btsaas.controller.*)"
        new AgentBuilder
                .Default()
                .type(ElementMatchers.<TypeDescription>nameMatches("(com.guo.springboot.controller.*|com.guo.springboot.service.*)"))
                .transform(transformer)
                .installOn(inst);
    }

    public static void main(String[] args) {
        System.out.println("this is a java agent only one args");
        System.out.println("参数:" + args + "\n");
    }
}
