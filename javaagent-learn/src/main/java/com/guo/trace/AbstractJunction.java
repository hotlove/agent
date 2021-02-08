package com.guo.trace;

import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Date: 2021/2/8 11:42
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class AbstractJunction<V> implements ElementMatcher.Junction<V> {
    @Override
    public <U extends V> Junction<U> and(ElementMatcher<? super U> other) {
        return new Conjunction<>(this, other);
    }

    @Override
    public <U extends V> Junction<U> or(ElementMatcher<? super U> other) {
        return new Disjunction<>(this, other);
    }

    @Override
    public boolean matches(V target) {
        return true;
    }
}
