package com.heaven7.utils.test;

import com.heaven7.core.util.Logger;
import com.vida.common.ai.AiGenerateContext;
import com.vida.common.ai.BaseMockAiGeneratorDelegate;

public class TestMockAiGeneratorDelegate extends BaseMockAiGeneratorDelegate {
    private static final String TAG = "TestMockAiGeneratorDele";

    @Override
    public void onMediaGenDone(AiGenerateContext context) {
        Logger.d(TAG, "onMediaGenDone", "" + context.getClass().getName());
    }
}