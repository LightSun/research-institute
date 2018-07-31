package com.heaven7.java.image;

public interface ImageReader {

     ImageInfo read(String imgFile);

     class ImageInfo{
          private Matrix2<Integer> mat;
          private int imageType;

          public ImageInfo(Matrix2<Integer> mat, int imageType) {
               this.mat = mat;
               this.imageType = imageType;
          }

          public Matrix2<Integer> getMat() {
               return mat;
          }
          public int getImageType() {
               return imageType;
          }
     }
}
