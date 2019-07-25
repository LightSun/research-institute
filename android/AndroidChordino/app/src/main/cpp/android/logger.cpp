//
// Created by Administrator on 2019/7/25.
//

#include  <stdio.h>
#include "logger.h"
#include <string>

std::string &getFormattedStr(std::string &strFormatted, const char *strFormat, va_list arglist)
{
    const int MAX_FORMATTED_STR_LEN = 2048;
    char strResult[MAX_FORMATTED_STR_LEN] = { 0 };
    vsprintf(strResult, strFormat, arglist);
    strFormatted = strResult;
    return strFormatted;
}

void log(const char *tem, ...)
{
    std::string strLog;
    va_list arglist;
    va_start(arglist, tem);
    strLog = getFormattedStr(strLog, tem, arglist);
    va_end(arglist);
    LOGD(strLog.c_str());
}

void log_init(){
    setLog(log);
}
