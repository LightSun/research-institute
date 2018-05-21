package com.heaven7.test;

//windows 下 eclipse c++ 环境配置: https://blog.csdn.net/jason314/article/details/5639334

import java.io.IOException;

/**
 * ffmpeg 截取一段视频：
 *       https://blog.csdn.net/huangxingli/article/details/46663143
 * ffmpeg  -i  F:\\videos\\wedding\\churchIn\\churchIn_C0006.mp4  -vcodec copy  -acodec copy -ss 00:00:25 -to 00:00:30 .cutout.mp4 -y
 *
 * 合并视频文件：
 *       https://www.cnblogs.com/duanxiaojun/articles/6904878.html
 * 多个视频变ts 然后ts合成MP4
 *       https://www.jianshu.com/p/6c51b11550be
 *
 * ffmpeg:
 *
 *            对视频压缩为音频
                  ffmpeg -y -I filename -vn -ar 44100 -ac  -ab 192 -f mp3

             然后切割音频
                  ffmpeg -y -vn -ss start -t duration -i filenam -acodec copy

             最后合并音频视频
                  ffmpeg -y -i filename -i filename2 -vcode copy -acodec copy

 *       merge multi videos ->
 *              ffmpeg -i "concat:input1.mp4|input2.mp4|input3.mp4" -c copy output.mp4
 *              ffmpeg -f concat -i textfile -c copy -fflags +genpts merged.mp4
 *              ffmpeg -f concat -i textfile -fflags +genpts merged.mp4
 *
 * sample:
 *              ffmpeg -i "concat:F:\\videos\\wedding\\dinner\\dinner_87552.mp4|F:\\videos\\wedding\\dinner\\dinner_C0037.mp4" -c copy ./concat_output.mp4 -y //bad
 *              ffmpeg -safe 0 -f concat -i E:\\study\\github\\ffmpeg-merge-video\\concat.txt -c copy concat_output.mp4 -y   //ok
 *
 * 你可以使用[HH:MM:SS]格式或者以秒为单位，如00:02:30 或者 150。
 * 如果你使用了小数，如02:30.05，小数点后面的05表示1秒*5%，即50毫秒(注：1秒=1000毫秒ms =106微秒µs)而不是帧数，
 * 如02:30.5表示2分钟，30秒，500毫秒(即半秒)。
 *
 *
 * 获取视频长度:
 *       ffprobe -i <file> -show_entries format=duration -v quiet -of csv="p=0"
 *       ps:   in seconds
 *      demo:
 *             ffprobe -i F:/videos/test_cut/test_shot_cut/concat_output.mp4 -show_entries format=duration -v quiet -of csv="p=0" // ok
 */
public class MergeVideoNote {

    public static void main(String[] args) {
        ProcessBuilder builder = new ProcessBuilder()
                .command("ffmpeg -i \"concat:input1.mp4|input2.mp4|input3.mp4\" -c copy output.mp4");
        try {
            builder.start();
            ProcessBuilder.Redirect redirect = builder.redirectOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
