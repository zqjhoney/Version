package com.bwie.version.bean;

/**
 * Created by 张乔君 on 2017/9/13.
 */

public class Version {
    private int VersionCode = 200;//版本号
    private String url;//远程apk地址

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
