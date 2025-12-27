package io.focuslauncher.phone.service;

import android.os.Environment;

// Removed for privacy: import com.androidnetworking.AndroidNetworking;
// Removed for privacy: import com.androidnetworking.common.Priority;
// Removed for privacy: import com.androidnetworking.error.ANError;
// Removed for privacy: import com.androidnetworking.interfaces.AnalyticsListener;
// Removed for privacy: import com.androidnetworking.interfaces.DownloadListener;
// Removed for privacy: import com.androidnetworking.interfaces.DownloadProgressListener;
// Removed for privacy: import com.androidnetworking.interfaces.StringRequestListener;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.focuslauncher.phone.event.CheckVersionEvent;
import io.focuslauncher.phone.event.DownloadApkEvent;
import io.focuslauncher.phone.log.Tracer;
import de.greenrobot.event.EventBus;
// Removed for privacy: import okhttp3.OkHttpClient;

/**
 * Created by Shahab on 1/10/2017.
 */

@EBean
public abstract class CoreAPIClient {

    protected final String AWS_HOST = "http://34.193.40.200:8001";
    protected final String AWS_TOKEN = "SN2NaFFSMPkKRhMOioNEPERrCl2iCuhRcHwpm0J9";
    // Privacy: android-networking library removed - version check disabled
    /* protected AnalyticsListener analyticsListener = new AnalyticsListener() {
        @Override
        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
            Tracer.i("timeTakenInMillis: " + timeTakenInMillis
                    + " bytesSent: " + bytesSent
                    + " bytesReceived: " + bytesReceived
                    + " isFromCache: " + isFromCache);
        }
    }; */

    protected abstract String getAppName();

    /**
     * This function is use to check current app version with play store version
     * and display alert if update is available using AWS API's.
     */
    public void checkAppVersion(String versionFor) {
        // Privacy: android-networking library removed - version check disabled
        // No remote version checking to protect privacy
    }

    public void downloadApk() {
        // Privacy: android-networking library removed - APK download disabled
        // No remote APK downloading to protect privacy
    }

}
