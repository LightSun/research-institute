package com.vida.common.ai;

import com.vida.common.TimeRecorder;

import java.util.Arrays;

/**
 * for gen batch image .they can be in different dir.
 * @author heaven7
 */
public abstract class BaseAiGenerateContext2 extends BaseAiGenerateContext{

    public BaseAiGenerateContext2(AiGeneratorDelegate delegate) {
        super(delegate);
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
}
