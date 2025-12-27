package io.focuslauncher.phone.service;

import android.os.AsyncTask;

// Removed for privacy: import com.squareup.okhttp.MediaType;
// Removed for privacy: import com.squareup.okhttp.OkHttpClient;
// Removed for privacy: import com.squareup.okhttp.Request;
// Removed for privacy: import com.squareup.okhttp.RequestBody;
// Removed for privacy: import com.squareup.okhttp.Response;

public class MailChimpOperation extends AsyncTask<String, Void, String> {

    public enum EmailType {
        EMAIL_REG,
        CONTRIBUTOR_EMAIL
    }

    public EmailType emailType;
    private boolean isSubscribed;

    public MailChimpOperation(EmailType emailType) {
        this.emailType = emailType;
        this.isSubscribed = isSubscribed;
    }

    public MailChimpOperation(EmailType emailType, boolean isSubscribed) {
        this.emailType = emailType;
        this.isSubscribed = isSubscribed;
    }

    @Override
    protected String doInBackground(String... strings) {
        // Privacy: MailChimp integration removed - no email addresses sent to external services
        return "execute";
    }

}
