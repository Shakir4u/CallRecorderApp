#include <jni.h>
#include <string>
#include <android/log.h>


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_callrecorder_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */, jstring s) {
    const char* str;
    str=env->GetStringUTFChars(s, (jboolean *)0);
    __android_log_print(ANDROID_LOG_ERROR, "@@ ", "%s", str);
//    return env->NewStringUTF(env->GetStringUTFChars(s, false));
    return env->NewStringUTF(str);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_callrecorder_MainActivity_Record(JNIEnv* env,
jobject /* this */, jstring s){

}
