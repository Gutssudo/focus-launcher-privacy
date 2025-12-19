package io.focuslauncher.phone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Privacy-first email utility
 * Uses native Android Intent instead of sending emails directly
 * User always sees and approves before sending
 */
public class EmailUtils {

    private static final String FEEDBACK_EMAIL = "feedback@focuslauncher.io";
    private static final String FALLBACK_URL = "https://github.com/Gutssudo/focus-launcher-privacy/issues";

    /**
     * Opens the default email app with pre-filled data
     * User must approve before sending - Privacy-respectful
     *
     * @param context Application context
     * @param subject Email subject
     * @param body Email body
     */
    public static void sendFeedbackEmail(Context context, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{FEEDBACK_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(emailIntent, "Send feedback via:"));
            } else {
                // Fallback: open GitHub issues in browser
                openFallbackFeedback(context);
            }
        } catch (Exception e) {
            openFallbackFeedback(context);
        }
    }

    /**
     * Opens GitHub issues as fallback when no email app is available
     *
     * @param context Application context
     */
    private static void openFallbackFeedback(Context context) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FALLBACK_URL));
            context.startActivity(browserIntent);
            Toast.makeText(context, "No email app found. Opening feedback page in browser.",
                Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Unable to send feedback. Please visit: " + FALLBACK_URL,
                Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Send feedback with device info for bug reports
     *
     * @param context Application context
     * @param subject Email subject
     * @param userMessage User's message
     * @param includeDeviceInfo Whether to include device information
     */
    public static void sendBugReport(Context context, String subject, String userMessage,
                                    boolean includeDeviceInfo) {
        String body = userMessage;

        if (includeDeviceInfo) {
            body += "\n\n--- Device Info (for debugging) ---\n";
            body += "Android Version: " + android.os.Build.VERSION.RELEASE + "\n";
            body += "Device: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "\n";
            body += "App Version: " + getAppVersion(context) + "\n";
            body += "\nNote: This information stays on your device and is only sent if YOU choose to send this email.";
        }

        sendFeedbackEmail(context, subject, body);
    }

    /**
     * Get app version name
     */
    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
