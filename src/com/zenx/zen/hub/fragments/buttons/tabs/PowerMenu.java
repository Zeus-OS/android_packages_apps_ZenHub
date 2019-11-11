/*
 * Copyright (C) 2020 zenx-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zenx.zen.hub.fragments.buttons.tabs;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.widget.Toast;
import android.app.Activity;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.zenx.zen.hub.preferences.Utils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class PowerMenu extends SettingsPreferenceFragment     
                implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "PowerMenu";

    private static final String KEY_POWERMENU_TORCH = "powermenu_torch";
    private static final String KEY_POWERMENU_LOCKSCREEN = "powermenu_lockscreen";
    private static final String KEY_POWERMENU_LS_REBOOT = "powermenu_ls_reboot";
    private static final String KEY_POWERMENU_LS_ADVANCED_REBOOT = "powermenu_ls_advanced_reboot";
    private static final String KEY_POWERMENU_LS_SCREENSHOT = "powermenu_ls_screenshot";
    private static final String KEY_POWERMENU_LS_SCREENRECORD = "powermenu_ls_screenrecord";
    private static final String KEY_POWERMENU_LS_TORCH = "powermenu_ls_torch";
    private static final String TORCH_POWER_BUTTON_GESTURE = "torch_power_button_gesture";

    private ListPreference mTorchPowerButton;

    private SwitchPreference mPowermenuTorch;
    private SwitchPreference mPowerMenuLockscreen;
    private SwitchPreference mPowerMenuReboot;
    private SwitchPreference mPowerMenuAdvancedReboot;
    private SwitchPreference mPowerMenuScreenshot;
    private SwitchPreference mPowerMenuScreenrecord;
    private SwitchPreference mPowerMenuLSTorch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu);
		
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mPowermenuTorch = (SwitchPreference) findPreference(KEY_POWERMENU_TORCH);
        mPowermenuTorch.setOnPreferenceChangeListener(this);
        if (!Utils.deviceSupportsFlashLight(getActivity())) {
            prefScreen.removePreference(mPowermenuTorch);
            prefScreen.removePreference(mPowerMenuLSTorch);
        } else {
        mPowermenuTorch.setChecked((Settings.System.getInt(resolver,
                Settings.System.POWERMENU_TORCH, 0) == 1));
        }

        mPowerMenuLockscreen = (SwitchPreference) findPreference(KEY_POWERMENU_LOCKSCREEN);
        mPowerMenuLockscreen.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LOCKSCREEN, 1) == 1));
        mPowerMenuLockscreen.setOnPreferenceChangeListener(this);

        mPowerMenuReboot = (SwitchPreference) findPreference(KEY_POWERMENU_LS_REBOOT);
        mPowerMenuReboot.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LS_REBOOT, 1) == 1));
        mPowerMenuReboot.setOnPreferenceChangeListener(this);

        mPowerMenuAdvancedReboot = (SwitchPreference) findPreference(KEY_POWERMENU_LS_ADVANCED_REBOOT);
        mPowerMenuAdvancedReboot.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LS_ADVANCED_REBOOT, 0) == 1));
        mPowerMenuAdvancedReboot.setOnPreferenceChangeListener(this);

        mPowerMenuScreenshot = (SwitchPreference) findPreference(KEY_POWERMENU_LS_SCREENSHOT);
        mPowerMenuScreenshot.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LS_SCREENSHOT, 0) == 1));
        mPowerMenuScreenshot.setOnPreferenceChangeListener(this);

        mPowerMenuScreenrecord = (SwitchPreference) findPreference(KEY_POWERMENU_LS_SCREENRECORD);
        mPowerMenuScreenrecord.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LS_SCREENRECORD, 0) == 1));
        mPowerMenuScreenrecord.setOnPreferenceChangeListener(this);

        mPowerMenuLSTorch = (SwitchPreference) findPreference(KEY_POWERMENU_LS_TORCH);
        mPowerMenuLSTorch.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWERMENU_LS_TORCH, 0) == 1));
        mPowerMenuLSTorch.setOnPreferenceChangeListener(this);

            // screen off torch
        mTorchPowerButton = (ListPreference) findPreference(TORCH_POWER_BUTTON_GESTURE);
        int mTorchPowerButtonValue = Settings.Secure.getInt(resolver,
                Settings.Secure.TORCH_POWER_BUTTON_GESTURE, 0);
        mTorchPowerButton.setValue(Integer.toString(mTorchPowerButtonValue));
        mTorchPowerButton.setSummary(mTorchPowerButton.getEntry());
        mTorchPowerButton.setOnPreferenceChangeListener(this);

        updateLockscreen();
    }

    private ListPreference initActionList(String key, int value) {
        ListPreference list = (ListPreference) getPreferenceScreen().findPreference(key);
        list.setValue(Integer.toString(value));
        list.setSummary(list.getEntry());
        list.setOnPreferenceChangeListener(this);
        return list;
    }

    private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getContentResolver(), setting, Integer.valueOf(value));
    }
	
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
         boolean DoubleTapPowerGesture = Settings.Secure.getInt(resolver,
                    Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, 1) == 0;
        if (preference == mTorchPowerButton) {
            int mTorchPowerButtonValue = Integer.valueOf((String) newValue);
            int index = mTorchPowerButton.findIndexOfValue((String) newValue);
            mTorchPowerButton.setSummary(
                    mTorchPowerButton.getEntries()[index]);
            Settings.Secure.putInt(resolver, Settings.Secure.TORCH_POWER_BUTTON_GESTURE,
                    mTorchPowerButtonValue);
            if (mTorchPowerButtonValue == 1 && DoubleTapPowerGesture) {
                //if doubletap for torch is enabled, switch off double tap for camera
                Settings.Secure.putInt(resolver, Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED,
                        1);
                Toast.makeText(getActivity(),
                    (R.string.torch_power_button_gesture_dt_toast),
                    Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (preference == mPowermenuTorch) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_TORCH, value ? 1 : 0);
            return true;
        } else if (preference == mPowerMenuLockscreen) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LOCKSCREEN, value ? 1 : 0);
            updateLockscreen();
            return true;
        } else if (preference == mPowerMenuReboot) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LS_REBOOT, value ? 1 : 0);
            return true;
        } else if (preference == mPowerMenuAdvancedReboot) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LS_ADVANCED_REBOOT, value ? 1 : 0);
            return true;
        } else if (preference == mPowerMenuScreenshot) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LS_SCREENSHOT, value ? 1 : 0);
            return true;
        } else if (preference == mPowerMenuScreenrecord) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LS_SCREENRECORD, value ? 1 : 0);
            return true;
        } else if (preference == mPowerMenuLSTorch) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_LS_TORCH, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }

    private void updateLockscreen() {
        boolean lockscreenOptions = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.POWERMENU_LOCKSCREEN, 1) == 1;

        if (lockscreenOptions) {
            mPowerMenuReboot.setEnabled(true);
            mPowerMenuAdvancedReboot.setEnabled(true);
            mPowerMenuScreenshot.setEnabled(true);
            mPowerMenuScreenrecord.setEnabled(true);
            mPowerMenuLSTorch.setEnabled(true);
        } else {
            mPowerMenuReboot.setEnabled(false);
            mPowerMenuAdvancedReboot.setEnabled(false);
            mPowerMenuScreenshot.setEnabled(false);
            mPowerMenuScreenrecord.setEnabled(false);
            mPowerMenuLSTorch.setEnabled(false);
        }
    }
}
