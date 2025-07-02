/**
 * Description: This is the main activity of the app.
 * Author: Adam Chen
 * Date: 2025/07/02
 */
package com.adam.app.mydeviceinfo;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.adam.app.mydeviceinfo.databinding.ActivityMainBinding;
import com.adam.app.mydeviceinfo.service.InfoService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    // view binding
    private ActivityMainBinding mBinding;

    // IItentService mService
    private IInfoService mBpService;

    // service connection: IntentService
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBpService = IInfoService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBpService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // view binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // query system information
        mBinding.btnRefresh.setOnClickListener(v -> fetchInfo());
        // export to file
        mBinding.btnExport.setOnClickListener(v -> exportToFile());

        // bind service
        bindService(new Intent(this, InfoService.class), mConnection, BIND_AUTO_CREATE);

    }

    /**
     * fechInfo: cpu and memory info
     */
    private void fetchInfo() {
        // start thread to fetch info from intent service
        new Thread(() -> {
            try {
                String cpuInfo = mBpService.getCpuInfo();
                String memoryInfo = mBpService.getMemoryInfo();
                runOnUiThread(() -> {
                    mBinding.textCpuInfo.setText(cpuInfo);
                    mBinding.textMemory.setText(memoryInfo);
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    mBinding.textCpuInfo.setText(R.string.error_failed_to_fetch_cpu_info);
                    mBinding.textMemory.setText(R.string.error_failed_to_fetch_memory_info);
                });
            }
        }).start();
    }

    /**
     * export to file: export cpu and memory info to file system_info.txt
     */
    private void exportToFile() {
        // fileName: system_info.txt
        String fileName = "system_info.txt";
        // content: cpu and memory info
        String content = mBinding.textCpuInfo.getText().toString() + "\n\n" + mBinding.textMemory.getText().toString();
        // write to file
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName); // 檔名
        values.put(MediaStore.Downloads.MIME_TYPE, "text/plain"); // 類型
        values.put(MediaStore.Downloads.IS_PENDING, 1); // 開始寫入前設為 pending

        ContentResolver resolver = getContentResolver();
        Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri fileUri = resolver.insert(collection, values); // 建立下載項目

        if (fileUri == null) {
            Util.showToast(this, getString(R.string.toast_can_not_create_uri));
            return;
        }

        try (OutputStream os = resolver.openOutputStream(fileUri)) {
            os.write(content.getBytes(StandardCharsets.UTF_8));
            os.flush();
            values.clear();
            values.put(MediaStore.Downloads.IS_PENDING, 0); // 完成寫入
            resolver.update(fileUri, values, null, null);
            Util.showToast(this, getString(R.string.toast_has_been_saved_to_download_folder));
        } catch (IOException e) {
            e.printStackTrace();
            Util.showToast(this, getString(R.string.toast_save_fail) + e.getMessage());
        }

    }

}