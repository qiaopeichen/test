package com.example.a55_city_receive_rice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class VillagerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        //1 获取发送广播携带的数据
        String content = getResultData();
        //2 显示结果
        Toast.makeText(context, "农民：" + content, Toast.LENGTH_SHORT).show();
    }
}
