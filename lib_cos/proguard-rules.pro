# cosxml
-dontwarn com.tencent.cos.**
-keep class com.tencent.cos.xml.**{*;}

# foundation
-keep class com.tencent.qcloud.cos.**{*;}

# beacon
-dontwarn com.tencent.beacon.**
-keep class com.tencent.beacon.** { *; }
-dontwarn com.tencent.qmsp.**
-keep class com.tencent.qmsp.** { *; }