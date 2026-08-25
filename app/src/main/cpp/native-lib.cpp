#include <jni.h>

// Two-layer stringify: STRINGIFY alone would just produce the macro's own
// name ("api_key"); TOSTRING forces expansion of api_key to its injected
// value (from the -Dapi_key=... compiler flag) before stringifying it.
#define STRINGIFY(x) #x
#define TOSTRING(x) STRINGIFY(x)

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ahmadabuhasan_movietmdb_core_config_AppConfig_apiKey(
        JNIEnv *env,
        jobject thiz) {
    return env->NewStringUTF(TOSTRING(api_key));
}
