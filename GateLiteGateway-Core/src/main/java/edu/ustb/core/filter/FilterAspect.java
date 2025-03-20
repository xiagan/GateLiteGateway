package edu.ustb.core.filter;

import java.lang.annotation.*;

/**
 * @author: ljr-YingWu
 * @date: 2023/10/30 9:44
 * FilterAspect注解
 * 提供切面增强功能
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FilterAspect {
    /**
     * 过滤器ID
     * @return
     */
    String id();

    /**
     * 过滤器名称
     * @return
     */
    String name() default "";

    /**
     * 排序
     * @return
     */
    int order() default 0;

}
