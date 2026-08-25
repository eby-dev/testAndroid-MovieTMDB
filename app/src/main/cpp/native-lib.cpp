#include <jni.h>

#define STRINGIFY(x) #x
#define TOSTRING(x) STRINGIFY(x)

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ahmadabuhasan_movietmdb_core_config_AppConfig_apiKey(
        JNIEnv *env,
        jobject thiz) {
    return env->NewStringUTF(TOSTRING(api_key));
}
