package com.heaven7.advance;

/**
 * 镜头相似性的研究.
 *  想法:
 *  特点： 文件连续的。
 * 1, 根据识别出的tag。计算出每秒画面, 所有概率大于 某个数值（比如0.7）的tag.
 * 2, 统计每个tag出现的次数。 根据出现的次数/ 总的秒数 （因为我们每秒取一帧画面）.得到这个概率（得出视频出现某个tag的概率）。
 *
 *  再统计概率大于 一个 ‘数值’ 的tag。
    3-1  这个个数/之前的tag数. 得到主要的画面百分比。,最后多个视频之间比较 这个概率. (待定)
    3-2,
       这些tag再对方视频中出现的概率,
 */
public class ShotSimilarTest {

    private static final float CANDIDATE_TAG_RATE = 0.5f;


}
