/*
 * Copyright (C) 2020 Zenx-OS
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
package com.zenx.zen.hub.fragments.lockscreenambient.tabs;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceScreen;

import com.android.internal.custom.app.LineageContextConstants;
import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.zenx.support.preferences.SystemSettingSeekBarPreference;

import com.zenx.support.preferences.SystemSettingMasterSwitchPreference;
import com.zenx.support.preferences.SecureSettingMasterSwitchPreference;
import com.zenx.support.preferences.CustomSeekBarPreference;
import com.zenx.support.colorpicker.ColorPickerPreference;
import com.zenx.support.preferences.SystemSettingSwitchPreference;

public class LockScreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String LOCKSCREEN_VISUALIZER_ENABLED = "lockscreen_visualizer_enabled";
    private static final String SYSUI_KEYGUARD_SHOW_BATTERY_BAR = "sysui_keyguard_show_battery_bar";
    private static final String FOD_ANIMATION_PREF = "fod_recognizing_animation";

    private static final int DEFAULT_COLOR = 0xffffffff;

    private SecureSettingMasterSwitchPreference mVisualizerEnabled;
    private CustomSeekBarPreference mMaxKeyguardNotifConfig;
    private SystemSettingMasterSwitchPreference mLsBatteryBar;
    private SystemSettingSwitchPreference mFODAnimationEnabled;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.zen_hub_lockscreen);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
        Resources resources = getResources();

        mLsBatteryBar = (SystemSettingMasterSwitchPreference) findPreference(SYSUI_KEYGUARD_SHOW_BATTERY_BAR);
        mLsBatteryBar.setOnPreferenceChangeListener(this);
        int lsBatteryBar = Settings.System.getInt(resolver,
                SYSUI_KEYGUARD_SHOW_BATTERY_BAR, 1);
        mLsBatteryBar.setChecked(lsBatteryBar != 0);
        PackageManager packageManager = getPackageManager();

        mFODAnimationEnabled = (SystemSettingSwitchPreference) findPreference(FOD_ANIMATION_PREF);
        if (!packageManager.hasSystemFeature(LineageContextConstants.Features.FOD)) {
            mFODAnimationEnabled.setVisible(false);
        }

        updateMasterPrefs();
    }

    private void updateMasterPrefs() {
        mVisualizerEnabled = (SecureSettingMasterSwitchPreference) findPreference(LOCKSCREEN_VISUALIZER_ENABLED);
        mVisualizerEnabled.setOnPreferenceChangeListener(this);
        int visualizerEnabled = Settings.Secure.getInt(getActivity().getContentResolver(),
                LOCKSCREEN_VISUALIZER_ENABLED, 0);
        mVisualizerEnabled.setChecked(visualizerEnabled != 0);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mVisualizerEnabled) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(),
		            LOCKSCREEN_VISUALIZER_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mLsBatteryBar) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
		            SYSUI_KEYGUARD_SHOW_BATTERY_BAR, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMasterPrefs();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateMasterPrefs();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }
}
