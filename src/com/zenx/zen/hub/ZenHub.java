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
import com.google.android.material.card.MaterialCardView;
import com.android.settings.R;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

import android.view.LayoutInflater;
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
    MaterialCardView mStatusbarCard;
    MaterialCardView mNotificationCard;
    MaterialCardView mLockscreenCard;
    MaterialCardView mNavigationCard;
    MaterialCardView mScreenCard;
    MaterialCardView mMiscCard;
    MaterialCardView mDevicePartsCard;
    MaterialCardView mRomInfoCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.zen_hub_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("ZenHub");

        mDevicePartsIntent = new Intent().setComponent(new ComponentName(
            getContext().getResources().getString(com.android.internal.R.string.config_device_parts_package_name), getContext().getResources().getString(com.android.internal.R.string.config_device_parts_package_activity)));

        mStatusbarCard = (MaterialCardView) view.findViewById(R.id.statusbar_card);
        mStatusbarCard.setOnClickListener(this);

        mNotificationCard = (MaterialCardView) view.findViewById(R.id.notification_card);
        mNotificationCard.setOnClickListener(this);

        mLockscreenCard = (MaterialCardView) view.findViewById(R.id.lockscreen_card);
        mLockscreenCard.setOnClickListener(this);

        mNavigationCard = (MaterialCardView) view.findViewById(R.id.navigation_card);
        mNavigationCard.setOnClickListener(this);

        mScreenCard = (MaterialCardView) view.findViewById(R.id.screen_card);
        mScreenCard.setOnClickListener(this);

        mMiscCard = (MaterialCardView) view.findViewById(R.id.misc_card);
        mMiscCard.setOnClickListener(this);

        mDevicePartsCard = (MaterialCardView) view.findViewById(R.id.device_parts_card);
        mDevicePartsCard.setOnClickListener(this);

        mRomInfoCard = (MaterialCardView) view.findViewById(R.id.rom_info_card);
        mRomInfoCard.setOnClickListener(this);
    }

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
