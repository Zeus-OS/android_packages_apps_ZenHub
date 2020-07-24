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
package com.zenx.zen.hub.fragments.statusbarquicksettings.tabs;

import static com.zenx.zen.hub.utils.Utils.handleOverlays;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.*;
import android.provider.Settings;
import com.android.internal.util.zenx.Utils;
import com.android.internal.util.zenx.ThemesUtils;
import android.content.res.Resources;
import android.content.om.IOverlayManager;
import android.os.ServiceManager;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;

import com.zenx.zen.hub.R;
import com.zenx.support.preferences.CustomSeekBarPreference;
import com.zenx.support.preferences.SystemSettingSeekBarPreference;
import com.zenx.support.colorpicker.ColorPickerPreference;
import com.zenx.support.preferences.SystemSettingMasterSwitchPreference;
import com.zenx.support.preferences.SystemSettingSwitchPreference;
import com.zenx.support.preferences.SystemSettingListPreference;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String QS_PANEL_ALPHA = "qs_panel_alpha";
    private static final String QS_HEADER_CLOCK_SIZE  = "qs_header_clock_size";
    private static final String QS_HEADER_CLOCK_FONT_STYLE  = "qs_header_clock_font_style";
    public static final String TAG = "QuickSettings";
    private static final String PREF_COLUMNS_PORTRAIT = "qs_columns_portrait";
    private static final String PREF_COLUMNS_LANDSCAPE = "qs_columns_landscape";
    private static final String QUICK_PULLDOWN = "quick_pulldown";
    private static final String PREF_COLUMNS_QUICKBAR = "qs_columns_quickbar";
    private static final String PREF_ROWS_PORTRAIT = "qs_rows_portrait";
    private static final String PREF_ROWS_LANDSCAPE = "qs_rows_landscape";
    private static final String STATUS_BAR_CUSTOM_HEADER = "status_bar_custom_header";
    private static final String QS_BACKGROUND_BLUR = "qs_background_blur";
    private static final String QS_ALWAYS_SHOW_SETTINGS = "qs_always_show_settings";
    private static final String QS_BATTERY_MODE = "qs_battery_mode";
    private static final String QS_SYS_BATTERY_MODE = "qs_sys_battery_mode";
    private static final String QS_HEADER_STYLE = "qs_header_style";
    private static final String QS_HEADER_STYLE_COLOR = "qs_header_style_color";
    private static final String QS_BACKGROUND_STYLE = "qs_background_style";
    private static final String QS_BACKGROUND_STYLE_COLOR = "qs_background_style_color";
    private static final String PREF_TILE_STYLE = "qs_tile_style";
    private static final String PREF_R_NOTIF_HEADER = "notification_headers";

    private CustomSeekBarPreference mQsPanelAlpha;
    private CustomSeekBarPreference mQsClockSize;
    private ListPreference mClockFontStyle;
    private CustomSeekBarPreference mQsColumnsPortrait;
    private CustomSeekBarPreference mQsColumnsLandscape;
    private ListPreference mQuickPulldown;
    private CustomSeekBarPreference mQsColumnsQuickbar;
    private CustomSeekBarPreference mQsRowsPortrait;
    private CustomSeekBarPreference mQsRowsLandscape;
    private SystemSettingMasterSwitchPreference mCustomHeader;
    private SystemSettingMasterSwitchPreference mQsBlur;
    private SystemSettingSwitchPreference mShowAlwaysSettings;
    private SystemSettingListPreference mQsBatteryMode;
    private SystemSettingListPreference mQsSysBatteryMode;
    private ListPreference mQsHeaderStyle;
    private ColorPickerPreference mQsHeaderStyleColor;
    private ListPreference mQsBackgroundStyle;
    private ColorPickerPreference mQsBackgroundStyleColor;
    private ListPreference mQsTileStyle;
    private SystemSettingSwitchPreference mNotifHeader;

    private IOverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.zen_hub_quicksettings);

        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));

        mQsPanelAlpha = (CustomSeekBarPreference) findPreference(QS_PANEL_ALPHA);
        int qsPanelAlpha = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.QS_PANEL_BG_ALPHA, 255, UserHandle.USER_CURRENT);
        mQsPanelAlpha.setValue(qsPanelAlpha);
        mQsPanelAlpha.setOnPreferenceChangeListener(this);

        ContentResolver resolver = getActivity().getContentResolver();

        mQsColumnsQuickbar = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_QUICKBAR);
        int columnsQuickbar = Settings.System.getInt(resolver,
                Settings.System.QS_QUICKBAR_COLUMNS, 6);
        mQsColumnsQuickbar.setValue(columnsQuickbar);
        mQsColumnsQuickbar.setOnPreferenceChangeListener(this);

        mQsClockSize = (CustomSeekBarPreference) findPreference(QS_HEADER_CLOCK_SIZE);
        int qsClockSize = Settings.System.getInt(resolver,
                Settings.System.QS_HEADER_CLOCK_SIZE, 14);
                mQsClockSize.setValue(qsClockSize / 1);
        mQsClockSize.setOnPreferenceChangeListener(this);

        mClockFontStyle = (ListPreference) findPreference(QS_HEADER_CLOCK_FONT_STYLE);
        int showClockFont = Settings.System.getInt(resolver,
                Settings.System.QS_HEADER_CLOCK_FONT_STYLE, 14);
        mClockFontStyle.setValue(String.valueOf(showClockFont));
        mClockFontStyle.setOnPreferenceChangeListener(this);

        mQsColumnsPortrait = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_PORTRAIT);
        int columnsPortrait = Settings.System.getIntForUser(resolver,
                Settings.System.QS_LAYOUT_COLUMNS, 4, UserHandle.USER_CURRENT);
        mQsColumnsPortrait.setValue(columnsPortrait);
        mQsColumnsPortrait.setOnPreferenceChangeListener(this);

        mQsColumnsLandscape = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_LANDSCAPE);
        int columnsLandscape = Settings.System.getIntForUser(resolver,
                Settings.System.QS_LAYOUT_COLUMNS_LANDSCAPE, 4, UserHandle.USER_CURRENT);
        mQsColumnsLandscape.setValue(columnsLandscape);
        mQsColumnsLandscape.setOnPreferenceChangeListener(this);

        mQsRowsPortrait = (CustomSeekBarPreference) findPreference(PREF_ROWS_PORTRAIT);
        int rowsPortrait = Settings.System.getIntForUser(resolver,
                Settings.System.QS_LAYOUT_ROWS, 3, UserHandle.USER_CURRENT);
        mQsRowsPortrait.setValue(rowsPortrait);
        mQsRowsPortrait.setOnPreferenceChangeListener(this);

        mQsRowsLandscape = (CustomSeekBarPreference) findPreference(PREF_ROWS_LANDSCAPE);
        int rowsLandscape = Settings.System.getIntForUser(resolver,
                Settings.System.QS_LAYOUT_ROWS_LANDSCAPE, 2, UserHandle.USER_CURRENT);
        mQsRowsLandscape.setValue(rowsLandscape);
        mQsRowsLandscape.setOnPreferenceChangeListener(this);

        mCustomHeader = (SystemSettingMasterSwitchPreference) findPreference(STATUS_BAR_CUSTOM_HEADER);
        int qsHeader = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0);
        mCustomHeader.setChecked(qsHeader != 0);
        mCustomHeader.setOnPreferenceChangeListener(this);

        boolean isBlurEnabled = Settings.System.getIntForUser(resolver,
                Settings.System.QS_BACKGROUND_BLUR, 1, UserHandle.USER_CURRENT) == 1;
        mQsBlur = (SystemSettingMasterSwitchPreference) findPreference(QS_BACKGROUND_BLUR);
        mQsBlur.setChecked(isBlurEnabled);
        mQsBlur.setOnPreferenceChangeListener(this);

        mShowAlwaysSettings = (SystemSettingSwitchPreference) findPreference(QS_ALWAYS_SHOW_SETTINGS);
        mShowAlwaysSettings.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_ALWAYS_SHOW_SETTINGS, 0) == 1));
        mShowAlwaysSettings.setOnPreferenceChangeListener(this);

        mQsBatteryMode = (SystemSettingListPreference) findPreference(QS_BATTERY_MODE);
        int qsBatteryMode = Settings.System.getIntForUser(resolver,
                Settings.System.QS_BATTERY_MODE, 0, UserHandle.USER_CURRENT);
        mQsBatteryMode.setValue(String.valueOf(qsBatteryMode));
        mQsBatteryMode.setSummary(mQsBatteryMode.getEntry());
        mQsBatteryMode.setOnPreferenceChangeListener(this);

        mQsSysBatteryMode = (SystemSettingListPreference) findPreference(QS_SYS_BATTERY_MODE);
        int qsSysBatteryMode = Settings.System.getIntForUser(resolver,
                Settings.System.QS_SYS_BATTERY_MODE, 0, UserHandle.USER_CURRENT);
        mQsSysBatteryMode.setValue(String.valueOf(qsSysBatteryMode));
        mQsSysBatteryMode.setSummary(mQsSysBatteryMode.getEntry());
        mQsSysBatteryMode.setOnPreferenceChangeListener(this);

        mQsTileStyle = (ListPreference) findPreference(PREF_TILE_STYLE);
        int qsTileStyle = Settings.System.getInt(resolver,
                Settings.System.QS_TILE_STYLE, 0);
        int qsTileStyleValue = getOverlayPosition(ThemesUtils.QS_TILE_THEMES);
        if (qsTileStyleValue != 0) {
            mQsTileStyle.setValue(String.valueOf(qsTileStyle));
        }
        mQsTileStyle.setSummary(mQsTileStyle.getEntry());
        mQsTileStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference == mQsTileStyle) {
                    String value = (String) newValue;
                    Settings.System.putInt(resolver, Settings.System.QS_TILE_STYLE, Integer.valueOf(value));
                    int valueIndex = mQsTileStyle.findIndexOfValue(value);
                    mQsTileStyle.setSummary(mQsTileStyle.getEntries()[valueIndex]);
                    String overlayName = getOverlayName(ThemesUtils.QS_TILE_THEMES);
                    if (overlayName != null) {
                    handleOverlays(overlayName, false, mOverlayManager);
                    }
                    if (valueIndex > 0) {
                        handleOverlays(ThemesUtils.QS_TILE_THEMES[valueIndex],
                                true, mOverlayManager);
                    }
                    return true;
                }
                return false;
            }
       });

        mNotifHeader = (SystemSettingSwitchPreference) findPreference(PREF_R_NOTIF_HEADER);
        mNotifHeader.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NOTIFICATION_HEADERS, 1) == 1));
        mNotifHeader.setOnPreferenceChangeListener(this);

        getQsHeaderStylePref();
        getQsBackgroundStylePref();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mQsPanelAlpha) {
            int bgAlpha = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_PANEL_BG_ALPHA, bgAlpha, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsColumnsQuickbar) {
                int value = (Integer) newValue;
                Settings.System.putIntForUser(getContentResolver(),
                        Settings.System.QS_QUICKBAR_COLUMNS, value, UserHandle.USER_CURRENT);
                return true;
            
        }  else if (preference == mQsClockSize) {
                int width = ((Integer)newValue).intValue();
                Settings.System.putInt(getContentResolver(),
                        Settings.System.QS_HEADER_CLOCK_SIZE, width);
                return true;
        } else if (preference == mClockFontStyle) {
                int showClockFont = Integer.valueOf((String) newValue);
                int index = mClockFontStyle.findIndexOfValue((String) newValue);
                Settings.System.putInt(getContentResolver(), Settings.System.
                    QS_HEADER_CLOCK_FONT_STYLE, showClockFont);
                mClockFontStyle.setSummary(mClockFontStyle.getEntries()[index]);
                return true;
        } else if (preference == mQsColumnsPortrait) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_LAYOUT_COLUMNS, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsColumnsLandscape) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_LAYOUT_COLUMNS_LANDSCAPE, value, UserHandle.USER_CURRENT);
            return true;
        } else  if (preference == mQuickPulldown) {
                int quickPulldownValue = Integer.valueOf((String) newValue);
                Settings.System.putIntForUser(getContentResolver(), Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN,
                        quickPulldownValue, UserHandle.USER_CURRENT);
                updatePulldownSummary(quickPulldownValue);
                return true;
        } else if (preference == mQsRowsPortrait) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_LAYOUT_ROWS, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsRowsLandscape) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_LAYOUT_ROWS_LANDSCAPE, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mCustomHeader) {
            boolean header = (Boolean) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_CUSTOM_HEADER, header ? 1 : 0);
            return true;
        } else if (preference == mQsBlur) {
                boolean value = (Boolean) newValue;
                Settings.System.putIntForUser(getActivity().getContentResolver(),
                        Settings.System.QS_BACKGROUND_BLUR, value ? 1 : 0,
                        UserHandle.USER_CURRENT);
                        mQsBlur.setChecked(value);
                return true;
        } else if (preference == mShowAlwaysSettings) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_ALWAYS_SHOW_SETTINGS, value ? 1 : 0);
            Utils.showSystemUiRestartDialog(getContext());
            return true;
        } else if (preference == mQsBatteryMode) {
            int qsBatteryMode = Integer.parseInt((String) newValue);
            int qsBatteryModeIndex = mQsBatteryMode.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_BATTERY_MODE, qsBatteryMode);
            mQsBatteryMode.setSummary(mQsBatteryMode.getEntries()[qsBatteryModeIndex]);
            return true;
        } else if (preference == mQsSysBatteryMode) {
            int qsSysBatteryMode = Integer.parseInt((String) newValue);
            int qsSysBatteryModeIndex = mQsSysBatteryMode.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_SYS_BATTERY_MODE, qsSysBatteryMode);
            mQsSysBatteryMode.setSummary(mQsSysBatteryMode.getEntries()[qsSysBatteryModeIndex]);
            return true;
        } else if (preference == mQsHeaderStyle) {
            String value = (String) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
			    Settings.System.QS_HEADER_STYLE, Integer.valueOf(value));
            int newIndex = mQsHeaderStyle.findIndexOfValue(value);
            mQsHeaderStyle.setSummary(mQsHeaderStyle.getEntries()[newIndex]);
            updateQsHeaderStyleColor();
            return true;
        } else if (preference == mQsHeaderStyleColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(hex);
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.QS_HEADER_STYLE_COLOR, intHex);
            return true;
        } else if (preference == mQsBackgroundStyle) {
            String valueBackground = (String) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
			    Settings.System.QS_BACKGROUND_STYLE, Integer.valueOf(valueBackground));
            int newIndex = mQsBackgroundStyle.findIndexOfValue(valueBackground);
            mQsBackgroundStyle.setSummary(mQsBackgroundStyle.getEntries()[newIndex]);
            updateQsBackgroundStyleColor();
            return true;
        } else if (preference == mQsBackgroundStyleColor) {
                String hexBackground = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(hexBackground);
                int intHexBackground = ColorPickerPreference.convertToColorInt(hexBackground);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.QS_BACKGROUND_STYLE_COLOR, intHexBackground);
            return true;
        } else if (preference == mNotifHeader) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NOTIFICATION_HEADERS, value ? 1 : 0);
            Utils.showSystemUiRestartDialog(getContext());
            return true;
        }
        return false;
    }

   private String getOverlayName(String[] overlays) {
        String overlayName = null;
        for (int i = 0; i < overlays.length; i++) {
            String overlay = overlays[i];
            if (Utils.isThemeEnabled(overlay)) {
                overlayName = overlay;
            }
        }
        return overlayName;
    }

 private int getOverlayPosition(String[] overlays) {
        int position = -1;
        for (int i = 0; i < overlays.length; i++) {
            String overlay = overlays[i];
            if (Utils.isThemeEnabled(overlay)) {
                position = i;
            }
        }
        return position;
    }

    private void getQsHeaderStylePref() {
        mQsHeaderStyle = (ListPreference) findPreference(QS_HEADER_STYLE);
        int qsHeaderStyle = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_HEADER_STYLE, 0);
        int valueIndex = mQsHeaderStyle.findIndexOfValue(String.valueOf(qsHeaderStyle));
        mQsHeaderStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
        mQsHeaderStyle.setSummary(mQsHeaderStyle.getEntry());
        mQsHeaderStyle.setOnPreferenceChangeListener(this);
        updateQsHeaderStyleColor();
    }

    private void updateQsHeaderStyleColor() {
        int qsHeaderStyle = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_HEADER_STYLE, 0);

        if(qsHeaderStyle == 3) {
                mQsHeaderStyleColor = (ColorPickerPreference) findPreference(QS_HEADER_STYLE_COLOR);
                int qsHeaderStyleColor = Settings.System.getInt(getContentResolver(),
                        Settings.System.QS_HEADER_STYLE_COLOR, 0x0000000);
                mQsHeaderStyleColor.setNewPreviewColor(qsHeaderStyleColor);
                String qsHeaderStyleColorHex = String.format("#%08x", (0x0000000 & qsHeaderStyleColor));
                mQsHeaderStyleColor.setSummary(qsHeaderStyleColorHex);
                mQsHeaderStyleColor.setOnPreferenceChangeListener(this);
                mQsHeaderStyleColor.setVisible(true);
        } else {
                mQsHeaderStyleColor = (ColorPickerPreference) findPreference(QS_HEADER_STYLE_COLOR);
                if(mQsHeaderStyleColor != null) {
                        mQsHeaderStyleColor.setVisible(false);
                }
        }
    }

    private void getQsBackgroundStylePref() {
        mQsBackgroundStyle = (ListPreference) findPreference(QS_BACKGROUND_STYLE);
        int qsBackgroundStyle = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_BACKGROUND_STYLE, 0);
        int valueIndex = mQsBackgroundStyle.findIndexOfValue(String.valueOf(qsBackgroundStyle));
        mQsBackgroundStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
        mQsBackgroundStyle.setSummary(mQsBackgroundStyle.getEntry());
        mQsBackgroundStyle.setOnPreferenceChangeListener(this);
        updateQsBackgroundStyleColor();
    }

    private void updateQsBackgroundStyleColor() {
        int qsBackgroundStyle = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_BACKGROUND_STYLE, 0);

        if(qsBackgroundStyle == 1) {
                mQsBackgroundStyleColor = (ColorPickerPreference) findPreference(QS_BACKGROUND_STYLE_COLOR);
                int qsBackgroundStyleColor = Settings.System.getInt(getContentResolver(),
                        Settings.System.QS_BACKGROUND_STYLE_COLOR, 0x0000000);
                mQsBackgroundStyleColor.setNewPreviewColor(qsBackgroundStyleColor);
                String qsBackgroundStyleColorHex = String.format("#%08x", (0x0000000 & qsBackgroundStyleColor));
                mQsBackgroundStyleColor.setSummary(qsBackgroundStyleColorHex);
                mQsBackgroundStyleColor.setOnPreferenceChangeListener(this);
                mQsBackgroundStyleColor.setVisible(true);
        } else {
                mQsBackgroundStyleColor = (ColorPickerPreference) findPreference(QS_BACKGROUND_STYLE_COLOR);
                if(mQsBackgroundStyleColor != null) {
                        mQsBackgroundStyleColor.setVisible(false);
                }
        }
    }

   @Override
    public void onResume() {
        super.onResume();
        updateQsHeaderStyleColor();
        updateQsBackgroundStyleColor();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateQsHeaderStyleColor();
        updateQsBackgroundStyleColor();
    }


    private void updatePulldownSummary(int value) {
        Resources res = getResources();
         if (value == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_off));
        } else if (value == 3) {
            // quick pulldown always
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_summary_always));
        } else {
            String direction = res.getString(value == 2
                    ? R.string.quick_pulldown_left
                    : R.string.quick_pulldown_right);
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_summary, direction));
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }
}
