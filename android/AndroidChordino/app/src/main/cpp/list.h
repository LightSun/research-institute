//
// Created by Administrator on 2019/7/24.
//

#ifndef ANDROIDCHORDINO_LIST_H
#define ANDROIDCHORDINO_LIST_H

#include "vector"

using namespace std;


template<typename T>
class List {

private:
    vector<T> array;

public:
    typedef int (*TravelFunc)(int, const T &);

    typedef int (*TravelTwoFunc)(int, const T &, const T &);

    typedef void *(*MapFunc)(int, const T &);

    inline size_t size() {
        return array.size();
    }

    inline vector<T> &getVector() {
        return array;
    }

    inline T& removeEnd() {
        return removeAt(size() - 1);
    }

    inline T& getAt(int index) {
        return array[index];
    }

    T& getEnd(){
        return getAt((int)size() - 1);
    }
    T& getStart(){
        return getAt(0);
    }

    void add(T t) {
        array.push_back(t);
       // array.insert(array.end(), t);
    }

    void add(int index, T t) {
        array.insert(array.begin() + index, t);
    }

    void addAll(List<T> &l) {
        array.insert(array.end(), l.array.begin(), l.array.end());
    }

    bool isEmpty() {
        return size() == 0;
    }

    bool remove(T t) {
        size_t size = array.size();
        for (int i = 0; i < size; i++) {
            //need overload ==
            if (array[i] == t) {
                array.erase(array.begin() + i);
                break;
            }
        }
        return false;
    }

    T &set(int index, T newT) {
        if (index >= size()) {
            return nullptr;
        }
        T old = array[index];
        array[index] = newT;
        return old;
    }

    void clear() {
        size_t size = List::size();
        if (size > 0) {
            remove(0, (int) size);
        }
    }

    T &removeAt(int index) {
        if (index >= size()) {
            std::string str = "";
            str += "index out of range. index = ";
            str += index;
            str += " ,size = ";
            str += size();
            __throw_invalid_argument(str.c_str());
        }
        T& t = array[index];
        remove(index, 1);
        return t;
    }

    void remove(int startIndex, int count) {
        long size = array.size();
        if (count <= 0 || startIndex < 0) {
            __throw_invalid_argument("start or end index is wrong");
        }
        array.erase(array.begin() + startIndex, array.begin() + startIndex + count);
    }

    void travel(TravelFunc tl) {
        const size_t s = size();
        for (int i = 0; i < s; ++i) {
            if (tl(i, getAt(i))) {
                break;
            }
        }
    }

    void travelTwo(TravelTwoFunc func) {
        const size_t s = size();
        for (int i = 0; i < s - 1; ++i) {
            T &t1 = getAt(i);
            T &t2 = getAt(i + 1);
            if (func(i, t1, t2)) {
                break;
            }
        }
    }

    void map(MapFunc tl, List<void *> out) {
        const size_t s = size();
        for (int i = 0; i < s; ++i) {
            void *const result = tl(i, getAt(i));
            if (result != nullptr) {
                out.add(result);
            }
        }
    }
    //========================================

    List<T> &operator=(const List<T> &array1) {
        if(&array1 != this){
            this->array = array1.array;
        }
        return *this;
    }

    const std::string toString(){
        const size_t s = size();
        if(s == 0){
            return "";
        }
        std::stringstream out;
        out << getAt(0);
        //startsWidth
        if(out.str().find("0x") == 0){
            //means is address.
            out.str("");
            for (int i = 0; i < s; ++i) {
                out << *getAt(i) << ", ";
            }
        } else{
            for (int i = 1; i < s; ++i) {
                out << ", " << getAt(i);
            }
        }
        return out.str();
    }
};


#endif //ANDROIDCHORDINO_LIST_H
