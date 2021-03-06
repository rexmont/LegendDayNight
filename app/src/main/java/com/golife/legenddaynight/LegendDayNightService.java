package com.golife.legenddaynight;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import androidx.core.content.ContextCompat;

import java.util.Calendar;


public class LegendDayNightService extends WallpaperService {
    @Override
    public WallpaperService.Engine onCreateEngine() {
        return new LegendEngine();
    }

    private class LegendEngine extends WallpaperService.Engine {
        private SurfaceHolder holder;
        private Drawable drawable;
        private boolean visible;
        private Handler handler;
        private int lastDrawableId = -1;

        public LegendEngine() {
            this.drawable = drawable;
            handler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            Log.d("wp", "start");
            this.visible = visible;

            if (visible) {
                handler.post(drawImage);
            } else {
                handler.removeCallbacks(drawImage);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            lastDrawableId = -1;
            handler.post(drawImage);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawImage);
        }

        private Runnable drawImage = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {
            Bitmap bitmap = getBitmapForHour();

            if (bitmap != null) {
                Canvas canvas = holder.lockCanvas();
                int width = holder.getSurfaceFrame().width();
                int height = holder.getSurfaceFrame().height();
                bitmap = scaleCenterCrop(bitmap, height, width);

                canvas.drawBitmap(
                        bitmap,
                        0,
                        0,
                        null
                );

                holder.unlockCanvasAndPost(canvas);
                Log.d("wp", "drawn!");
            } else {
                Log.d("wp", "no need to draw!");
            }

            handler.removeCallbacks(drawImage);
            handler.postDelayed(drawImage, 15 * 60 * 1000);
        }

        private Bitmap getBitmapForHour() {
            Calendar rightNow = Calendar.getInstance();
            int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
            Bitmap bitmap = null;
            int drawableId = 0;
            Resources res = getResources();

            if (currentHourIn24Format >= 20 || currentHourIn24Format < 5) {
                drawableId = R.drawable.night;
            } else if (currentHourIn24Format >= 5 && currentHourIn24Format < 7) {
                drawableId = R.drawable.dawn;
            } else if (currentHourIn24Format >= 7 && currentHourIn24Format < 9) {
                drawableId = R.drawable.day;
            } else if (currentHourIn24Format >= 9 && currentHourIn24Format < 16) {
                drawableId = R.drawable.day_2;
            } else if (currentHourIn24Format >= 16 && currentHourIn24Format < 18) {
                drawableId = R.drawable.afternoon;
            } else if (currentHourIn24Format >= 18 && currentHourIn24Format < 20) {
                drawableId = R.drawable.dusk;
            } else {
                drawableId = R.drawable.day;
            }
            if (lastDrawableId != drawableId) {
                lastDrawableId = drawableId;
                return BitmapFactory.decodeResource(res, drawableId);
            } else {
                return null;
            }
        }

        private Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);

            return dest;
        }
    }
}
