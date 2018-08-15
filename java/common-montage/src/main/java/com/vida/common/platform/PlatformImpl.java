package com.vida.common.platform;

import com.vida.common.FFMpegCmdGenerator;
import com.vida.common.Platform;
import com.vida.common.PlatformAICmdGenerator;

public class PlatformImpl extends Platform {

    @Override
    public FFMpegCmdGenerator getFFMpegCmdGenerator(boolean showWindow) {
        if(getSystemType() != WINDOWS){
            throw new UnsupportedOperationException("Platform not support now.");
        }
        return new FFMpegCmdHelper(showWindow);
    }

    @Override
    public PlatformAICmdGenerator getAiCmdGenerator(boolean showWindow) {
        if(getSystemType() != WINDOWS){
            throw new UnsupportedOperationException("Platform not support now.");
        }
        return new WindowsCmdGenerator(showWindow);
    }
}
