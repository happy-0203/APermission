package com.jy.lib_permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jy.lib_permission.core.IPermission;
import com.jy.lib_permission.utils.PermissionUtil;

public class PermissionActivity extends AppCompatActivity {

    private static final String REQUEST_PERMISSION = "request_permission";
    private static final String REQUEST_PERMISSION_CODE = "request_permission_code";
    private int mRequestCode;
    private String[] mPermissions;

    private static IPermission sIPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra(REQUEST_PERMISSION_CODE, -1);
        mPermissions = intent.getStringArrayExtra(REQUEST_PERMISSION);

        if (mPermissions == null || mPermissions.length == 0 || sIPermission == null) {
            finish();
        }

        if (PermissionUtil.hasPermission(this, mPermissions)) {
            sIPermission.granted();
            finish();
        } else {
            //请求权限
            ActivityCompat.requestPermissions(this, mPermissions, mRequestCode);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.verifyPermission(grantResults)) {
            sIPermission.granted();
            finish();
            return;
        }

        //用户点击了不再显示,权限拒绝
        if (!PermissionUtil.shouldShowRequestPermissionRationale(this, permissions)) {
            sIPermission.denied();
            finish();
            return;
        }

        //用户取消
        sIPermission.canceled();
        finish();


    }

    /**
     * 打开权限请求的Activity
     *
     * @param context
     * @param permissions
     * @param requestCode
     * @param iPermission
     */
    public static void requestPermission(Context context, String[] permissions, int requestCode, IPermission iPermission) {

        sIPermission = iPermission;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt(REQUEST_PERMISSION_CODE, requestCode);
        bundle.putStringArray(REQUEST_PERMISSION, permissions);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }


    @Override
    public void finish() {
        super.finish();
        //关闭默认的动画
        overridePendingTransition(0, 0);
    }
}
