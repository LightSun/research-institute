//
// Created by Administrator on 2018/11/1 0001.
//

#include <random>
#include <type_traits>

using namespace std;

//-------------  泛型限定 -------------------
struct B {};
struct D : public B {};
struct N {};


// #include <type_traits>
//template <class T, class H = std::enable_if_t<std::is_base_of<B, T>>>
//struct S {};


//S<D> s1; // ok
// S<N> s2; // error