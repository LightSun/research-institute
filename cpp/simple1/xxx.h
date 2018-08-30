#ifndef XXX_XXX_H
#define XXX_XXX_H
#endif

#pragma once
#ifdef BUILD_XXX_DLL
#define IO_XXX_DLL __declspec(export)
#else
#define IO_XXX_DLL __declspec(import)
#endif

extern "C"
{
IO_XXX_DLL void hello(void);


}