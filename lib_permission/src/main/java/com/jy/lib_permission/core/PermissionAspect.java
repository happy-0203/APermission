package com.jy.lib_permission.core;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.jy.lib_permission.PermissionActivity;
import com.jy.lib_permission.annotation.Permission;
import com.jy.lib_permission.annotation.PermissionCancled;
import com.jy.lib_permission.annotation.PermissionDenied;
import com.jy.lib_permission.utils.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect {

    private static final String TAG = PermissionAspect.class.getSimpleName();

    @Pointcut("execution(@com.jy.lib_permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission){

    }

    @Around("requestPermission(permission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, Permission permission){
        final Object object = joinPoint.getThis();

        Context context = null;
        if (object instanceof Context){
            context = (Context) object;
        }else if (object instanceof Fragment){
            context = ((Fragment) object).getActivity();
        }else if (object instanceof android.app.Fragment){
            context = ((android.app.Fragment) object).getActivity();
        }

        if (context == null || permission == null){
            Log.e(TAG, "aroundJointPoint is error");
            return;
        }

        //打开Activity请求权限
        final Context finalContext = context;
        PermissionActivity.requestPermission(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void granted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void canceled() {
                PermissionUtil.invokeAnnotation(object, PermissionCancled.class);
            }

            @Override
            public void denied() {
                PermissionUtil.invokeAnnotation(object, PermissionDenied.class);

                //弹框提示

                PermissionUtil.getToSetting(finalContext);
            }
        });


    }
}
