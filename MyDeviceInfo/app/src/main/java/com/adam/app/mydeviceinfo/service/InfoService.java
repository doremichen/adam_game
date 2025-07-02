/**
 * Description: This is the service of the app.
 * Author: Adam Chen
 * Date: 2025/07/02
 */
package com.adam.app.mydeviceinfo.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.adam.app.mydeviceinfo.IInfoService;
import com.adam.app.mydeviceinfo.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class InfoService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new BnInfoService(this);
    }

    private String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            sb.append("Failed to read: ").append(e.getMessage());
        }
        return sb.toString();
    }

    private ActivityManager.MemoryInfo getMemoryInfoInternal() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return info;
    }


    /**
     * Binder stub that allows clients to call on to the service.
     */
    private static class BnInfoService extends IInfoService.Stub {

        // weak reference of info service
        private final WeakReference<InfoService> mService;

        /**
         * Constructor
         * @param service InfoService: The service that this stub is for.
         */
        public BnInfoService(InfoService service) {
            mService = new WeakReference<>(service);
        }


        @Override
        public String getCpuInfo() {
            return mService.get().readFile("/proc/cpuinfo");
        }
        @Override
        public String getMemoryInfo() {
            ActivityManager.MemoryInfo memInfo = mService.get().getMemoryInfoInternal();
            return String.format(
                    Locale.getDefault(),
                    mService.get().getString(R.string.info_total_available),
                    memInfo.totalMem / 1024f / 1024f,
                    memInfo.availMem / 1024f / 1024f);
        }
    }

}