package com.zego.liveroomplayground.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zego.liveroomplayground.R;
import com.zego.liveroomplayground.databinding.ActivityVersionBinding;
import com.zego.videocommunication.ui.PublishStreamAndPlayStreamUI;
import com.zego.zegoliveroom.ZegoLiveRoom;

public class VersionActivity extends AppCompatActivity {

    private ActivityVersionBinding binding;

    private String veVersion = "VE 版本：";
    private String sdkVersion = "SDK 版本：";
    private String demoVersion = "Demo 版本：";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_version);

        binding.txVeVersion.setText(getVeVersion());
        binding.txSdkVersion.setText(getSdkVersion());
        binding.txDemoVersion.setText(demoVersion +getLocalVersionName(this));

        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 获取 VE 版本
    public String getVeVersion() {

        return veVersion+ZegoLiveRoom.version2();
    }

    // 获取 SDK 版本
    public String getSdkVersion() {
        return sdkVersion+ZegoLiveRoom.version();
    }

    /**
     * 供其他Activity调用，进入当前Activity查看版本
     *
     * @param activity
     */
    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, VersionActivity.class);

        activity.startActivity(intent);
    }

    public String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("liveroomplayground","not found package name" + e.getMessage());

        }

        return localVersion;
    }
}