package test.effect;

import test.AnimationPlan;
import test.Effect;

/**
 * 装饰效果：
 */
public class DecorateEffect extends Effect {



    // 文本字幕
    // 图形贴纸
    // 个人标识
    // 产品水印
    // AR元素
    static class DecorateItem{
        AnimationPlan anim;
        int[] location; //x, y相对视频上的
        String text;
      //决定出现个数
        long[] appear;        //出现时间
        long[] disappearTime; //消失时间
    }
}
