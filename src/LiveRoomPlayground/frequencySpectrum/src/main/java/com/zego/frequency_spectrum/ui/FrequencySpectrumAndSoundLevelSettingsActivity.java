package com.zego.frequency_spectrum.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zego.frequency_spectrum.ZGFrequencySpectrumAndSoundLevelHelper;
import com.zego.zegoavkit2.frequencyspectrum.ZegoFrequencySpectrumMonitor;


import com.zego.zegoavkit2.soundlevel.ZegoSoundLevelMonitor;

import com.zego.common.ui.BaseActivity;
import com.zego.frequency_spectrum.R;


/**
 * 本类为设置页面相关的Activity，没有详细注释，业务可以根据自己的需求设计自己的设置页面,也可根据以下作为参考
 *
 */
public class FrequencySpectrumAndSoundLevelSettingsActivity extends BaseActivity {

    Switch sw_frequency_spectrum;
    Switch sw_sound_level;
    TextView tv_frequency_spectrum_current_monitor_cycle;
    TextView tv_sound_level_current_monitor_cycle;
    SeekBar sb_frequency_spectrum_monitor_cycle_settings;
    SeekBar sb_sound_level_monitor_cycle_settings;

    SharedPreferences sp;
    SharedPreferences.Editor edit;

    Boolean last_frequency_spectrum_monitor_state;
    Boolean last_sound_level_monitor_state;
    Integer last_frequency_spectrum_monitor_circle;
    Integer last_sound_level_monitor_circle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frequency_spectrum_sound_level_settings);

        sw_frequency_spectrum = findViewById(R.id.sw_frequency_spectrum);
        sw_sound_level = findViewById(R.id.sw_sound_level);
        tv_frequency_spectrum_current_monitor_cycle =  findViewById(R.id.tv_frequency_spectrum_current_monitor_cycle);
        tv_sound_level_current_monitor_cycle = findViewById(R.id.tv_sound_level_current_monitor_cycle);
        sb_frequency_spectrum_monitor_cycle_settings = findViewById(R.id.sb_frequency_spectrum_monitor_cycle_settings);
        sb_sound_level_monitor_cycle_settings = findViewById(R.id.sb_sound_level_monitor_cycle_settings);

        sp = getSharedPreferences("FrequencySpectrumAndSoundLevel", Context.MODE_PRIVATE);

        last_frequency_spectrum_monitor_state = sp.getBoolean("last_frequency_spectrum_state", true);
        last_sound_level_monitor_state = sp.getBoolean("last_sound_level_monitor_state", true);
        last_frequency_spectrum_monitor_circle = sp.getInt("last_frequency_spectrum_monitor_circle", 500);
        last_sound_level_monitor_circle = sp.getInt("last_sound_level_monitor_circle", 200);

        sw_frequency_spectrum.setChecked(last_frequency_spectrum_monitor_state);
        sw_sound_level.setChecked(last_sound_level_monitor_state);
        tv_frequency_spectrum_current_monitor_cycle.setText(last_frequency_spectrum_monitor_circle+"ms");
        tv_sound_level_current_monitor_cycle.setText(last_sound_level_monitor_circle+"ms");

        sb_frequency_spectrum_monitor_cycle_settings.setProgress(last_frequency_spectrum_monitor_circle);
        sb_sound_level_monitor_cycle_settings.setProgress(last_sound_level_monitor_circle);
        sb_frequency_spectrum_monitor_cycle_settings.setEnabled(last_frequency_spectrum_monitor_state);
        sb_sound_level_monitor_cycle_settings.setEnabled(last_sound_level_monitor_state);

        sb_frequency_spectrum_monitor_cycle_settings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 不同手机的SeekBar控件表现不一样，一些手机不能拖到顶部，使用取巧的方式解决
                if(progress >= 2990){
                    tv_frequency_spectrum_current_monitor_cycle.setText(3000+"ms");
                    last_frequency_spectrum_monitor_circle = 3000;
                    seekBar.setProgress(3000);


                }else {
                    tv_frequency_spectrum_current_monitor_cycle.setText(progress + "ms");
                    last_frequency_spectrum_monitor_circle = progress;

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                ZGFrequencySpectrumAndSoundLevelHelper.modifyFrequencySpectrumMonitorCycle(seekBar.getProgress());

            }
        });

        sb_sound_level_monitor_cycle_settings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 不同手机的SeekBar控件表现不一样，一些手机不能拖到顶部，使用取巧的方式解决
                if(progress == 3000){
                    tv_sound_level_current_monitor_cycle.setText(progress + "ms");
                    last_sound_level_monitor_circle = progress;
                }
                else if(progress>=2800 && fromUser == true){


                    tv_sound_level_current_monitor_cycle.setText(2800+2*(progress-2800)+"ms");
                    last_sound_level_monitor_circle = 2800+2*(progress-2800);
                    seekBar.setProgress(2800+2*(progress-2800));

                }
                else {
                    tv_sound_level_current_monitor_cycle.setText(progress + "ms");
                    last_sound_level_monitor_circle = progress;

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                ZGFrequencySpectrumAndSoundLevelHelper.modifySoundLevelMonitorCycle(seekBar.getProgress());
            }
        });

        sw_frequency_spectrum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(true == isChecked){
                    ZegoFrequencySpectrumMonitor.getInstance().start();
                    sb_frequency_spectrum_monitor_cycle_settings.setEnabled(true);
                    last_frequency_spectrum_monitor_state = true;

                }else {
                    ZegoFrequencySpectrumMonitor.getInstance().stop();
                    sb_frequency_spectrum_monitor_cycle_settings.setEnabled(false);
                    last_frequency_spectrum_monitor_state = false;

                }


            }
        });
        sw_sound_level.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(true == isChecked){
                    ZegoSoundLevelMonitor.getInstance().start();
                    sb_sound_level_monitor_cycle_settings.setEnabled(true);
                    last_sound_level_monitor_state = true;
                }else {
                    ZegoSoundLevelMonitor.getInstance().stop();
                    sb_sound_level_monitor_cycle_settings.setEnabled(false);
                    last_sound_level_monitor_state = false;
                }


            }
        });



    }


    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, FrequencySpectrumAndSoundLevelSettingsActivity.class);
        activity.startActivity(intent);
    }

    public void goBackToFrequencySpectrumRoomActivity(View view){

        finish();

    }

    @Override
    protected void onStop(){

        super.onStop();

        edit = sp.edit();

        edit.putBoolean("last_frequency_spectrum_state", last_frequency_spectrum_monitor_state);
        edit.putBoolean("last_sound_level_monitor_state", last_sound_level_monitor_state);
        edit.putInt("last_frequency_spectrum_monitor_circle", last_frequency_spectrum_monitor_circle);
        edit.putInt("last_sound_level_monitor_circle", last_sound_level_monitor_circle);

        edit.commit();

    }

}
