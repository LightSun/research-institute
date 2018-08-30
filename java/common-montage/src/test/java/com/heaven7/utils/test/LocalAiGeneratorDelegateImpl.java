package com.heaven7.utils.test;

import com.heaven7.utils.CmdHelper;
import com.vida.common.PlatformAICmdGenerator;
import com.vida.common.ai.LocalAiGeneratorDelegate;

import java.io.File;

/**
 * @author heaven7
 */
public class LocalAiGeneratorDelegateImpl extends LocalAiGeneratorDelegate {

    private static final String ENV = "D:\\ai_scripts";

    public LocalAiGeneratorDelegateImpl( PlatformAICmdGenerator mAiCmdGen) {
        super( mAiCmdGen, new CmdHelper.LogCallback(){
            @Override
            public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                pb.directory(new File(ENV));
                pb.inheritIO();
            }
        }, new OkHAiCallback());
    }
}
