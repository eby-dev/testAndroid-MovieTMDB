# Add project specific ProGuard rules here.
# https://developer.android.com/build/shrink-code

# Keep AppConfig so R8 never renames/removes it or its native (JNI) method binding -
# native-lib.cpp exports a symbol named after this exact class/method signature.
-keep class com.ahmadabuhasan.movietmdb.core.config.AppConfig {
    *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}

# Gson populates DTO fields via reflection, keyed off @SerializedName - R8 shrinking can't
# see that usage, so it would otherwise strip/rename the fields and break deserialization.
-keepclassmembers class com.ahmadabuhasan.movietmdb.data.remote.dto.** {
    <fields>;
}
-keepattributes Signature
-keepattributes *Annotation*
