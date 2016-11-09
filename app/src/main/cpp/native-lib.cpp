#include <jni.h>
#include <android/log.h>
#include <string>
#include "ImageBlur.c"
#include <android/bitmap.h>

extern "C"
{

#define LOG_TAG "JNI_LOG"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


jstring Java_ndk_studyndk_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jint Java_ndk_studyndk_MainActivity_doBlur(JNIEnv *env, jobject , jobject bitmapIn, jint r) {
    AndroidBitmapInfo infoIn;
    void *pixelsIn;
    int ret;
    LOGI("cpp code");
    // Get image info
    if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoIn)) < 0)
        return 1;
    // Check image
    if (infoIn.format != ANDROID_BITMAP_FORMAT_RGBA_8888)
        return 1;
    // Lock all images
    if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsIn)) < 0) {
        //AndroidBitmap_lockPixels failed!
    }
    //height width
    int h = infoIn.height;
    int w = infoIn.width;

    //Start
    pixelsIn = StackBlur((int *) pixelsIn, w, h, r);
    //End

    // Unlocks everything
    AndroidBitmap_unlockPixels(env, bitmapIn);
    return 0;
}
}
