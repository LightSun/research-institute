package com.vida.common.platform;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextReadHelper;
import com.vida.common.entity.BatchImageLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the python delegate . which is relative to python script
 * in this class .all return path is absolute path.
 *
 * @author heaven7
 */
public abstract class PyDelegate {

    private static final PyDelegate INSTANCE = new PyDelegateImpl();

    public static PyDelegate getDefault() {
        return INSTANCE;
    }

    /**
     * get out put file path by tfrecord path.
     *
     * @param tfreordPath the tfrecord path
     * @return the output file path.
     */
    public abstract String getTagOutputFile(String tfreordPath);

    /**
     * get tfrecord path for video
     *
     * @param dataDir   the data dir
     * @param videoFile the video file path
     * @return the tfrecord path
     */
    public abstract String getTfrecordPathForVideo(String dataDir, String videoFile);

    /**
     * get face path for target source dir and data dir.
     * @param videoFile the file , often is video file
     * @param dataDir the data dir , often is data dir
     * @return the face path
     */
    public abstract String getFacePath(String videoFile, String dataDir);

    /**
     * get the tfrecord config path for batch image.
     *
     * @param outDir the out dir of batch image
     * @return the tfrecords config path. often simple name is 'tfs_conig.txt'
     */
    public abstract String getTfrecordsConfigPath(String outDir);

    /**
     * get the face config path ofr batch image.
     * @param outDir the out dir of batch images.
     * @return the face fonig path
     */
    public abstract String getFaceConfigPath(String outDir);

    /**
     * read batch image config file.
     *
     * @param configPath the config path. simple name often is tfs_config.txt and face_config.txt
     * @return the batch image line.
     */
    public abstract List<BatchImageLine> readBatchImageConfigFile(String configPath);

    private static class PyDelegateImpl extends PyDelegate {
        @Override
        public String getTagOutputFile(String tfreordPath) {
            String dir = FileUtils.getFileDir(tfreordPath, 1, true);
            String fileName = FileUtils.getFileName(tfreordPath);
            if (fileName.endsWith("_output")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("_output"));
            } else if (fileName.endsWith("_outputs")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("_outputs"));
            }
            String outFile = dir + File.separator + fileName + "_predictions.csv";
            return outFile;
        }

        @Override //tfrecord path :  xxx/xxxx_output.tfrecord
        public String getTfrecordPathForVideo(String dataDir, String videoFile) {
            String fileName = FileUtils.getFileName(videoFile);
            return String.format("%s%s%s_output.tfrecord", dataDir, File.separator, fileName);
        }

        @Override
        public String getTfrecordsConfigPath(String outDir) {
            return outDir + File.separator + "tfs_config.txt";
        }

        @Override
        public String getFacePath(String videoFile, String dataDir) {
            String fileName = FileUtils.getFileName(videoFile);
            return String.format("%s%s%s_predictions.csv", dataDir, File.separator, fileName);
        }

        @Override
        public String getFaceConfigPath(String outDir) {
            return outDir + File.separator + "face_config.txt";
        }

        @Override
        public List<BatchImageLine> readBatchImageConfigFile(String configPath) {
            if (!new File(configPath).exists()) {
                return null;
            }
            TextReadHelper<BatchImageLine> helper = new TextReadHelper<>(new TextReadHelper.Callback<BatchImageLine>() {
                @Override
                public BufferedReader open(Context context, String url) throws IOException {
                    return new BufferedReader(new FileReader(url));
                }
                @Override
                public BatchImageLine parse(String line) {
                    String[] strs = line.split(",");
                    if (Predicates.isEmpty(strs)) {
                        return null;
                    }
                    BatchImageLine bil = new BatchImageLine();
                    bil.setTfrecordPath(strs[0]);
                    if (strs.length > 1) {
                        String[] imgs = strs[1].split(" ");
                        bil.setImages(new ArrayList<>(Arrays.asList(imgs)));
                    }
                    return bil;
                }
            });
            return helper.read(null, configPath);
        }
    }

}
