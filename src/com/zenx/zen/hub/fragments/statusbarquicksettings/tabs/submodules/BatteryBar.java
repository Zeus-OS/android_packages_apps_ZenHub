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
package com.zenx.zen.hub.fragments.statusbarquicksettings.tabs.submodules;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.preference.*;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto; 
import com.android.settings.SettingsPreferenceFragment;
import com.zenx.support.preferences.SystemSettingSeekBarPreference;

import com.zenx.zen.hub.R;

public class BatteryBar extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "BatteryBar";
    private static final String KEY_THICKNESS = "statusbar_battery_bar_thickness";

    private SystemSettingSeekBarPreference mThickness;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.battery_bar);

        mThickness = (SystemSettingSeekBarPreference) findPreference(KEY_THICKNESS);
        int thickness = Settings.System.getIntForUser(resolver,
                Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, 0, UserHandle.USER_CURRENT);
        mThickness.setValue(thickness);
        mThickness.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mThickness) {
            int delay = (Integer) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, delay, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }
}
