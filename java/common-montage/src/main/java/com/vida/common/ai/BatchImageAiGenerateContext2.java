package com.vida.common.ai;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.vida.common.TimeRecorder;
import com.vida.common.entity.BatchImageLine;
import com.vida.common.platform.PyDelegate;

import java.util.Arrays;
import java.util.List;

import static com.heaven7.utils.TextUtils.getRelativePathForPrefix;

/**
 * the batch image ai generate context. batch images can be in different dir.
 * @author heaven7
 */
public class BatchImageAiGenerateContext2 extends BaseAiGenerateContext {

    private final List<String> images;
    private final String outDir;

    private final String[] mAiGenIo;

    private final String tfs_config_path;
    private final MultiFileParamContext mMultiContext;
    private BatchImageLine batchLine;

    public BatchImageAiGenerateContext2(AiGeneratorDelegate delegate, OnGenerateListener l, List<String> images, String outDir, MultiFileParamContext multiContext) {
        super(delegate, l);
        this.images = images;
        this.outDir = outDir;
        this.mMultiContext = multiContext;
        //------------------- outDir file1 file2 file3
        String[] arr = new String[images.size() + 1];
        arr[0] = outDir;
        int index = 1;
        for (String image : images){
            arr[index ++ ] = image;
        }
        mAiGenIo = arr;

        tfs_config_path = PyDelegate.getDefault().getTfrecordsConfigPath(outDir);
    }

    @Override
    protected void log(TimeRecorder recorder, String keyword) {
        //for batch image .cmd is 'outDir file1 file2 file3'
        String[] inOut = getAiGenIO();
        String prefix;
        if(BaseAiGeneratorDelegate.isVideo(inOut[1])){
            prefix = String.format("[ Ai-Gen-%s: in = %s, out = %s ]\n", keyword, inOut[0], inOut[1]);
        }else{
            prefix = String.format("[ Ai-Gen-%s: out-dir and in-files = %s ]\n", keyword, Arrays.toString(inOut));
        }
        onWriteAiGenLog(recorder.toString(prefix));
    }

    @Override
    protected String[] getAiGenIO() {
        return mAiGenIo;
    }

    @Override
    public boolean hasTask(String path) {
        return images.contains(path);
    }
    @Override
    public String getTfRecordPath() {
        if (batchLine == null) {
            List<BatchImageLine> lines = PyDelegate.getDefault().readBatchImageConfigFile(tfs_config_path);
            if (Predicates.isEmpty(lines)) {
                Logger.w(TAG, "genTag", "STATE_TAG_TFRECORD_DONE, but read tf record path failed. " +
                        "tfs_config_path = " + tfs_config_path);
                return null;
            }
            if (lines.size() > 1) {
                Logger.w(TAG, "genTag", "batch image often is in one dir. so lines is incorrect." +
                        " tfs_config_path = " + tfs_config_path);
            }
            batchLine = lines.get(0);
        }
        return batchLine.getTfrecordPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onGenerateDone() {
        String tfrecordPath = batchLine.getTfrecordPath();
        String faceConfigPath = PyDelegate.getDefault().getFaceConfigPath(outDir);

        String tagPath = PyDelegate.getDefault().getTagOutputFile(tfrecordPath);
        AiBatchImageFileInfo info = new AiBatchImageFileInfo();
        info.setTfsConfigPath(getRelativePathForPrefix(tfs_config_path, outDir));
        info.setFaceConfigPath(getRelativePathForPrefix(faceConfigPath, outDir));
        info.setTagPath(getRelativePathForPrefix(tagPath, outDir));
        info.setTfrecordPath(getRelativePathForPrefix(faceConfigPath, outDir));
        //info.setFacePath();
        List<BatchImageLine> lines = PyDelegate.getDefault().readBatchImageConfigFile(faceConfigPath);
        if (!Predicates.isEmpty(lines)) {
            BatchImageLine line = lines.get(0);
            info.setFacePath(getRelativePathForPrefix(line.getTfrecordPath(), outDir));
        }
        List<FileParamContext> params = (List<FileParamContext>) mMultiContext.getAllFileParamContext();
        if(Predicates.isEmpty(params)){
            Logger.w(TAG, "no file param Batch-Image. for input file " + images);
            return;
        }
        List<String> images = batchLine.getImages();
        VisitServices.from(params).fire(new FireVisitor<FileParamContext>() {
            @Override
            public Boolean visit(FileParamContext param, Object param2) {
                for(String image : images){
                    if(image.endsWith(param.getSavePath())){
                        param.setImageGenInfo(info);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
