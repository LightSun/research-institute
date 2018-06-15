package com.heaven7.advance;

import java.io.File;

public interface ImageGenerator {

    boolean generate(Matrix2<Integer> mat, File dst, int imageType, String format);

}
