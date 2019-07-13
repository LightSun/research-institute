package com.heaven7.android.bpmdetect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by heaven7 on 2019/6/14.
 */
public final class ShortArray {

    private ByteBuffer mBuffer;

    public ShortArray(int initLength) {
        this.mBuffer = ByteBuffer.allocateDirect(initLength);
        mBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    private void require(int length){
        if(mBuffer.capacity() < length){
            this.mBuffer = ByteBuffer.allocateDirect(length);
            this.mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    public void fill(byte[] data, int offset, int size){
        require(size);
        mBuffer.rewind();
        mBuffer.put(data, offset, size);
        ShortBuffer buffer = mBuffer.asShortBuffer().asReadOnlyBuffer();
        short[] arr = new short[buffer.remaining()];
        buffer.get(arr);
    }

    public short[] getData(){
        ShortBuffer buffer = mBuffer.asShortBuffer();
        short[] arr = new short[buffer.remaining()];
        buffer.get(arr);
        return arr;
    }
}
