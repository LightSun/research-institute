package com.heaven7.ve.test.util;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.colorgap.VEGapUtils;

import java.io.*;
import java.util.List;

public class FileHelper {

    public static final FileFilter TRUE_FILE_FILTER =
            new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };

    public static void writeTo(String file, String content) {
        writeTo(new File(file), content);
    }

    public static void writeTo(File dst, String content) {
        if (dst.isDirectory()) {
            throw new IllegalStateException();
        }
        if (dst.exists()) {
            dst.delete();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(dst);
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void checkDir(String dir, boolean mustExist) {
        File file = new File(dir);
        if ((mustExist && !file.exists()) || file.isFile()) {
            throw new IllegalStateException("must be dir");
        }
    }

    public static void getVideos(File dir, List<String> outVideos) {
        getFiles(dir, "mp4", outVideos);
    }

    public static void getFiles(File dir, String extension, List<String> outFiles) {
        getFiles(dir, extension, TRUE_FILE_FILTER, outFiles);
    }

    public static void getFiles(File dir, String extension,
                                FileFilter filter, List<String> outFiles) {
        File[] videoFiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isFile()) {
                    return false;
                }
                if (!filter.accept(pathname)) {
                    return false;
                }
                String extension2 = getFileExtension(pathname);
                return extension.equalsIgnoreCase(extension2);
            }
        });
        if (!Predicates.isEmpty(videoFiles)) {
            for (File file : videoFiles) {
                outFiles.add(file.getAbsolutePath());
            }
        }
        File[] dirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (!Predicates.isEmpty(dirs)) {
            for (File dir1 : dirs) {
                getFiles(dir1, extension, filter, outFiles);
            }
        }
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    /**
     * exclude "."
     */
    public static String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * exclude "."
     */
    public static String getFileExtension(File file) {
        return getFileExtension(file.getAbsolutePath());
    }

    public static void copyFile(File src, File dst) {
        if (!dst.exists()) {
            dst.delete();
        }
        try {
            dst.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new FileInputStream(src));
            out = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
    public static FileFilter ofDirFileFilter(String dir){
        Throwables.checkNull(dir);
        return new DirFileFilter(dir);
    }

    private static final class DirFileFilter implements FileFilter{
        private final String dir;

        DirFileFilter(String dir) {
            this.dir = dir;
        }
        @Override
        public boolean accept(File pathname) {
            return dir.equals(VEGapUtils.getFileDir(pathname.getAbsolutePath(), 1, false));
        }
    }
}
