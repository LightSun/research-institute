package com.heaven7.utils.test;

import com.heaven7.utils.CmdHelper;
import com.vida.common.Platform;
import com.vida.common.PlatformAICmdGenerator;
import org.junit.Test;

import java.io.File;

/**
 * @author heaven7
 */
public class AiGenTest {

    @Test
    public void testGenFace2(){
        String videoFile = "F:\\videos\\ClothingWhite\\LM0A0231.mp4";
        String inputDir = "F:\\videos\\ClothingWhite\\temp\\LM0A0231";
        String outDir = "F:\\videos\\temp_works\\test_xxx";
        PlatformAICmdGenerator gen = Platform.getDefault().getAiCmdGenerator(true);
        String[] cmds = gen.generateFaceForVideo2(videoFile, inputDir, outDir);
        new CmdHelper(cmds).execute(new CmdHelper.LogCallback(){
            @Override
            public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                pb.directory(new File("D:\\ai_scripts"));
            }
        });
    }
}
