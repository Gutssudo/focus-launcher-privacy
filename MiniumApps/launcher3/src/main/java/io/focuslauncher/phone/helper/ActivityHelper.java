package io.focuslauncher.phone.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.widget.Toast;

import java.util.List;

import io.focuslauncher.phone.activities.AlphaSettingsActivity;
import io.focuslauncher.phone.activities.NoteListActivity;
import io.focuslauncher.phone.activities.SuppressNotificationActivity;
import io.focuslauncher.phone.app.CoreApplication;
import io.focuslauncher.phone.db.DBClient;
import io.focuslauncher.phone.launcher.FakeLauncherActivity;
import io.focuslauncher.phone.log.Tracer;
import io.focuslauncher.phone.utils.UIUtils;
import io.focuslauncher.R;


public class ActivityHelper {

    private Context context;

    public ActivityHelper(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void openNotesApp(boolean openLast) {
        try {
            Intent intent = new Intent(getContext(), NoteListActivity.class);
            intent.putExtra(NoteListActivity.EXTRA_OPEN_LATEST, openLast);
            getContext().startActivity(intent);
        } catch (Exception e) {
            CoreApplication.getInstance().logException(e);
            Tracer.e(e, e.getMessage());
        }
    }


    public void handleDefaultLauncher(Activity activity) {
        if (activity != null) {
            if (UIUtils.isMyLauncherDefault(activity)) {
                Tracer.i("Launcher3 is the default launcher");
                activity.getPackageManager().clearPackagePreferredActivities(activity.getPackageName());
                openChooser(activity);
            } else {
                Tracer.i("Launcher3 is not the default launcher: " + UIUtils.getLauncherPackageName(activity));
                if (UIUtils.getLauncherPackageName(activity).equals("android")) {
                    openChooser(activity);
                } else
                    resetPreferredLauncherAndOpenChooser(activity);
            }
        }
    }

    private void resetPreferredLauncherAndOpenChooser(Activity activity) {
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            ComponentName componentName = new ComponentName(activity, FakeLauncherActivity.class);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            activity.startActivity(startMain);
            packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        }
    }

    private void openChooser(Activity activity) {
        if (activity != null) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
        }
    }


    public void openBecomeATester() {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            CoreApplication.getInstance().logException(e);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Open the application with predefine package name.
     */
    public boolean openAppWithPackageName(String packageName) {
        return openAppWithPackageName(packageName, null);
    }

    /**
     * Open the application with package name and user profile support.
     * @param packageName The package name of the app to launch
     * @param userHandle The UserHandle for work profile apps, null for personal profile
     */
    public boolean openAppWithPackageName(String packageName, UserHandle userHandle) {
        if (packageName == null || packageName.equalsIgnoreCase("")) {
            UIUtils.alert(context, context.getString(R.string.app_not_found));
            return false;
        }

        try {
            new DBClient().deleteMsgByPackageName(packageName);

            // Use LauncherApps for work profile apps on Android 5.0+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && userHandle != null) {
                LauncherApps launcherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
                UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);

                if (launcherApps == null || userManager == null) {
                    throw new Exception("LauncherApps or UserManager not available");
                }

                // Check if work profile is paused (Android 7.0+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (userManager.isQuietModeEnabled(userHandle)) {
                        Toast.makeText(context,
                                "Le profil professionnel est en pause. Activez-le pour lancer cette application.",
                                Toast.LENGTH_LONG).show();

                        // Open settings to enable profile
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                // Try to open general settings (work profile settings may not be directly accessible)
                                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                context.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                }

                // Launch work profile app via LauncherApps
                List<LauncherActivityInfo> activities = launcherApps.getActivityList(packageName, userHandle);

                if (!activities.isEmpty()) {
                    LauncherActivityInfo activity = activities.get(0);
                    launcherApps.startMainActivity(
                            activity.getComponentName(),
                            userHandle,
                            null,  // sourceBounds
                            null   // opts
                    );
                    return true;
                } else {
                    throw new Exception("No activities found for package: " + packageName);
                }
            } else {
                // Standard launch for personal profile or legacy Android
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    context.startActivity(intent);
                    return true;
                } else {
                    throw new Exception("Launch intent not found for package: " + packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CoreApplication.getInstance().logException(e);
            UIUtils.alert(context, context.getString(R.string.app_not_found));
            return false;
        }
    }


    public void openSiempoSuppressNotificationsSettings() {
        try {
            Intent i = new Intent(context, SuppressNotificationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(i);
        } catch (Exception e) {
            Tracer.e(e, e.getMessage());
            CoreApplication.getInstance().logException(e);
        }
    }


    public void openSiempoAlphaSettingsApp() {
        try {
            getContext().startActivity(new Intent(getContext(), AlphaSettingsActivity.class));
        } catch (Exception e) {
            CoreApplication.getInstance().logException(e);
            Tracer.e(e, e.getMessage());
        }
    }
}
