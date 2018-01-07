package com.example.android.minmaxagent;

/**
 * Created by siddh on 1/7/2018.
 *
 */

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FruitRageApplication extends Application {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ComicRelief.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build() );
    }
}