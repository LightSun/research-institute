package com.heaven7.java.image;

import java.io.InputStream;

public interface ImageReader {

     ImageInfo readMatrix(String imgFile);

     ImageInfo readBytes(String imgFile, String format);

     ImageInfo readMatrix(InputStream in);

     int[] readWidthHeight(InputStream in);
     int[] readWidthHeight(String imageFile);

     class ImageInfo{
          private Matrix2<Integer> mat;
          /** the public image type . see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.*/
          private int imageType;
          private int width;
          private int height;
          private byte[] data;

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
