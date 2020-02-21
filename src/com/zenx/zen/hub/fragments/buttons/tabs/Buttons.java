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
package com.zenx.zen.hub.fragments.buttons.tabs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.widget.Toast;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto; 
import com.android.internal.util.zenx.Utils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.zenx.support.preferences.SystemSettingSwitchPreference;

public class Buttons extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Buttons";

    private static final String NAV_BAR_LAYOUT = "nav_bar_layout";
    private static final String SYSUI_NAV_BAR = "sysui_nav_bar";
    private static final String KEY_NAVIGATION_BAR_ENABLED = "force_show_navbar";
    private static final String KEY_NAVIGATION_BAR_ARROWS = "navigation_bar_menu_arrow_keys";
    private static final String KEY_SWAP_NAVIGATION_KEYS = "swap_navigation_keys";

    private ContentResolver resolver;
    private ListPreference mNavBarLayout;
    private SwitchPreference mNavigationBar;
    private SystemSettingSwitchPreference mNavigationArrows;
    private SystemSettingSwitchPreference mSwapHardwareKeys;

    private int deviceKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.zen_hub_buttons);
        resolver = getActivity().getContentResolver();
        final PreferenceScreen prefSet = getPreferenceScreen();

        mNavBarLayout = (ListPreference) findPreference(NAV_BAR_LAYOUT);
        mNavBarLayout.setOnPreferenceChangeListener(this);
        String navBarLayoutValue = Settings.Secure.getString(resolver, SYSUI_NAV_BAR);
        if (navBarLayoutValue != null) {
            mNavBarLayout.setValue(navBarLayoutValue);
        } else {
            mNavBarLayout.setValueIndex(0);
        }

        if (!Utils.isThemeEnabled("com.android.internal.systemui.navbar.threebutton")) {
            prefSet.removePreference(mNavBarLayout);
        }

        final boolean defaultToNavigationBar = getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar);
        final boolean navigationBarEnabled = Settings.System.getIntForUser(
                resolver, Settings.System.FORCE_SHOW_NAVBAR,
                defaultToNavigationBar ? 1 : 0, UserHandle.USER_CURRENT) != 0;

        deviceKeys = getResources().getInteger(
                com.android.internal.R.integer.config_deviceHardwareKeys);

        mNavigationBar = (SwitchPreference) findPreference(KEY_NAVIGATION_BAR_ENABLED);
        mNavigationBar.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.FORCE_SHOW_NAVBAR,
                defaultToNavigationBar ? 1 : 0) == 1));
        mNavigationBar.setOnPreferenceChangeListener(this);

        mNavigationArrows = (SystemSettingSwitchPreference) findPreference(KEY_NAVIGATION_BAR_ARROWS);
        if (Utils.isThemeEnabled("com.android.internal.systemui.navbar.gestural_nopill")) {
            prefSet.removePreference(mNavigationArrows);
        }

        mSwapHardwareKeys = (SystemSettingSwitchPreference) findPreference(KEY_SWAP_NAVIGATION_KEYS);

        if (deviceKeys == 0) {
            prefSet.removePreference(mSwapHardwareKeys);
        }
        updateOptions();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        resolver = getActivity().getContentResolver();
        boolean DoubleTapPowerGesture = Settings.Secure.getInt(resolver,
                    Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, 1) == 0;
        if (preference == mNavBarLayout) {
            Settings.Secure.putString(resolver, SYSUI_NAV_BAR, (String) objValue);
            return true;
        } else if (preference == mNavigationBar) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.FORCE_SHOW_NAVBAR, value ? 1 : 0);
            updateOptions();
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOptions();

    }

    @Override
    public void onPause() {
        super.onPause();
        updateOptions();
    }

    private void updateOptions() {
        resolver = getActivity().getContentResolver();
        boolean defaultToNavigationBar = getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar);
        boolean navigationBarEnabled = Settings.System.getIntForUser(
                resolver, Settings.System.FORCE_SHOW_NAVBAR,
                defaultToNavigationBar ? 1 : 0, UserHandle.USER_CURRENT) != 0;

        if (navigationBarEnabled) {
            mSwapHardwareKeys.setEnabled(false);
        } else {
            mSwapHardwareKeys.setEnabled(true);
        }
    }
}