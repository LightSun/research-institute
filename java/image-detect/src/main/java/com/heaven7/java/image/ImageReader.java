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

     class ImageInfo{
          private Matrix2<Integer> mat;
          /** the public image type . see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.*/
          private int imageType;
          /** the raw width  */
          private int width;
          /** the raw height  */
          private int height;
          private byte[] data;
          /** the scale rate of width */
          private float widthRate;
          /** the scale rate of height */
          private float heightRate;

          public ImageInfo(){}

          public ImageInfo(Matrix2<Integer> mat, int imageType) {
               this.mat = mat;
               this.imageType = imageType;
          }
          public ImageInfo(byte[] data, int imageType) {
               this.data = data;
               this.imageType = imageType;
          }
          public byte[] getData() {
               return data;
          }
          public void setData(byte[] data) {
               this.data = data;
          }

          public float getWidthRate() {
               return widthRate;
          }
          public void setWidthRate(float widthRate) {
               this.widthRate = widthRate;
          }

          public float getHeightRate() {
               return heightRate;
          }
          public void setHeightRate(float heightRate) {
               this.heightRate = heightRate;
          }

          public Matrix2<Integer> getMat() {
               return mat;
          }
          public int getImageType() {
               return imageType;
          }
          public void setMat(Matrix2<Integer> mat) {
               this.mat = mat;
          }
          public void setImageType(int imageType) {
               this.imageType = imageType;
          }

          public int getWidth() {
               return width;
          }
          public void setWidth(int width) {
               this.width = width;
          }

          public int getHeight() {
               return height;
          }
          public void setHeight(int height) {
               this.height = height;
          }
     }
}
