package com.casperedmusic.s905xtvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    private final int background = Color.rgb(18, 20, 27);
    private final int cardColor = Color.rgb(39, 43, 54);
    private final int accent = Color.rgb(75, 145, 245);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(background);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(42), dp(28), dp(42), dp(32));
        scroll.addView(page);

        TextView heading = new TextView(this);
        heading.setText("Home");
        heading.setTextColor(Color.WHITE);
        heading.setTextSize(30);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        page.addView(heading);

        TextView subtitle = new TextView(this);
        subtitle.setText("Your apps");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(16);
        LinearLayout.LayoutParams subtitleParams = new LinearLayout.LayoutParams(-2, -2);
        subtitleParams.topMargin = dp(8);
        subtitleParams.bottomMargin = dp(22);
        page.addView(subtitle, subtitleParams);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(5);
        page.addView(grid);

        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        if (apps == null || apps.isEmpty()) {
            query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            query.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(query, 0);
        }
        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override public int compare(ResolveInfo a, ResolveInfo b) {
                return a.loadLabel(pm).toString().compareToIgnoreCase(b.loadLabel(pm).toString());
            }
        });

        for (final ResolveInfo app : apps) {
            final String packageName = app.activityInfo.packageName;
            TextView tile = new TextView(this);
            tile.setText(app.loadLabel(pm));
            tile.setTextColor(Color.WHITE);
            tile.setTextSize(16);
            tile.setGravity(Gravity.CENTER);
            tile.setCompoundDrawablesWithIntrinsicBounds(null,
                    app.loadIcon(pm), null, null);
            tile.setCompoundDrawablePadding(dp(10));
            tile.setPadding(dp(10), dp(18), dp(10), dp(18));
            tile.setFocusable(true);
            tile.setFocusableInTouchMode(true);
            tile.setBackground(makeCard(cardColor, dp(12)));
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = dp(150);
            lp.height = dp(142);
            lp.setMargins(dp(7), dp(7), dp(7), dp(7));
            grid.addView(tile, lp);
            tile.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent launch = pm.getLaunchIntentForPackage(packageName);
                    if (launch != null) startActivity(launch);
                }
            });
            tile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override public void onFocusChange(View v, boolean focused) {
                    v.setBackground(makeCard(focused ? accent : cardColor, dp(12)));
                    v.setScaleX(focused ? 1.06f : 1f);
                    v.setScaleY(focused ? 1.06f : 1f);
                }
            });
        }

        if (apps.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No launchable apps found.");
            empty.setTextColor(Color.LTGRAY);
            empty.setTextSize(18);
            page.addView(empty);
        }

        setContentView(scroll);
        if (grid.getChildCount() > 0) grid.getChildAt(0).requestFocus();
    }

    private GradientDrawable makeCard(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
