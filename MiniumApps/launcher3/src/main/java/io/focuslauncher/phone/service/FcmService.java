package io.focuslauncher.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Privacy-focused stub for Firebase Cloud Messaging Service.
 * Firebase has been removed for privacy - this is a no-op service.
 */
public class FcmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // No-op: Firebase removed for privacy
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // No-op: Firebase removed for privacy
        return START_NOT_STICKY;
    }
}
