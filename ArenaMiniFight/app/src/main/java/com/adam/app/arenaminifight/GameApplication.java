/**
 * Copyright 2023 Adam Chen. All rights reserved.
 *
 * Description: This is the game application of the application.
 *
 * @author Adam Chen
 * @version 1.0.0 - 2026/03/03
 */
package com.adam.app.arenaminifight;

import android.app.Application;
import android.content.Context;

public class GameApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // init helper
        Helper.initialize(this.getApplicationContext());
    }

    public static class Helper {
        private final Context mContext;

        private static Helper sInstance;

        private Helper(Context context) {
            mContext = context;
        }

        public Context get() {
            return sInstance.mContext;
        }

        public static Helper getInstance() {
            if (sInstance == null) {
                throw new IllegalStateException("Helper not initialized");
            }
            return sInstance;
        }

        // initial
        public static void initialize(Context context) {
            if (sInstance == null) {
                sInstance = new Helper(context);
            }
        }

    }


}
