package com.ofek2608.dnd.resources;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Res {
	String name() default "";
	Class<?> mapKind() default Object.class;
}
