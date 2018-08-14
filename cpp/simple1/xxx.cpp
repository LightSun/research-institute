//
// Created by Administrator on 2018/8/14 0014.
//

#define BUILD_XXX_DLL
#include "xxx.h"
#include <iostream>

IO_XXX_DLL void hello(void)
{
    std::cout<<"Hello from dll!"<<std::endl;
    std::cin.get();
}