package com.ivory.isasa.model;

public enum LinkEnum {
    //本地测试
//    INTERFACE_LINK("http://192.168.10.212:8090"),
//    VIEW_LINK("http://192.168.10.212:8088"),
    //
    INTERFACE_LINK("http://42.192.238.161"),
    VIEW_LINK("http://42.192.238.161:8089"),
    APK_LINk("https://android-apk-1304365928.cos.ap-shanghai.myqcloud.com/isasa.apk");




    private final String link;

    LinkEnum(String link) {
        this.link=link;
    }

    public String getLink() {
        return link;
    }
}
