package com.guo.trace;

import net.bytebuddy.matcher.ElementMatcher;

/**
 * @Date: 2021/2/8 11:39
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class ProtectiveShieldMatcher<T> extends ElementMatcher.Junction.AbstractBase<T> {

    private final ElementMatcher<? super T> matcher;

    public ProtectiveShieldMatcher(ElementMatcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(T target) {
        try {
            return this.matcher.matches(target);
        } catch (Exception exception) {
            return false;
        }
    }
}
