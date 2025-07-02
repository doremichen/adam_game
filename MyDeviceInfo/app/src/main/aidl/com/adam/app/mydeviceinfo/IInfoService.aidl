// IInfoService.aidl
package com.adam.app.mydeviceinfo;

// Declare any non-default types here with import statements

interface IInfoService {
    // get cpu info: String
    String getCpuInfo();
    // get memory info: String
    String getMemoryInfo();
}