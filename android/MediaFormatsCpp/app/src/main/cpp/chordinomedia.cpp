//
// Created by Administrator on 2019/7/11.
//

#include <iostream>
#include <string>
#include "chordinomedia.h"

MediaFormat** formats;
int count;

bool strEquals(int index, const std::string* ft, void* param){
    std::string suffixStr = *static_cast<std::string*>(param);
    std::string formatStr = *ft;

    return suffixStr == formatStr;
}

bool travelStrings(std::string* formats, int c, void* param, bool (*travel)(int,const std::string*, void*)){
    for (int i = 0; i < c; ++i) {
        if(travel(i, &formats[i], param)){
            return true;
        }
    }
    return false;
}

void registerMediaFormats(MediaFormat** mf, int size){
    formats = mf;
    count = size;
}
void releaseMediaFormats(){
    for (int i = 0; i < count; ++i) {
       // formats[i]->formats;
        delete formats[i];
    }
    formats = nullptr;
}

MediaFormat* getMediaFormat(const char* filename){

    std::string file(filename);
    int index = file.find(".");
    if(index < 0){
        return nullptr;
    }
    std::string suffix = file.substr(index + 1);
    for (int i = 0; i < count; ++i) {
        MediaFormat * pFormat = formats[i];
        if(travelStrings(pFormat->formats, pFormat->formatCount, &suffix, strEquals)) {
            return pFormat;
        }
    }
    return nullptr;
}


