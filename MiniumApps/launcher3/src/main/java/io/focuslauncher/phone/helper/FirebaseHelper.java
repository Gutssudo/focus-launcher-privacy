package io.focuslauncher.phone.helper;

/**
 * Privacy-focused stub version of FirebaseHelper.
 * All analytics and tracking methods are no-ops.
 *
 * Original Firebase tracking functionality has been removed for privacy.
 * This stub prevents compilation errors in code that references these methods.
 */
public class FirebaseHelper {

    // Action constants (kept for API compatibility)
    public static String ACTION_CALL = "call";
    public static String ACTION_SMS = "send_as_sms";
    public static String ACTION_SAVE_NOTE = "save_note";
    public static String ACTION_CONTACT_PICK = "contact_picked";
    public static String ACTION_APPLICATION_PICK = "application_picked";
    public static String SEARCH_PANE = "search_pane";
    public static String INTENTIONS = "intentions";
    public static String HIDE_ICON_BRANDING = "hide_icon_branding";
    public static String RANDOMIZED_JUNK_FOOD = "randomized_junk_food";
    public static String ALLOW_SPECIFIC = "allow_specific";
    public static String DETER_OVER_USE = "deter_over_use";
    public static String JUNKFOOD_USAGE = "junk_food_usage";
    public static String JUNKFOOD_USAGE_COVER = "junk_food_usage_cover";
    public static String USAGE_THIRD_PARTY_AS_DEFAULT = "usage_third_party_as_default";
    public static String USAGE_THIRD_PARTY_NOT_DEFAULT = "usage_third_party_not_default";

    private static FirebaseHelper firebaseHelper;

    public FirebaseHelper() {
        // No-op constructor
    }

    public static FirebaseHelper getInstance() {
        if (firebaseHelper == null) {
            firebaseHelper = new FirebaseHelper();
        }
        return firebaseHelper;
    }

    // All tracking methods are now no-ops (do nothing)

    public void logScreenUsageTime(String screenName, long startTime) {
        // Privacy: No tracking
    }

    public void logSuppressedNotification(String applicationName, long count) {
        // Privacy: No tracking
    }

    public void logSiempoMenuUsage(int actionFor, String toolname, String applicationName) {
        // Privacy: No tracking
    }

    public void logIFAction(String action, String applicationName, String data) {
        // Privacy: No tracking
    }

    public void logSiempoAsDefault(String action, long startTime) {
        // Privacy: No tracking
    }

    public void logTempoIntervalTime(int tempoType, int tempo_interval, String tempo_interval_onlyat) {
        // Privacy: No tracking
    }

    public void logIntention_IconBranding_Randomize(String eventFor, int enableDisable) {
        // Privacy: No tracking
    }

    public void logBlockUnblockApplication(String application_name, int block_unblock) {
        // Privacy: No tracking
    }

    public void logDeterUseEvent(int time_selected) {
        // Privacy: No tracking
    }

    public void logJunkFoodUsageTime(long usage_time) {
        // Privacy: No tracking
    }

    public void logJunkFoodUsageTimeWithCover(long usage_time) {
        // Privacy: No tracking
    }

    public void logTimeThirdPartyUsageAppNotAsLauncher(String applicationname, long usage_time) {
        // Privacy: No tracking
    }

    public void logTimeThirdPartyUsageAppAsLauncher(String applicationname, long usage_time) {
        // Privacy: No tracking
    }
}
