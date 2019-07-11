//
// Created by Administrator on 2019/7/11.
//

#ifndef ANDROIDCHORDINO_MEDIAMANAGER_H
#define ANDROIDCHORDINO_MEDIAMANAGER_H

#include "chordinomedia.h"
#include "string"

bool travelStrings(std::string* formats, int c, std::string param, bool (*travel)(int,std::string, std::string));

void registerMediaFormats(MediaFormat** formats, int count);

void releaseMediaFormats(); // who create who release

MediaFormat* getMediaFormat(const char* filename);

#endif //ANDROIDCHORDINO_MEDIAMANAGER_H
