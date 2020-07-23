/*
 *  Copyright (C) 2020 Zenx-OS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.zenx.zen.hub;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;
import androidx.preference.Preference;
import com.android.settings.R;
import android.widget.GridLayout;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.Gravity;
import android.util.Log;
import android.provider.Settings;

import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.ComponentName;

import com.zenx.zen.hub.fragments.statusbarquicksettings.StatusbarQuicksettingsController;
import com.zenx.zen.hub.fragments.screenanimation.UserInterfaceController;
import com.zenx.zen.hub.fragments.noficationheadsup.NotificationsHeadsUpSettingsController;
import com.zenx.zen.hub.fragments.miscbattery.MiscBatteryController;
import com.zenx.zen.hub.fragments.lockscreenambient.LockscreenAmbientController;
import com.zenx.zen.hub.fragments.linksdevs.LinksDevsController;
import com.zenx.zen.hub.fragments.buttons.ButtonsPowerMenuVolumeButtonController;

public class ZenHub extends Fragment implements View.OnClickListener {

    private static Intent mDevicePartsIntent;

    GridLayout mMainGrid;
    FrameLayout mStatusbarCard;
    FrameLayout mNotificationCard;
    FrameLayout mLockscreenCard;
    FrameLayout mNavigationCard;
    FrameLayout mScreenCard;
    FrameLayout mMiscCard;
    FrameLayout mDevicePartsCard;
    FrameLayout mRomInfoCard;

    TextView mStatusbarCardTitle;
    TextView mNotificationCardTitle;
    TextView mLockscreenCardTitle;
    TextView mNavigationCardTitle;
    TextView mScreenCardTitle;
    TextView mMiscCardTitle;
    TextView mDevicePartsCardTitle;
    TextView mRomInfoCardTitle;

    TextView mStatusbarCardDescription;
    TextView mNotificationCardDescription;
    TextView mLockscreenCardDescription;
    TextView mNavigationCardDescription;
    TextView mScreenCardDescription;
    TextView mMiscCardDescription;
    TextView mDevicePartsCardDescription;
    TextView mRomInfoCardDescription;

    ImageView mStatusbarCardImage;
    ImageView mNotificationCardImage;
    ImageView mLockscreenCardImage;
    ImageView mNavigationCardImage;
    ImageView mScreenCardImage;
    ImageView mMiscCardImage;
    ImageView mDevicePartsCardImage;
    ImageView mRomInfoCardImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zen_hub_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("ZenHub");

        lockCurrentOrientation(getActivity());

        mDevicePartsIntent = new Intent().setComponent(new ComponentName(
            getContext().getResources().getString(com.android.internal.R.string.config_device_parts_package_name), getContext().getResources().getString(com.android.internal.R.string.config_device_parts_package_activity)));

        mStatusbarCard = (FrameLayout) view.findViewById(R.id.statusbar_card);
        mStatusbarCard.setOnClickListener(this);

        mNotificationCard = (FrameLayout) view.findViewById(R.id.notification_card);
        mNotificationCard.setOnClickListener(this);

        mLockscreenCard = (FrameLayout) view.findViewById(R.id.lockscreen_card);
        mLockscreenCard.setOnClickListener(this);

        mNavigationCard = (FrameLayout) view.findViewById(R.id.navigation_card);
        mNavigationCard.setOnClickListener(this);

        mScreenCard = (FrameLayout) view.findViewById(R.id.screen_card);
        mScreenCard.setOnClickListener(this);

        mMiscCard = (FrameLayout) view.findViewById(R.id.misc_card);
        mMiscCard.setOnClickListener(this);

        mDevicePartsCard = (FrameLayout) view.findViewById(R.id.device_parts_card);
        mDevicePartsCard.setOnClickListener(this);

        mRomInfoCard = (FrameLayout) view.findViewById(R.id.rom_info_card);
        mRomInfoCard.setOnClickListener(this);

        updateTitlePosition(view);
        updateDescritionPosition(view);
        // updateZenHubIconSize(view);
    }

    public boolean isCenterTitleEnabled() {
        return Settings.System.getInt(getContext().getContentResolver(),
            Settings.System.ZENHUB_TITLE_CENTER, 1) == 1;
    }

    public boolean isCenterDescriptionEnabled() {
        return Settings.System.getInt(getContext().getContentResolver(),
            Settings.System.ZENHUB_DESCRIPTION_CENTER, 1) == 1;
    }

    public boolean isDescriptionTextEnabled() {
        return Settings.System.getInt(getContext().getContentResolver(),
            Settings.System.ZENHUB_SHOW_DESCRIPTION, 1) == 1;
    }

    public int zenHubIconSize() {
        return Settings.System.getInt(getContext().getContentResolver(),
            Settings.System.ZENHUB_ICON_SIZE, 1);
    }

    private void updateTitlePosition(View view) {

        mStatusbarCardTitle = (TextView) view.findViewById(R.id.statusbar_card_title);
        mNotificationCardTitle = (TextView) view.findViewById(R.id.notification_card_title);
        mLockscreenCardTitle = (TextView) view.findViewById(R.id.lockscreen_card_title);
        mNavigationCardTitle = (TextView) view.findViewById(R.id.navigation_card_title);
        mScreenCardTitle = (TextView) view.findViewById(R.id.screen_card_title);
        mMiscCardTitle = (TextView) view.findViewById(R.id.misc_card_title);
        mDevicePartsCardTitle = (TextView) view.findViewById(R.id.device_parts_card_title);
        mRomInfoCardTitle = (TextView) view.findViewById(R.id.rom_info_card_title);

        if(isCenterTitleEnabled()) {
            mStatusbarCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mNotificationCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mLockscreenCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mNavigationCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mScreenCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mMiscCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mDevicePartsCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mRomInfoCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            mStatusbarCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mNotificationCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mLockscreenCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mNavigationCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mScreenCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mMiscCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mDevicePartsCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            mRomInfoCardTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    private void updateDescritionPosition(View view) {

        mStatusbarCardDescription = (TextView) view.findViewById(R.id.statusbar_card_description);
        mNotificationCardDescription = (TextView) view.findViewById(R.id.notification_card_description);
        mLockscreenCardDescription = (TextView) view.findViewById(R.id.lockscreen_card_description);
        mNavigationCardDescription = (TextView) view.findViewById(R.id.navigation_card_description);
        mScreenCardDescription = (TextView) view.findViewById(R.id.screen_card_description);
        mMiscCardDescription = (TextView) view.findViewById(R.id.misc_card_description);
        mDevicePartsCardDescription = (TextView) view.findViewById(R.id.device_parts_card_description);
        mRomInfoCardDescription = (TextView) view.findViewById(R.id.rom_info_card_description);

        if(isDescriptionTextEnabled()) {
            mStatusbarCardDescription.setVisibility(View.VISIBLE);
            mNotificationCardDescription.setVisibility(View.VISIBLE);
            mLockscreenCardDescription.setVisibility(View.VISIBLE);
            mNavigationCardDescription.setVisibility(View.VISIBLE);
            mScreenCardDescription.setVisibility(View.VISIBLE);
            mMiscCardDescription.setVisibility(View.VISIBLE);
            mDevicePartsCardDescription.setVisibility(View.VISIBLE);
            mRomInfoCardDescription.setVisibility(View.VISIBLE);
            if(isCenterDescriptionEnabled()) {
                mStatusbarCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mNotificationCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mLockscreenCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mNavigationCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mScreenCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mMiscCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mDevicePartsCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mRomInfoCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                mStatusbarCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mNotificationCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mLockscreenCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mNavigationCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mScreenCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mMiscCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mDevicePartsCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                mRomInfoCardDescription.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }
        } else {
            mStatusbarCardDescription.setVisibility(View.GONE);
            mNotificationCardDescription.setVisibility(View.GONE);
            mLockscreenCardDescription.setVisibility(View.GONE);
            mNavigationCardDescription.setVisibility(View.GONE);
            mScreenCardDescription.setVisibility(View.GONE);
            mMiscCardDescription.setVisibility(View.GONE);
            mDevicePartsCardDescription.setVisibility(View.GONE);
            mRomInfoCardDescription.setVisibility(View.GONE);
        }
    }

    // private void updateZenHubIconSize(View view) {

    //     mStatusbarCardImage = (ImageView) view.findViewById(R.id.statusbar_card_image);
    //     mNotificationCardImage = (ImageView) view.findViewById(R.id.notification_card_image);
    //     mLockscreenCardImage = (ImageView) view.findViewById(R.id.lockscreen_card_image);
    //     mNavigationCardImage = (ImageView) view.findViewById(R.id.navigation_card_image);
    //     mScreenCardImage = (ImageView) view.findViewById(R.id.screen_card_image);
    //     mMiscCardImage = (ImageView) view.findViewById(R.id.misc_card_image);
    //     mDevicePartsCardImage = (ImageView) view.findViewById(R.id.device_parts_card_image);
    //     mRomInfoCardImage = (ImageView) view.findViewById(R.id.rom_info_card_image);

    //     switch(zenHubIconSize()) {
    //         case 0:
    //             mStatusbarCardImage.getLayoutParams().height = 60;
    //             mNotificationCardImage.getLayoutParams().height = 60;
    //             mLockscreenCardImage.getLayoutParams().height = 60;
    //             mNavigationCardImage.getLayoutParams().height = 60;
    //             mScreenCardImage.getLayoutParams().height = 60;
    //             mMiscCardImage.getLayoutParams().height = 60;
    //             mDevicePartsCardImage.getLayoutParams().height = 60;
    //             mRomInfoCardImage.getLayoutParams().height = 60;
    //             mStatusbarCardImage.getLayoutParams().width = 60;
    //             mNotificationCardImage.getLayoutParams().width = 60;
    //             mLockscreenCardImage.getLayoutParams().width = 60;
    //             mNavigationCardImage.getLayoutParams().width = 60;
    //             mScreenCardImage.getLayoutParams().width = 60;
    //             mMiscCardImage.getLayoutParams().width = 60;
    //             mDevicePartsCardImage.getLayoutParams().width = 60;
    //             mRomInfoCardImage.getLayoutParams().width = 60;
    //             break;
    //         case 1:
    //             mStatusbarCardImage.getLayoutParams().height = 85;
    //             mNotificationCardImage.getLayoutParams().height = 85;
    //             mLockscreenCardImage.getLayoutParams().height = 85;
    //             mNavigationCardImage.getLayoutParams().height = 85;
    //             mScreenCardImage.getLayoutParams().height = 85;
    //             mMiscCardImage.getLayoutParams().height = 85;
    //             mDevicePartsCardImage.getLayoutParams().height = 85;
    //             mRomInfoCardImage.getLayoutParams().height = 85;
    //             mStatusbarCardImage.getLayoutParams().width = 85;
    //             mNotificationCardImage.getLayoutParams().width = 85;
    //             mLockscreenCardImage.getLayoutParams().width = 85;
    //             mNavigationCardImage.getLayoutParams().width = 85;
    //             mScreenCardImage.getLayoutParams().width = 85;
    //             mMiscCardImage.getLayoutParams().width = 85;
    //             mDevicePartsCardImage.getLayoutParams().width = 85;
    //             mRomInfoCardImage.getLayoutParams().width = 85;
    //              break;
    //         case 2:
    //             mStatusbarCardImage.getLayoutParams().height = 110;
    //             mNotificationCardImage.getLayoutParams().height = 110;
    //             mLockscreenCardImage.getLayoutParams().height = 110;
    //             mNavigationCardImage.getLayoutParams().height = 110;
    //             mScreenCardImage.getLayoutParams().height = 110;
    //             mMiscCardImage.getLayoutParams().height = 110;
    //             mDevicePartsCardImage.getLayoutParams().height = 110;
    //             mRomInfoCardImage.getLayoutParams().height = 110;
    //             mStatusbarCardImage.getLayoutParams().width = 110;
    //             mNotificationCardImage.getLayoutParams().width = 110;
    //             mLockscreenCardImage.getLayoutParams().width = 110;
    //             mNavigationCardImage.getLayoutParams().width = 110;
    //             mScreenCardImage.getLayoutParams().width = 110;
    //             mMiscCardImage.getLayoutParams().width = 110;
    //             mDevicePartsCardImage.getLayoutParams().width = 110;
    //             mRomInfoCardImage.getLayoutParams().width = 110;
    //             break;
    //         case 3:
    //             mStatusbarCardImage.getLayoutParams().height = 134;
    //             mNotificationCardImage.getLayoutParams().height = 134;
    //             mLockscreenCardImage.getLayoutParams().height = 134;
    //             mNavigationCardImage.getLayoutParams().height = 134;
    //             mScreenCardImage.getLayoutParams().height = 134;
    //             mMiscCardImage.getLayoutParams().height = 134;
    //             mDevicePartsCardImage.getLayoutParams().height = 134;
    //             mRomInfoCardImage.getLayoutParams().height = 134;
    //             mStatusbarCardImage.getLayoutParams().width = 134;
    //             mNotificationCardImage.getLayoutParams().width = 134;
    //             mLockscreenCardImage.getLayoutParams().width = 134;
    //             mNavigationCardImage.getLayoutParams().width = 134;
    //             mScreenCardImage.getLayoutParams().width = 134;
    //             mMiscCardImage.getLayoutParams().width = 134;
    //             mDevicePartsCardImage.getLayoutParams().width = 134;
    //             mRomInfoCardImage.getLayoutParams().width = 134;
    //             break;
    //         default:
    //             break;
    //     }

    // }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.statusbar_card:
                StatusbarQuicksettingsController statusfragment = new StatusbarQuicksettingsController();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(this.getId(), statusfragment);
                transaction.commit();
                break;
            case R.id.notification_card:
                NotificationsHeadsUpSettingsController notifcationfragment = new NotificationsHeadsUpSettingsController();
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                transaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction1.replace(this.getId(), notifcationfragment);
                transaction1.commit();
                break;
            case R.id.lockscreen_card:
                LockscreenAmbientController lockscreenfragment = new LockscreenAmbientController();
                FragmentTransaction transaction2 = getFragmentManager().beginTransaction();
                transaction2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction2.replace(this.getId(), lockscreenfragment);
                transaction2.commit();
                break;
            case R.id.screen_card:
                UserInterfaceController screenfragment = new UserInterfaceController();
                FragmentTransaction transaction3 = getFragmentManager().beginTransaction();
                transaction3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction3.replace(this.getId(), screenfragment);
                transaction3.commit();
                break;
            case R.id.misc_card:
                MiscBatteryController miscfragment = new MiscBatteryController();
                FragmentTransaction transaction4 = getFragmentManager().beginTransaction();
                transaction4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction4.replace(this.getId(), miscfragment);
                transaction4.commit();
                break;
            case R.id.rom_info_card:
                LinksDevsController rominfofragment = new LinksDevsController();
                FragmentTransaction transaction5 = getFragmentManager().beginTransaction();
                transaction5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction5.replace(this.getId(), rominfofragment);
                transaction5.commit();
                break;
            case R.id.navigation_card:
                ButtonsPowerMenuVolumeButtonController navigationfragment = new ButtonsPowerMenuVolumeButtonController();
                FragmentTransaction transaction6 = getFragmentManager().beginTransaction();
                transaction6.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction6.replace(this.getId(), navigationfragment);
                transaction6.commit();
                break;
            case R.id.device_parts_card:
                getActivity().startActivity(mDevicePartsIntent);
                break;
        }
    }

    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.ZENX_SETTINGS;
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }
}
