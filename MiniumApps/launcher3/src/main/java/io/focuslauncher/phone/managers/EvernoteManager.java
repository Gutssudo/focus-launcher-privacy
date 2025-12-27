package io.focuslauncher.phone.managers;

import android.content.Context;
import android.widget.Toast;

// Removed for privacy: import com.evernote.client.android.EvernoteSession;
// Removed for privacy: import com.evernote.client.android.EvernoteUtil;
// Removed for privacy: import com.evernote.client.android.asyncclient.EvernoteCallback;
// Removed for privacy: import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
// Removed for privacy: import com.evernote.edam.type.Note;
// Removed for privacy: import com.evernote.edam.type.Notebook;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.focuslauncher.phone.app.CoreApplication;
import io.focuslauncher.phone.log.Tracer;

import static io.focuslauncher.phone.utils.DataUtils.NOTE_BODY;
import static io.focuslauncher.phone.utils.DataUtils.NOTE_TITLE;

/**
 * Created by Shahab on 2/7/2017.
 */
@SuppressWarnings("DefaultFileTemplate")
public class EvernoteManager {
    // Privacy: All Evernote integration removed

    public void createSiempoNotebook() {
        // Privacy: Evernote removed - no remote notebook creation
    }

    public void createNote(JSONObject newNoteObject) {
        // Privacy: Evernote removed - notes stay local only
    }

    public void listNoteBooks(String guid) {
        // Privacy: Evernote removed - no remote notebook access
    }

    public void deleteNote(final Context context) {
        // Privacy: Evernote removed - local note deletion only
    }

    public void sync() {
        // Privacy: Evernote removed - no cloud sync
    }
}
