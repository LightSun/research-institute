package com.vida.ai.third.baidu.temp;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.utils.FileUtils;
import okhttp3.Call;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * upload for empty content.
 * @author heaven7
 */
public class UploadTaskVisitor implements FireVisitor<BatchUpdateSample.Task> {

    private static final String TAG = "UploadTaskVisitor";
    private int datasetId;
    private BatchUpdateSample parent;

    private final StringBuilder sb_empty = new StringBuilder();
    private final StringBuilder sb_failed = new StringBuilder();

    private String emptyLogFile;
    private String failedLogFile;

    private String tasksTag;
    private final AtomicInteger varCount = new AtomicInteger();
    private int count;
    private Runnable end;

    protected UploadTaskVisitor(UploadTaskVisitor.Builder builder) {
        this.datasetId = builder.datasetId;
        this.parent = builder.parent;
        this.count = builder.count;

        this.emptyLogFile = builder.emptyLogFile;
        this.failedLogFile = builder.failedLogFile;
        this.tasksTag = builder.tasksTag;
        this.end = builder.endTask;
    }

    @Override
    public Boolean visit(BatchUpdateSample.Task task, Object param) {
        try {
            parent.detectObject(datasetId, task.imagePath, task.jsonPath, new BatchUpdateSample.LogCallback(task.jsonPath){
                @Override
                public void onEmptyLabel(String image, String json) {
                    sb_empty.append(image).append("\r\n");
                    super.onEmptyLabel(image, json);
                }

                @Override
                protected void onFailed(String jsonPath) {
                    sb_failed.append(task.imagePath).append("\r\n");
                }

                @Override
                protected void postResponse() {
                    Logger.d(TAG, "postResponse", "upload finish(may fail). index = "
                            + varCount.get() + ",count = " + count + ", for tasksTag = " + tasksTag);
                    if(varCount.incrementAndGet() == count){
                        FileUtils.writeTo(emptyLogFile, sb_empty.toString());
                        FileUtils.writeTo(failedLogFile, sb_failed.toString());
                        if(end != null){
                            end.run();
                        }
                    }
                }
                @Override
                public boolean handleEmpty(BatchUpdateSample.JsonLabels labels) {
                    labels.ensureNotEmpty();
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDatasetId() {
        return this.datasetId;
    }

    public BatchUpdateSample getParent() {
        return this.parent;
    }

    public StringBuilder getSb_empty() {
        return this.sb_empty;
    }

    public StringBuilder getSb_failed() {
        return this.sb_failed;
    }

    public String getEmptyLogFile() {
        return this.emptyLogFile;
    }

    public String getFailedLogFile() {
        return this.failedLogFile;
    }

    public String getTasksTag() {
        return this.tasksTag;
    }

    public AtomicInteger getVarCount() {
        return this.varCount;
    }

    public static class Builder {
        private int datasetId;
        private BatchUpdateSample parent;
        private String emptyLogFile;
        private String failedLogFile;
        private String tasksTag;
        private int count;
        private Runnable endTask;

        public Builder setEndTask(Runnable endTask) {
            this.endTask = endTask;
            return this;
        }

        public Builder setDatasetId(int datasetId) {
            this.datasetId = datasetId;
            return this;
        }

        public Builder setParent(BatchUpdateSample parent) {
            this.parent = parent;
            return this;
        }

        public Builder setEmptyLogFile(String emptyLogFile) {
            this.emptyLogFile = emptyLogFile;
            return this;
        }

        public Builder setFailedLogFile(String failedLogFile) {
            this.failedLogFile = failedLogFile;
            return this;
        }
        public Builder setCount(int count) {
            this.count = count;
            return this;
        }
        public Builder setTasksTag(String tasksTag) {
            this.tasksTag = tasksTag;
            return this;
        }

        public UploadTaskVisitor build() {
            return new UploadTaskVisitor(this);
        }
    }
}
