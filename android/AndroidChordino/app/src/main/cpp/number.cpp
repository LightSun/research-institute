//
// Created by Administrator on 2019/7/25.
//

#include "number.h"
#include <sstream>


template <typename T>
T stringToNumber(const std::string& str)
{
    std::istringstream iss(str);
    T num;
    iss >> num;
    return num;
}

float strToFloat(const std::string& str){
    std::istringstream iss(str);
    float num;
    iss >> num;
    return num;
}
