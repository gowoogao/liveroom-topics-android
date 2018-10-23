package com.zego.playground.demo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zego.mediaplayer.ui.MediaPlayerDemoUI;
import com.zego.mediaplayer.ui.MediaPlayerResourcesList;
import com.zego.playground.demo.R;
import com.zego.playground.demo.adapter.MainAdapter;
import com.zego.playground.demo.databinding.ActivityMainBinding;
import com.zego.playground.demo.entity.ModuleInfo;


public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter = new MainAdapter();
    private static final int REQUEST_PERMISSION_CODE = 101;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ZegoDemo");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainAdapter.setOnItemClickListener((view, position) -> {
            boolean orRequestPermission = checkOrRequestPermission(REQUEST_PERMISSION_CODE);
            ModuleInfo moduleInfo = (ModuleInfo) view.getTag();
            if (orRequestPermission) {
                switch (moduleInfo.getModule()){
                    case "mediaPlayer":
                        Intent intent = new Intent(MainActivity.this, MediaPlayerResourcesList.class);
                        MainActivity.this.startActivity(intent);
                        break;
                }
            }
        });


        binding.moduleList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false));
        // 设置adapter
        binding.moduleList.setAdapter(mainAdapter);
        // 设置Item添加和移除的动画
        binding.moduleList.setItemAnimator(new DefaultItemAnimator());

        // 添加模块
        mainAdapter.addModuleInfo(new ModuleInfo().moduleName("mediaPlayer"));
    }

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE", Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};


    private boolean checkOrRequestPermission(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, code);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Runtime.getRuntime().gc();
    }
}
