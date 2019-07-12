package net.sourceforge.lame;

/**
 * Created by heaven7 on 2019/7/12.
 */
public final class ChordinoMediaPreTest {

    public boolean init(String fileName){
        return nInit(fileName);
    }

    public int readMediaData(float[] pcms, int blockSize){
        return nReadMediaData(pcms, blockSize);
    }
    public void destroy(){
        nDestroy();
    }

    private native boolean nInit(String fileName);
    private native void nDestroy();
    private native int nReadMediaData(float[] pcms, int blockSize);
}
