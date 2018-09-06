package com.heaven7.ve;

/**
 * 滤镜信息
 * Created by heaven7 on 2018/1/16 0016.
 */
public class FilterInfo extends EffectInfo {

    public static final FilterInfo NONE;

    public static final int TYPE_NONE  = 0;
    public static final int TYPE_F1    = 1;
    public static final int TYPE_F2    = 2;
    public static final int TYPE_F3    = 3;
    public static final int TYPE_F4    = 4;
    public static final int TYPE_F5    = 5;

   /* public int strength;   //强度
    public int lightness;  //亮度
    public int saturation; //饱和度
    public int contrast ;  //对比度*/

   static {
       NONE = new FilterInfo();
       NONE.setType(TYPE_NONE);
   }

    public void setTypeFrom(String val) {
 //TODO parse filter type.
    }

   /* public native int getStrength();
    public native void setStrength(int strength);

    public native int getLightness();
    public native void setLightness(int lightness);

    public native int getSaturation();
    public native void setSaturation(int saturation);

    public native int getContrast();
    public native void setContrast(int contrast);

    @Override
    protected int getNativeType() {
        return NTYPE_FILTER;
    }

    @Override
    public void setFrom(TimeTraveller src) {
        super.setFrom(src);
        if(src instanceof FilterInfo){
            FilterInfo fi = (FilterInfo) src;
            setStrength(fi.getStrength());
            setLightness(fi.getLightness());
            setSaturation(fi.getSaturation());
            setContrast(fi.getContrast());
        }
    }*/
}
