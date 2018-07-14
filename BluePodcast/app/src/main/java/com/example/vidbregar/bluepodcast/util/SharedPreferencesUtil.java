package com.example.vidbregar.bluepodcast.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {

    private static final String IS_ON_PODCAST_DETAIL_LAYOUT = "is-on-podcast-detail-layout";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context applicationContext) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void setIsOnPodcastDetailLayout(boolean isOnPodcastDetailLayout) {
        sharedPreferences.edit()
                .putBoolean(IS_ON_PODCAST_DETAIL_LAYOUT, isOnPodcastDetailLayout)
                .apply();
    }

    public boolean getIsOnPodcastDetailLayout() {
        return sharedPreferences.getBoolean(IS_ON_PODCAST_DETAIL_LAYOUT, false);
    }
}