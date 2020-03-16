package com.shs.trophiesapp.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class TrophiesAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        int diskCacheSizeBytes = 1024 * 1024 * 2000;
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, diskCacheSizeBytes));

        builder.setLogLevel(Log.DEBUG);
    }
}
