package com.mullerco.dank.dankplacestoeat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Utility functions for notifications & toasts.
 */

public class NotificationUtils {

    /**
     * Show a simple toast notification for the default short time period (2 seconds)
     */
    public static void ShowSimpleToastNotification(Context context, String toastText) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastText, duration);
        toast.show();
    }
}
