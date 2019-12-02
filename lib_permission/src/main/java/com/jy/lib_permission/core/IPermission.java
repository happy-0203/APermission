package com.jy.lib_permission.core;

import android.content.pm.PackageManager;

public interface IPermission {

    /**
     * 权限同意
     */
    void granted();

    /**
     * 权限取消
     */
    void canceled();

    /**
     * 权限拒绝
     */
    void denied();

}
