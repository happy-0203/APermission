package com.jy.lib_permission.annotation;

import com.jy.lib_permission.utils.PermissionUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {

    String[] value();
    int requestCode() default PermissionUtil.REQUEST_CODE;
}
