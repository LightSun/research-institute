package com.heaven7.utils.test;

import com.heaven7.java.base.util.Logger;
import com.vida.common.ai.AiGenerateContext;

/**
 * @author heaven7
 */
public class LogOnGenerateListenerImpl implements AiGenerateContext.OnGenerateListener {

    @Override
    public void onGenerateDone(AiGenerateContext context) {
        Logger.d("LogOnGenerateListenerImpl", "onMediaGenDone", "" + context.getClass().getName());
    }
}
