package com.vida.common.platform;

import com.vida.common.FFMpegCmdGenerator;
import com.vida.common.Platform;

public class PlatformImpl extends Platform {

    @Override
    public FFMpegCmdGenerator getFFMpegCmdGenerator(boolean showWindow) {
        if(getSystemType() != WINDOWS){
            throw new UnsupportedOperationException("not support now.");
        }
        return new FFMpegCmdHelper(showWindow);
    }

    @Override
    public PlatformAICmdGenerator getAiCmdGenerator(boolean showWindow) {
        if(getSystemType() != WINDOWS){
            throw new UnsupportedOperationException("not support now.");
        }
        return new WindowsCmdGenerator(true);
    }
}
