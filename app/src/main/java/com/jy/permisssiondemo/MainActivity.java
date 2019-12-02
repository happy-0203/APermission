package com.jy.permisssiondemo;

import android.Manifest;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jy.lib_permission.annotation.Permission;
import com.jy.lib_permission.annotation.PermissionCancled;
import com.jy.lib_permission.annotation.PermissionDenied;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //多个权限
        findViewById(R.id.btn_more_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMorePermissions();
            }
        });

        //单个权限
        findViewById(R.id.btn_one_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOnePermission();
            }
        });
    }


    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    public void requestMorePermissions(){
        Toast.makeText(this,"请求权限成功",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancled(requestCode = 1000)
    public void requestMorePermissionCancle(){
        Toast.makeText(this,"权限取消",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(requestCode = 1001)
    public void requestMorePermissionDenied(){
        Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
    }



    @Permission(Manifest.permission.READ_PHONE_STATE)
    public void requestOnePermission(){
        Toast.makeText(this,"请求权限成功",Toast.LENGTH_SHORT).show();
    }

    @PermissionCancled
    public void requestOnePermissionCancle(){
        Toast.makeText(this,"权限取消",Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void requestOnePermissionDenied(){
        Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
    }

}
