package com.heaven7.java.image.detect;

/**
 * the high light data.
 */
public interface IHighLightData extends IDataTransformer<IHighLightData>{

     String getName();
     float getScore();
     Location getLocation();
}