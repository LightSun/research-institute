package com.heaven7.ve;


/**
 * Created by heaven7 on 2018/1/19 0019.
 */
public class PathTimeTraveller extends TimeTraveller {

    public static final byte TYPE_VIDEO = 1;
    public static final byte TYPE_IMAGE = 2;
    public static final byte TYPE_AUDIO = 3;

    @Override
    protected TimeTravelEntityDelegate createImpl() {
        return new PathTimeTravelDelegateImpl();
    }

    public void setFrom(TimeTraveller src){
        super.setFrom(src);
        if(src instanceof PathTimeTraveller){
            setPath(((PathTimeTraveller) src).getPath());
            setType(((PathTimeTraveller) src).getType());
        }
    }

    public String getPath(){
        return getDelegateAs(PathTimeTravelDelegate.class).getPath();
    }
    public void setPath(String path){
        getDelegateAs(PathTimeTravelDelegate.class).setPath(path);
    }
    public int getType() {
        return getDelegateAs(PathTimeTravelDelegate.class).getType();
    }
    public void setType(int type) {
        getDelegateAs(PathTimeTravelDelegate.class).setType(type);
    }

    @Override
    public String toString() {
        return "PathTimeTraveller{" +
                "path='" + getPath() + '\'' +
                ", type=" + getType() +
                "} " + super.toString();
    }

    protected static class PathTimeTravelDelegateImpl extends TimeTravelDelegateImpl implements PathTimeTravelDelegate{
        private String path;
        private int type;
        @Override
        public String getPath() {
            return path;
        }
        @Override
        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public int getType() {
            return type;
        }
        @Override
        public void setType(int type) {
            this.type = type;
        }
    }
}
