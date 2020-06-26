package com.zenx.zen.hub.utils;

import static android.os.UserHandle.USER_SYSTEM;

import android.content.Context;
import android.content.om.IOverlayManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.widget.Toast;

import com.android.settings.R;

public class Utils {

    public static void handleOverlays(String packagename, Boolean state, IOverlayManager mOverlayManager) {
        try {
            mOverlayManager.setEnabled(packagename, state, USER_SYSTEM);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}