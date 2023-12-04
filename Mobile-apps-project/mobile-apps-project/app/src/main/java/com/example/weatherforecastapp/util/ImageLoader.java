package com.example.weatherforecastapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageLoader {
    private static final String BASE_URL = "https://openweathermap.org/img/wn/%s.png";

    public static Bitmap loadImage(String iconName) {
        String imageUrl = String.format(BASE_URL, iconName);
        Callable<Bitmap> loadImageCallable = () -> urlToBitmap(imageUrl);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> bitmapFuture = executor.submit(loadImageCallable);
        Bitmap bitmap = null;
        try {
            bitmap = bitmapFuture.get();
        } catch (ExecutionException | InterruptedException e) {
          Log.e("ThreadException", e.getMessage(), e);
        } finally {
            executor.shutdown();
        }
        return bitmap;
    }

    @Nullable
    private static Bitmap urlToBitmap(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            Log.e("MalformedUrl", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("IOException", e.getMessage(), e);
        }
        return null;
    }
}
