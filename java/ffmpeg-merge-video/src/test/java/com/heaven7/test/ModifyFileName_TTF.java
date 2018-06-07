package com.heaven7.test;

import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.test.util.FileHelper;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (跑视频tag时) 修改python 生成ttf文件的文件名
 */
public class ModifyFileName_TTF {

    public static final RenameDelegate DELEGATE = new RenameDelegateImpl(false);

    @Test
    public void generateTags_Help(){
        String dir = "F:\\videos\\故事线\\婚礼1\\空镜素材\\tfrecord";
        List<String> files = new ArrayList<>();
        FileHelper.getFiles(new File(dir), "tfrecord", files);
        generateFileOfTags_Help(dir, files);
    }

    @Test
    public void modifyFileNameOfDir() {
        String dir = "F:\\videos\\jinliang\\tb女装\\tfrecord";
        modifyFileNameByDir(dir, new RenameDelegateImpl(false));
    }

    @Test
    public void modifyFileNameOfDir2() {
        String dir = "F:\\videos\\故事线\\婚礼1\\教堂内\\tfrecord";
        modifyFileNameByDir(dir, new RenameDelegateImpl_keyword("CHURCHIN", "churchIn"));
    }

    @Test
    public void modifyFileNameOfDir3() {
        String dir = "F:\\videos\\故事线\\婚礼1\\教堂外\\tfrecord";
        modifyFileNameByDir(dir, new RenameDelegateImpl_keyword(
                Arrays.asList("CHARACTER", "CHURCHOUT"),
                Arrays.asList("character", "churchOut")
        ));
    }
    @Test
    public void modifyFileNameOfDir4() {
        String dir = "F:\\videos\\故事线\\婚礼1\\空镜素材\\tfrecord";
        modifyFileNameByDir(dir, new RenameDelegateImpl_keyword(
                Arrays.asList("CHARACTER", "EMPTY"),
                Arrays.asList("character", "empty")
        ));
    }
    @Test
    public void modifyFileNameOfDir5() {
        String dir = "F:\\videos\\故事线\\婚礼4\\晚宴\\tfrecord";
        modifyFileNameByDir(dir, DELEGATE);
    }


    private static void modifyFileNameByDir(String dir, RenameDelegate delegate) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalStateException("must exist and be dir");
        }
        List<String> files = new ArrayList<>();
        FileHelper.getFiles(file, "tfrecord'", files);
        //dst file for generate 'tags_help' file
        List<String> dstFiles = new ArrayList<>();
        for (String fn : files) {
            dstFiles.add(modifyFileName(fn, delegate));
        }

        //生成["xxx.mp4", "xxx.mp4"] 的字符串
        if (files.isEmpty()) {
            FileHelper.getFiles(file, "tfrecord", files);
            for (String fn : files) {
                dstFiles.add(modifyFileName(fn, delegate));
            }
        }
        generateFileOfTags_Help(dir, dstFiles);
    }

    private static void generateFileOfTags_Help(String dir, List<String> dstFiles) {
        StringBuilder sb = new StringBuilder();
        sb.append(dir).append("\r\n");
        sb.append("fnames = [");
        for (int i = 0, size = dstFiles.size(); i < size; i++) {
            String path = dstFiles.get(i);
            String fileName = VEGapUtils.getFileName(path);
            if (fileName.endsWith("_output")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("_output"));
            }
            sb.append("\"")
                    .append(fileName)
                    .append(".mp4\"");
            if (i != size - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        //log and write to file
        System.out.println(sb.toString());
        File helpFile = new File(dir + File.separator + "tags_help.txt");
        FileHelper.writeTo(helpFile, sb.toString());
    }

    /**
     * modify file name.
     *
     * @param path     the tf-record file path
     * @param delegate the rename delegate
     */
    private static String modifyFileName(String path, RenameDelegate delegate) {
        File file = new File(path);
        int index1 = path.indexOf("'");
        int index2 = path.lastIndexOf("'");
        boolean changed = false;
        if (index1 != -1 && index2 != -1) {
            path = path.substring(0, index1) + path.substring(index1 + 1, index2);
            changed = true;
        }
        String rename = delegate.rename(path);
        if (rename != null) {
            boolean result = file.renameTo(new File(rename));
            System.out.println("rename " + (result ? "success" : "failed") + " , path = " + rename);
            return rename;
        }
        if(changed){
            boolean result = file.renameTo(new File(path));
            System.out.println("rename " + (result ? "success" : "failed") + " , path = " + path);
        }
        return path;
    }

    public interface RenameDelegate {
        /**
         * rename file
         *
         * @param path              the full file name
         * @return the new file path.(full path)
         */
        String rename(String path);
    }

    private static final class RenameDelegateImpl implements RenameDelegate {

        private boolean toLower;

        RenameDelegateImpl(boolean toLower) {
            this.toLower = toLower;
        }

        @Override
        public String rename(String path) {
            if (toLower) {
                return path.toLowerCase();
            }
            return null;
        }
    }

    private static final class RenameDelegateImpl_keyword implements RenameDelegate {

        private final List<String> keywords;
        private final List<String> targets;

        public RenameDelegateImpl_keyword(String keyword, String target) {
            this(Arrays.asList(keyword), Arrays.asList(target));
        }

        public RenameDelegateImpl_keyword(List<String> keywords, List<String> targets) {
            this.keywords = keywords;
            this.targets = targets;
        }

        @Override
        public String rename(String path) {

            int index = path.lastIndexOf("\\");
            if (index == -1) {
                index = path.lastIndexOf("/");
            }
            if(index == -1){
                throw new IllegalStateException();
            }
            String prefix = path.substring(0, index);
            String fileName = VEGapUtils.getFileName(path);
            int index_step = fileName.indexOf("_"); //下划线
            if (index_step == -1) {
                throw new UnsupportedOperationException();
            }
            String str_check = fileName.substring(0, index_step);
            index = keywords.indexOf(str_check);
            if (index == -1) {
                return null;
            }
            return prefix + File.separator + targets.get(index)
                    + fileName.substring(index_step)+ "." + FileHelper.getFileExtension(path);
        }
    }

    public static void main(String[] args) {
        String path = "F:\\videos\\故事线\\婚礼1\\教堂外\\tfrecord\\'CHARACTER_C0200_output.tfrecord'";
        int index1 = path.indexOf("'");
        int index2 = path.lastIndexOf("'");
        if (index1 != -1 && index2 != -1) {
            path = path.substring(0, index1) + path.substring(index1 + 1, index2);
        }
        System.out.println(path);

        RenameDelegateImpl_keyword impl_keyword = new RenameDelegateImpl_keyword("CHARACTER", "character");
        String result = impl_keyword.rename(path);
        System.out.println(result);
    }
}
