# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.twoplaytech.drbetting.data.models.Team
-keep class com.twoplaytech.drbetting.data.models.Sport
-keep class com.twoplaytech.drbetting.data.models.Status
-keep class com.twoplaytech.drbetting.data.models.BettingTip
-keep class com.twoplaytech.drbetting.domain.common.Resource
-keep class com.twoplaytech.drbetting.domain.common.Result
-keep class com.twoplaytech.drbetting.data.models.TypeStatus
-keep class com.twoplaytech.drbetting.admin.data.mappers.BettingTipMapper
-keep class com.twoplaytech.drbetting.data.models.BettingTipInput
-keep class com.twoplaytech.drbetting.admin.data.models.Credentials
-keep class com.twoplaytech.drbetting.admin.data.mappers.CredentialsMapper
-keep class com.twoplaytech.drbetting.admin.data.models.CredentialsDataModel
-keep class com.twoplaytech.drbetting.admin.data.models.User
-keep class com.twoplaytech.drbetting.admin.data.models.UserInput
-keep class com.twoplaytech.drbetting.admin.data.models.AccessToken
-keep class com.twoplaytech.drbetting.admin.data.mappers.AccessTokenMapper
-keep class com.twoplaytech.drbetting.admin.data.models.UserRole
-keep class com.twoplaytech.drbetting.data.models.Message
-keep class com.twoplaytech.drbetting.data.mappers.MessageMapper
-keep class retrofit2.**
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-dontnote okhttp3.**, okio.**, retrofit2.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class com.test.apppackagename.retrofit.dto** {
    *;
}
-keep class com.twoplaytech.drbetting.data.*
-keep class com.twoplaytech.drbetting.admin.data.*