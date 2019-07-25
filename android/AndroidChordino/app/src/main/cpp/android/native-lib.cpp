#include <jni.h>
#include <string>

#include "../chordextract.h"
#include "../cut_generator.h"
#include "../chordinomedia/chordinomedia.h"
#include "../cut_gen_strategy.h"

#include "android/logger.h"

using namespace CUT_GEN;

void toIntArray(JNIEnv* env, List<ChordInfo*> *list, jintArray* jar){
    jint buf[1];
    for (int i = 0, size = (int) list->size(); i < size; ++i) {
        ChordInfo *const &pInfo = list->getAt(i);
        buf[0] = pInfo->timemsec;
        env->SetIntArrayRegion(*jar, i, 1, buf);
    }
}

 extern "C" JNIEXPORT void JNICALL
 Java_com_heaven7_android_chordino_MainActivity_extractChords(
         JNIEnv *env,
         jobject obj, jstring simpleName, jstring filename) {
     log_init();

     char* sn = const_cast<char*>(env->GetStringUTFChars(simpleName, nullptr));
     char* file = const_cast<char*>(env->GetStringUTFChars(filename, nullptr));
     void* arr[2];
     arr[0] = sn;
     arr[1] = file;
     extractChords(2, arr);
 }
//======================= cut gen ===========================
extern "C" JNIEXPORT jintArray JNICALL
Java_com_heaven7_android_chordino_Chordino_nTestSplit(JNIEnv *env, jclass clazz, jint start, jint end){
    //int SplitStrategyImpl(CutContext *rp, ChordInfo* start, ChordInfo* end, List<ChordInfo*>* out)
    log_init();

    List<ChordInfo*> list;
    CutContext* context = createCutContext();
    ChordInfo* st = context->pool.create();
    st->timemsec = start;
    ChordInfo* ed = context->pool.create();
    ed->timemsec = end;

    SplitStrategyImpl(context, st, ed, &list);
    jintArray array = env->NewIntArray((int) list.size());
    toIntArray(env, &list, &array);
    delete(context);

    return array;
 }

extern "C" JNIEXPORT jintArray JNICALL
Java_com_heaven7_android_chordino_Chordino_nTestMerge(JNIEnv *env, jclass clazz, jintArray ja){
  // MergeStrategyImpl(CutContext *rp, List<ChordInfo*>* in, List<ChordInfo*>* out)
    log_init();

    List<ChordInfo*> list;
    CutContext* context = createCutContext();
    List<ChordInfo*>* in = new List<ChordInfo*>();

    int size = env->GetArrayLength(ja);
    jint buf[1];
    for (int i = 0; i < size; ++i) {
        ChordInfo* st = context->pool.create();
        env->GetIntArrayRegion(ja, i, 1, buf);
        st->timemsec = buf[0];
        in->add(st);
    }
    MergeStrategyImpl(context, in, &list);
    jintArray array = env->NewIntArray((int) list.size());
    toIntArray(env, &list, &array);

    delete(in);
    delete(context);
    return array;
}

extern "C" JNIEXPORT jintArray JNICALL
 Java_com_heaven7_android_chordino_Chordino_nGenerateCuts(JNIEnv *env, jclass clazz, jstring filename, jstring simpleName){
    log_init();

    char* sn = const_cast<char*>(env->GetStringUTFChars(simpleName, nullptr));
    char* file = const_cast<char*>(env->GetStringUTFChars(filename, nullptr));
    List<ChordInfo*>* list = new List<ChordInfo*>();
    CutContext* context = createCutContext();

    void* arr[4];
    arr[0] = sn;
    arr[1] = file;
    arr[2] = context;
    arr[3] = list;
    if(extractChords(4, arr) != 0){
        delete(context);
        delete(list);
        return nullptr;
    }
    MusicInfo* info = new MusicInfo(*list);
    CutGenerator* cg = new CutGenerator();
    cg->splitStrategy = SplitStrategyImpl;
    cg->mergeStrategy = MergeStrategyImpl;
    //opt
    const int cutCount = generateCuts(context, info, cg, list);
    jintArray array = nullptr;
    if(cutCount == 0){
        Log log = getLog();
        if(log != nullptr){
            log("get out ChordInfos is empty. see detail of 'cut_generator.cpp'");
        }
    } else {
        array = env->NewIntArray((int) list->size());
        toIntArray(env, list, &array);
    }
    //delete
    delete(cg);
    delete(list);
    delete(info);
    delete(context);
    return array;
}