package com.heaven7.java.image;

import java.io.InputStream;

public interface ImageReader {

     default ImageInfo readMatrix(String imgFile){
          return readMatrix(imgFile, null);
     }
     //format like 'jpg' , 'png' and etc.
     default ImageInfo readBytes(String imgFile, String format){
          return readBytes(imgFile, format, null);
     }
     default ImageInfo readMatrix(InputStream in){
          return readMatrix(in, null);
     }

     ImageInfo readMatrix(String imgFile, ImageLimitInfo info);
     //format like 'jpg' , 'png' and etc.
     ImageInfo readBytes(String imgFile, String format, ImageLimitInfo info);

     ImageInfo readMatrix(InputStream in, ImageLimitInfo info);

     int[] readWidthHeight(InputStream in);
     int[] readWidthHeight(String imageFile);

     class ImageInfo extends BasicImageInfo{
          private Matrix2<Integer> mat;
          private byte[] data;

          public ImageInfo(){}

          public ImageInfo(Matrix2<Integer> mat, int imageType) {
               this.mat = mat;
               setImageType(imageType);
          }
          public ImageInfo(byte[] data, int imageType) {
               this.data = data;
               setImageType(imageType);
          }
          public byte[] getData() {
               return data;
          }
          public void setData(byte[] data) {
               this.data = data;
          }

          public Matrix2<Integer> getMat() {
               return mat;
          }
          public void setMat(Matrix2<Integer> mat) {
               this.mat = mat;
          }
     }
}
