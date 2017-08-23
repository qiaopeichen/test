package com.itheima.mobilesafe74.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;
import com.itheima.mobilesafe74.view.SettingItemView;

public class Setup2Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }

    private void initUI() {
        final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        // 1.回显（读取已有的绑定状态，用作显示，sp中是否存储了sim卡的序列号）
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        // 2.判断是否序列卡号为""
        if (TextUtils.isEmpty(sim_number)){
            siv_sim_bound.setCheck(false);
        } else {
            siv_sim_bound.setCheck(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 动态获取运行时权限
                if (Build.VERSION.SDK_INT >= 23) {
                    int REQUEST_CODE_CONTACT = 101;
                    String[] permissions = {Manifest.permission.READ_PHONE_STATE};
                    //验证是否许可权限
                    for (String str : permissions) {
                        if (ContextCompat.checkSelfPermission(Setup2Activity.this, str) != PackageManager.PERMISSION_GRANTED) {
                            //申请权限
                            ActivityCompat.requestPermissions(Setup2Activity.this, permissions, REQUEST_CODE_CONTACT);
                            return;
                        }
                    }
                }

                // 3.获取原有的状态
                boolean isCheak = siv_sim_bound.isCheck();
                // 4.将原有状态取反
                // 5.状态设置给当前条目
                siv_sim_bound.setCheck(!isCheak);
                if (!isCheak) { //if(true)走此方法，if(false)走else方法
                    // 6.存储（序列卡号）
                    // 6.1 获取sim卡序列号TelephoneManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    // 6.2 获取sim卡的序列卡号
                    String simSerialNumber = manager.getSimSerialNumber();
                    // 6.3 存储
                    SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
                } else {
                    // 7.将存储序列卡号的节点，从sp中删除掉
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    public void nextPage(View v) {
        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(serialNumber)) {
            Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请绑定sim卡");
        }
    }

    public void prePage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

}