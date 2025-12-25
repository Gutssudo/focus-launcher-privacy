package io.focuslauncher.phone.util;

import android.Manifest;
import android.content.Context;

// Removed for privacy: import com.gun0912.tedpermission.PermissionListener;
// Removed for privacy: import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.focuslauncher.R;
import io.focuslauncher.phone.helper.FirebaseHelper;
import io.focuslauncher.phone.main.MainFragmentMediator;
import io.focuslauncher.phone.token.TokenItem;
import io.focuslauncher.phone.token.TokenItemType;
import io.focuslauncher.phone.token.TokenRouter;
import io.focuslauncher.phone.utils.PermissionUtil;
import io.focuslauncher.phone.utils.UIUtils;

/**
 * Created by roma on 16/4/18.
 */

public class ContactSmsPermissionHelper {


    private boolean isFromTokenParser;
    private TokenRouter router;
    private Context context;
    private PermissionUtil permissionUtil;
    private MainFragmentMediator mediator;
    private String messageData;
    // Privacy: TedPermission removed - using standard permission handling
    /* PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if (isFromTokenParser) {
                router.setCurrent(new TokenItem(TokenItemType.CONTACT));
            } else {
                mediator.loadContacts();
                if (router != null) {
                    router.sendText(context);
                    FirebaseHelper.getInstance()
                            .logIFAction
                                    (FirebaseHelper
                                            .ACTION_SMS, "", messageData);
                }
            }

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            UIUtils.toast(context, "Permission denied");
        }
    }; */


    public ContactSmsPermissionHelper(TokenRouter router, Context context,
                                      MainFragmentMediator mediator, boolean
                                              isFromTokenParser, String data) {
        this.router = router;
        this.context = context;
        this.mediator = mediator;
        this.isFromTokenParser = isFromTokenParser;
        this.messageData = data;

        permissionUtil = new PermissionUtil(context);

    }

    public void checkForContactAndSMSPermission() {

        if (permissionUtil.hasGiven(PermissionUtil.CONTACT_PERMISSION)
                /*&& permissionUtil.hasGiven(PermissionUtil.SEND_SMS_PERMISSION)*/
                ) {
            if (isFromTokenParser) {
                router.setCurrent(new TokenItem(TokenItemType.CONTACT));
            } else {
                mediator.loadContacts();
                if (router != null) {
                    router.sendText(context);
                    FirebaseHelper.getInstance().logIFAction(FirebaseHelper
                            .ACTION_SMS, "", messageData);
                }


            }

        } else {
            // Privacy: TedPermission removed - permission requests disabled
            UIUtils.toast(context, "Permissions required - please grant manually in settings");
        }
    }
}
