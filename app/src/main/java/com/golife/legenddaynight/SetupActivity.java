package com.golife.legenddaynight;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Opens Android's wallpaper preview without relying on a manufacturer's settings menu. */
public class SetupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (24 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);
        TextView description = new TextView(this);
        description.setText(R.string.setup_description);
        description.setTextSize(18);
        layout.addView(description);
        Button button = new Button(this);
        button.setText(R.string.set_wallpaper);
        layout.addView(button);
        TextView status = new TextView(this);
        layout.addView(status);
        button.setOnClickListener(view -> {
            Intent preview = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            preview.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, LegendDayNightService.class));
            try {
                startActivity(preview);
            } catch (ActivityNotFoundException exception) {
                try {
                    startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
                } catch (ActivityNotFoundException unavailable) {
                    status.setText(R.string.wallpaper_unavailable);
                }
            }
        });
        setContentView(layout);
    }
}
