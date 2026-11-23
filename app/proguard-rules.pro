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

# Gson deserializes these via reflection - keepclassmembers alone isn't enough since R8's
# inlining/merging can still change class identity in ways that break Gson at runtime
# (observed as InternalSyntheticThrowCCEIfNotNull crashes), so the whole class is kept.
-keep class com.ahmadabuhasan.movietmdb.data.remote.dto.** {
    *;
}
-keepattributes Signature
-keepattributes *Annotation*
