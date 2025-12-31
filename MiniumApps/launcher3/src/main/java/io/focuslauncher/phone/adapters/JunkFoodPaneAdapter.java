package io.focuslauncher.phone.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.focuslauncher.R;
import io.focuslauncher.phone.activities.CoreActivity;
import io.focuslauncher.phone.activities.JunkfoodFlaggingActivity;
import io.focuslauncher.phone.app.BitmapWorkerTask;
import io.focuslauncher.phone.app.CoreApplication;
import io.focuslauncher.phone.customviews.RobotoMediumTextView;
import io.focuslauncher.phone.customviews.RobotoRegularTextView;
import io.focuslauncher.phone.event.JunkAppOpenEvent;
import io.focuslauncher.phone.event.WorkProfileStateChangedEvent;
import io.focuslauncher.phone.helper.ActivityHelper;
import io.focuslauncher.phone.helper.FirebaseHelper;
import io.focuslauncher.phone.utils.DrawableProvider;
import io.focuslauncher.phone.utils.PrefSiempo;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by RajeshJadi on 2/23/2017.
 */

public class JunkFoodPaneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types for different items
    private static final int VIEW_TYPE_PERSONAL_HEADER = 0;
    private static final int VIEW_TYPE_WORK_HEADER = 1;
    private static final int VIEW_TYPE_APP_ITEM = 2;
    private static final int VIEW_TYPE_WORK_PROFILE_STATUS = 3;

    private final Context context;
    private List<String> mainListItemList;
    private List<String> personalApps = new ArrayList<>();
    private List<String> workApps = new ArrayList<>();
    private boolean hasWorkProfile = false;
    private boolean isHideIconBranding = false;
    private DrawableProvider mProvider;


    public JunkFoodPaneAdapter(Context context, ArrayList<String> mainListItemList, boolean isHideIconBranding) {
        this.context = context;
        this.mainListItemList = mainListItemList;
        this.isHideIconBranding = isHideIconBranding;
        mProvider = new DrawableProvider(context);
    }

    public void setMainListItemList(List<String> mainListItemList, boolean isHideIconBranding) {
        this.mainListItemList = mainListItemList;
        this.isHideIconBranding = isHideIconBranding;

        // Separate personal and work profile apps
        personalApps.clear();
        workApps.clear();
        hasWorkProfile = false;

        for (String packageName : mainListItemList) {
            if (packageName != null && packageName.startsWith("work:")) {
                // Work profile app
                workApps.add(packageName);
                hasWorkProfile = true;
            } else {
                // Personal profile app
                personalApps.add(packageName);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int personalSectionSize = personalApps.isEmpty() ? 0 : 1 + personalApps.size();

        if (!personalApps.isEmpty() && position == 0) {
            return VIEW_TYPE_PERSONAL_HEADER;
        }

        if (position < personalSectionSize) {
            return VIEW_TYPE_APP_ITEM;
        }

        // Work profile section
        if (hasWorkProfile && !workApps.isEmpty()) {
            int workSectionStart = personalSectionSize;
            if (position == workSectionStart) {
                return VIEW_TYPE_WORK_HEADER;
            }
            if (position == workSectionStart + 1) {
                return VIEW_TYPE_WORK_PROFILE_STATUS;
            }
        }

        return VIEW_TYPE_APP_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_PERSONAL_HEADER:
            case VIEW_TYPE_WORK_HEADER:
                View headerView = inflater.inflate(R.layout.section_header, parent, false);
                return new HeaderViewHolder(headerView);

            case VIEW_TYPE_WORK_PROFILE_STATUS:
                View statusView = inflater.inflate(R.layout.work_profile_status, parent, false);
                return new StatusViewHolder(statusView);

            case VIEW_TYPE_APP_ITEM:
            default:
                View appView = inflater.inflate(R.layout.list_application_item_grid_junk, parent, false);
                return new AppViewHolder(appView);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_PERSONAL_HEADER:
                ((HeaderViewHolder) holder).bind("Applications personnelles");
                break;

            case VIEW_TYPE_WORK_HEADER:
                ((HeaderViewHolder) holder).bind("Applications professionnelles");
                break;

            case VIEW_TYPE_WORK_PROFILE_STATUS:
                boolean isAvailable = CoreApplication.getInstance().isWorkProfileAvailable();
                ((StatusViewHolder) holder).bind(isAvailable);
                break;

            case VIEW_TYPE_APP_ITEM:
                bindAppItem((AppViewHolder) holder, position);
                break;
        }
    }

    private void bindAppItem(final AppViewHolder holder, int position) {
        final String item = getItemAtPosition(position);
        holder.linearLayout.setVisibility(View.VISIBLE);
        String applicationName = CoreApplication.getInstance().getApplicationNameFromPackageName(item);
        holder.text.setText(applicationName);
        if (isHideIconBranding) {
            holder.txtAppTextImage.setVisibility(View.VISIBLE);
            holder.imgAppIcon.setVisibility(View.GONE);
            holder.imgUnderLine.setVisibility(View.VISIBLE);
            String fontPath = "fonts/robotocondensedregular.ttf";
            if (!TextUtils.isEmpty(applicationName)) {
                holder.txtAppTextImage.setText("" + applicationName.toUpperCase().charAt(0));
            }
            // Loading Font Face
            Typeface tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            // Applying font
            holder.txtAppTextImage.setTypeface(tf);

        } else {
            holder.txtAppTextImage.setVisibility(View.GONE);
            holder.imgUnderLine.setVisibility(View.GONE);
            holder.imgAppIcon.setVisibility(View.VISIBLE);
            Bitmap bitmap = CoreApplication.getInstance().getBitmapFromMemCache(item);
            if (bitmap != null) {
                holder.imgAppIcon.setImageBitmap(bitmap);
            } else {
                ApplicationInfo appInfo = null;
                try {
                    appInfo = context.getPackageManager().getApplicationInfo(item, PackageManager.GET_META_DATA);
                    BitmapWorkerTask bitmapWorkerTask = new BitmapWorkerTask(appInfo, context.getPackageManager());
                    CoreApplication.getInstance().includeTaskPool(bitmapWorkerTask, null);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Drawable drawable = CoreApplication.getInstance().getApplicationIconFromPackageName(item);
                holder.imgAppIcon.setImageDrawable(drawable);
            }


        }

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, JunkfoodFlaggingActivity.class);
                context.startActivity(intent);
                ((CoreActivity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.getInstance().logSiempoMenuUsage(2, "", CoreApplication.getInstance().getApplicationNameFromPackageName(item));

                // Remove "work:" prefix if present
                String actualPackageName = item.startsWith("work:") ? item.substring(5) : item;

                // Get UserHandle for work profile apps
                UserHandle userHandle = getUserHandleForPackage(item);

                // Launch app with UserHandle support
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && userHandle != null) {
                    new ActivityHelper(context).openAppWithPackageName(actualPackageName, userHandle);
                } else {
                    new ActivityHelper(context).openAppWithPackageName(actualPackageName);
                }

                //Show blocking overlay after onclick
                EventBus.getDefault().post(new JunkAppOpenEvent(true));
            }
        });

        boolean isEnable = PrefSiempo.getInstance(context).read(PrefSiempo.DEFAULT_ICON_JUNKFOOD_TEXT_VISIBILITY_ENABLE, false);
        if(isEnable)
        {
            holder.txtLayout.setVisibility(View.GONE);
        }else
        {
            holder.txtLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        int count = 0;

        // Personal section (header + apps)
        if (!personalApps.isEmpty()) {
            count += 1; // Header
            count += personalApps.size();
        }

        // Work profile section (header + status + apps)
        if (hasWorkProfile && !workApps.isEmpty()) {
            count += 1; // Header
            count += 1; // Status indicator
            count += workApps.size();
        }

        return count;
    }

    /**
     * Get the app package name at the given position, accounting for headers and status views
     */
    private String getItemAtPosition(int position) {
        int personalSectionSize = personalApps.isEmpty() ? 0 : 1 + personalApps.size();

        if (position < personalSectionSize) {
            // Personal section (position 0 = header, so subtract 1)
            return personalApps.get(position - 1);
        } else {
            // Work section (positions 0,1 = header + status, so subtract personalSectionSize + 2)
            int workIndex = position - personalSectionSize - 2;
            return workApps.get(workIndex);
        }
    }

    /**
     * Get the UserHandle for a package name
     */
    private UserHandle getUserHandleForPackage(String packageName) {
        if (packageName != null && packageName.startsWith("work:")) {
            return CoreApplication.getInstance().getWorkProfileHandle();
        }
        return null;
    }

    @Subscribe
    public void onWorkProfileStateChanged(WorkProfileStateChangedEvent event) {
        // Refresh the adapter when work profile state changes
        notifyDataSetChanged();
    }

    // ViewHolder for app items
    public class AppViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        ImageView imgView, imgAppIcon;
        View imgUnderLine;
        TextView text, txtAppTextImage;
        TextView textDefaultApp;
        RelativeLayout relMenu;
        LinearLayout linearLayout;
        LinearLayout txtLayout;

        public AppViewHolder(View v) {
            super(v);
            layout = v;
            linearLayout = v.findViewById(R.id.linearList);
            relMenu = v.findViewById(R.id.relMenu);
            text = v.findViewById(R.id.text);
            textDefaultApp = v.findViewById(R.id.textDefaultApp);
            txtAppTextImage = v.findViewById(R.id.txtAppTextImage);
            imgView = v.findViewById(R.id.imgView);
            imgAppIcon = v.findViewById(R.id.imgAppIcon);
            imgUnderLine = v.findViewById(R.id.imgUnderLine);
            txtLayout = v.findViewById(R.id.txtLayout);
        }
    }

    // ViewHolder for section headers
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        RobotoMediumTextView txtSectionTitle;

        public HeaderViewHolder(View v) {
            super(v);
            txtSectionTitle = v.findViewById(R.id.txtSectionTitle);
        }

        public void bind(String title) {
            txtSectionTitle.setText(title);
        }
    }

    // ViewHolder for work profile status
    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        RobotoRegularTextView txtWorkProfileStatus;
        View imgWorkProfileStatusIndicator;

        public StatusViewHolder(View v) {
            super(v);
            txtWorkProfileStatus = v.findViewById(R.id.txtWorkProfileStatus);
            imgWorkProfileStatusIndicator = v.findViewById(R.id.imgWorkProfileStatusIndicator);
        }

        public void bind(boolean isAvailable) {
            if (isAvailable) {
                txtWorkProfileStatus.setText("Profil professionnel actif");
                imgWorkProfileStatusIndicator.setBackgroundResource(R.color.appland_blue_bright);
            } else {
                txtWorkProfileStatus.setText("Profil professionnel en pause");
                imgWorkProfileStatusIndicator.setBackgroundResource(R.color.black_50);
            }
        }
    }
}
