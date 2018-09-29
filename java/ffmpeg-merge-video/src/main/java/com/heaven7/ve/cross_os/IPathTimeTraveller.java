package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public interface IPathTimeTraveller extends ITimeTraveller{

    byte TYPE_VIDEO = 1;
    byte TYPE_IMAGE = 2;
    byte TYPE_AUDIO = 3;

    String getPath();
    void setPath(String path);

    int getType() ;
    void setType(int type);

}
