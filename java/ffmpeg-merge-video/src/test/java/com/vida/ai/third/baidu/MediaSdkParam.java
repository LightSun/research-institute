package com.vida.ai.third.baidu;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class MediaSdkParam {

    private final String mediaSdkExePath;
    private final List<VideoParam> videoParams = new ArrayList<>();
    private AudioParam audioParam;
    private List<TransitionParam> transitionParams;
    private List<EffectParam> effectParams;
    private String outputFile;

    public MediaSdkParam(String mediaSdkExePath) {
        this.mediaSdkExePath = mediaSdkExePath;
    }

    public String[] toCmds(boolean showWindow) {
        List<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("/c");
        cmds.add("start");
        cmds.add("/wait");
        if(!showWindow){
            cmds.add("/b");
        }
        cmds.add(mediaSdkExePath);

        Throwables.checkEmpty(videoParams);
        for (VideoParam param : videoParams) {
            cmds.add("-video");
            cmds.add(param.getVideoPath() + ","
                    + param.getStartTime() + ","
                    + param.getStartFrame() + ","
                    + param.getEndFrame()
            );
        }
        Throwables.checkNull(audioParam);
        cmds.add("-audio");
        cmds.add(audioParam.getAudioPath() + ","
              + audioParam.getStartTime() + ","
              + audioParam.getEndTime());
        if(!Predicates.isEmpty(transitionParams)){
            for (TransitionParam param : transitionParams){
                cmds.add("-transition");
                cmds.add(param.getIndex() + "," + param.getType());
            }
        }
        if(!Predicates.isEmpty(effectParams)){
            for (EffectParam param : effectParams){
                cmds.add("-effect");
                cmds.add(param.getIndex() + "," + param.getType());
            }
        }
        cmds.add("-output");
        cmds.add(getOutputFile());
        return cmds.toArray(new String[cmds.size()]);
    }

    public static class VideoParam {
        private String videoPath;
        private float startTime;
        private long startFrame;
        private long endFrame;

        protected VideoParam(VideoParam.Builder builder) {
            this.videoPath = builder.videoPath;
            this.startTime = builder.startTime;
            this.startFrame = builder.startFrame;
            this.endFrame = builder.endFrame;
        }

        public String getVideoPath() {
            return this.videoPath;
        }

        public float getStartTime() {
            return this.startTime;
        }

        public long getStartFrame() {
            return this.startFrame;
        }

        public long getEndFrame() {
            return this.endFrame;
        }

        public static class Builder {
            private String videoPath;
            private float startTime;
            private long startFrame;
            private long endFrame;

            public Builder setVideoPath(String videoPath) {
                this.videoPath = videoPath;
                return this;
            }

            public Builder setStartTime(float startTime) {
                this.startTime = startTime;
                return this;
            }

            public Builder setStartFrame(long startFrame) {
                this.startFrame = startFrame;
                return this;
            }

            public Builder setEndFrame(long endFrame) {
                this.endFrame = endFrame;
                return this;
            }

            public VideoParam build() {
                return new VideoParam(this);
            }
        }
    }

    public static class AudioParam {
        private String audioPath;
        private float startTime;
        private float endTime;

        protected AudioParam(AudioParam.Builder builder) {
            this.audioPath = builder.audioPath;
            this.startTime = builder.startTime;
            this.endTime = builder.endTime;
        }

        public String getAudioPath() {
            return this.audioPath;
        }

        public float getStartTime() {
            return this.startTime;
        }

        public float getEndTime() {
            return this.endTime;
        }

        public static class Builder {
            private String audioPath;
            private float startTime;
            private float endTime;

            public Builder setAudioPath(String audioPath) {
                this.audioPath = audioPath;
                return this;
            }

            public Builder setStartTime(float startTime) {
                this.startTime = startTime;
                return this;
            }

            public Builder setEndTime(float endTime) {
                this.endTime = endTime;
                return this;
            }

            public AudioParam build() {
                return new AudioParam(this);
            }
        }
    }

    public static class TransitionParam {
        private int index;
        private int type;

        public int getIndex() {
            return index;
        }

        public int getType() {
            return type;
        }

        public TransitionParam(int index, int type) {
            this.index = index;
            this.type = type;
        }
    }

    public static class EffectParam {
        private int index;
        private int type;

        public int getIndex() {
            return index;
        }

        public int getType() {
            return type;
        }

        public EffectParam(int index, int type) {
            this.index = index;
            this.type = type;
        }
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getMediaSdkExePath() {
        return this.mediaSdkExePath;
    }

    public void addVideoParam(VideoParam vp) {
        videoParams.add(vp);
    }

    public List<VideoParam> getVideoParams() {
        return videoParams;
    }

    public AudioParam getAudioParam() {
        return this.audioParam;
    }

    public void setAudioParam(AudioParam audioParam) {
        this.audioParam = audioParam;
    }

    public void addTransitionParam(TransitionParam param) {
        if (transitionParams == null) {
            transitionParams = new ArrayList<>();
        }
        transitionParams.add(param);
    }

    public void addEffectParam(EffectParam param) {
        if (effectParams == null) {
            effectParams = new ArrayList<>();
        }
        effectParams.add(param);
    }

    public List<TransitionParam> getTransitionParams() {
        return transitionParams;
    }

    public List<EffectParam> getEffectParams() {
        return effectParams;
    }
}
