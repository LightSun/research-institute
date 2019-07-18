//
// Created by Administrator on 2019/7/11.
//

#include <iostream>
#include <string>
#include "chordinomedia.h"

Log _log = nullptr;
LogOnLevel _logLevel = nullptr;
int _log_enable = 0;

void Log_default(const char *tem, ...){
   //empty impl
}
void setLogEnable(int enable){
    _log_enable = enable;
}

void setLog(Log log) {
    _log = log;
}

Log getLog() {
    if(_log_enable){
        return _log;
    }
    return Log_default;
}


void setLogOnLevel(LogOnLevel ll) {
    _logLevel = ll;
}

LogOnLevel getLogOnLevel() {
    return _logLevel;
}

