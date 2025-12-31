package io.focuslauncher.phone.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import de.greenrobot.event.EventBus;
import io.focuslauncher.phone.event.WorkProfileStateChangedEvent;

/**
 * Broadcast receiver to listen for work profile state changes
 * (available, unavailable, unlocked)
 */
public class WorkProfileStateReceiver extends BroadcastReceiver {
    private static final String TAG = "WorkProfileReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        String action = intent.getAction();
        Log.d(TAG, "Received broadcast: " + action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (action) {
                case Intent.ACTION_MANAGED_PROFILE_AVAILABLE:
                    Log.d(TAG, "Work profile is now available");
                    EventBus.getDefault().post(new WorkProfileStateChangedEvent(true));
                    break;

                case Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE:
                    Log.d(TAG, "Work profile is now unavailable");
                    EventBus.getDefault().post(new WorkProfileStateChangedEvent(false));
                    break;

                case Intent.ACTION_MANAGED_PROFILE_UNLOCKED:
                    Log.d(TAG, "Work profile has been unlocked");
                    EventBus.getDefault().post(new WorkProfileStateChangedEvent(true));
                    break;

                default:
                    Log.w(TAG, "Unknown action: " + action);
                    break;
            }
        }
    }
}
