package com.heaven7.test_okh;

import com.heaven7.utils.FileUtils;
import com.vida.common.IOUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author heaven7
 */
public class DownloadVideo {

    public static void main(String[] args) {
        //https://qiniu-xpc0.xpccdn.com/5b4332236e237.mp4
        String url = "https://qiniu-xpc0.xpccdn.com/5b4332236e237.mp4";
        OkHttpHelper.get(url, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if(body != null) {
                    File file = new File("E:\\test\\test.mp4");
                    FileOutputStream fos = new FileOutputStream(file);
                    IOUtils.copyLarge(body.byteStream(), fos);
                    fos.close();
                }
                response.close();
                System.out.println("download down.");
            }
        });
    }
}
