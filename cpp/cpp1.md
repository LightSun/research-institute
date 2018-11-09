# c++ 各种cast
 * reinterpret_cast 
     * 重新诠释. 比如 把long  变化为具体的指针类型（*）。
     * ColorMixInfo *info = reinterpret_cast<ColorMixInfo *>(ptr);
 * static_cast
     * 静态转化。 比如把int64 转化 为int. 
     * eg: int end = static_cast<int>(veNode->getAdjustEndTime(track));
 * const_cast  
     * 常量转换。 把常量变化为非常量.
     * char *path = const_cast<char *>(pointer->getPath().c_str()); 
 * 常量引用类型变化为非常量引用
```cpp
const StickerDataConvertor &convertor = createStickerDataConvertor();
const vector<StickerData> &vector = vec.convert(
(DataConvertor<StickerElement, StickerData> &) convertor).getVector();
``` 


# c++ 泛型
```cpp
template<typename T_src,typename T_dst>
class DataConvertor {
public:
    virtual T_dst convert(T_src obj) = 0;
};

//--- typename 标识的类型可以是基本类型 --------
//...
template<typename D> Vector2<D> convert(DataConvertor<T, D> &vt) {
        Vector2<D> result;
        long size = list.size();
        for (int i = 0; i < size; i++) {
            const D d = vt.convert(list[i]);
            result.add(d);
        }
        return result;
    }

//use
const StickerDataConvertor &convertor = createStickerDataConvertor();
const vector<StickerData> &vector = vec.convert(
           (DataConvertor<StickerElement, StickerData> &) convertor).getVector();
```