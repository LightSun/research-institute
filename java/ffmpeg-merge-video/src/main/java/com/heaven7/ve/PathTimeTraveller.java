package com.heaven7.ve;


/**
 * Created by heaven7 on 2018/1/19 0019.
 */
public class PathTimeTraveller extends TimeTraveller {

    public static final byte TYPE_VIDEO = 1;
    public static final byte TYPE_IMAGE = 2;
    public static final byte TYPE_AUDIO = 3;

    private String path;
    private int type;

    public void setFrom(TimeTraveller src){
        super.setFrom(src);
        if(src instanceof PathTimeTraveller){
            setPath(((PathTimeTraveller) src).getPath());
            setType(((PathTimeTraveller) src).getType());
        }
    }

    @Override
    protected int getNativeType() {
        return NTYPE_PATH;
    }

    public String getPath(){
        return path;
    }
    public void setPath(String path){
        this.path = path;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "PathTimeTraveller{" +
                "path='" + getPath() + '\'' +
                ", type=" + getType() +
                "} " + super.toString();
    }
}
