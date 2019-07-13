//
// Created by Administrator on 2019/7/11.
//

#include "mediamanager.h"
#include "chordinomedia/chordinomedia.h"

MediaFormat** __formats;
int __count;

bool strEquals(int index, std::string ft, std::string param){
    Log log = getLog();
    if(log != nullptr){
        log("start compare: %s == %s", ft.c_str(), param.c_str());
    }
    return param == ft;
}

bool travelStrings(std::string* formats, int c, std::string param, bool (*travel)(int,std::string, std::string)){
    for (int i = 0; i < c; ++i) {
        if(travel(i, formats[i], param)){
            return true;
        }
    }
    return false;
}

void registerMediaFormats(MediaFormat** mf, int size){
    __formats = mf;
    __count = size;
}
void releaseMediaFormats(){
    if(__formats != nullptr){
        for (int i = __count - 1; i >=0 ; i--) {
            getLog()("start release %d", i);
            delete __formats[i];
        }
        __formats = nullptr;
    }
}

MediaFormat* getMediaFormat(const char* filename){

    std::string file(filename);
    int index = file.find(".");
    if(index < 0){
        return nullptr;
    }
    std::string suffix = file.substr(index + 1);
    for (int i = 0; i < __count; ++i) {
        MediaFormat * pFormat = __formats[i];
        if(travelStrings(pFormat->formats, pFormat->formatCount, suffix, strEquals)) {
            return pFormat;
        }
    }
    return nullptr;
}

