package com.heaven7.ve.cross_os.pc;


import com.heaven7.ve.cross_os.CopyHelper;
import com.heaven7.ve.cross_os.IPathTimeTraveller;
import com.heaven7.ve.cross_os.ITimeTraveller;

/**
 * @author heaven7
 */
/*public*/ class SimplePathTimeTraveller extends SimpleTimeTraveller implements IPathTimeTraveller{

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
    @Override
    public void setFrom(ITimeTraveller src) {
        super.setFrom(src);
        CopyHelper.copyPathTimeTraveller(this, src);
    }
}
