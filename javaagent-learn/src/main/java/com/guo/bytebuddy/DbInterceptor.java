package com.guo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Date: 2021/2/4 15:32
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class DbInterceptor {
//    public static String intercept1(String name) { return "String"; }
//    public static String intercept1(int i) { return "int"; }
//    public static String intercept1(Object o) { return "Object";}

    /**
     * @RuntimeType 注解：告诉 Byte Buddy 不要进行严格的参数类型检测，在参数匹配失败时，尝试使用类型转换方式（runtime type casting）进行类型转换，匹配相应方法。
     * **@This 注解：**注入被拦截的目标对象（即前面示例的 DB 对象）。
     * @AllArguments 注解：注入目标方法的全部参数，是不是感觉与 Java 反射的那套 API 有点类似了？
     * @Origin 注解：注入目标方法对应的 Method 对象。如果拦截的是字段的话，该注解应该标注到 Field 类型参数。
     * @Super 注解：注入目标对象。通过该对象可以调用目标对象的所有方法。
     * @SuperCall：这个注解比较特殊，我们要在 intercept() 方法中调用目标方法的话，需要通过这种方式注入，与 Spring AOP 中的 ProceedingJoinPoint.proceed() 方法有点类似，需要注意的是，这里不能修改调用参数，从上面的示例的调用也能看出来，参数不用单独传递，都包含在其中了。另外，@SuperCall 注解还可以修饰 Runnable 类型的参数，只不过目标方法的返回值就拿不到了。
     */
    @RuntimeType
    public static Object intercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> zuper, @Origin Method method, @Super DB db) throws Exception {
        System.out.println(obj);
        System.out.println(db);
        return zuper.call();
    }
}
