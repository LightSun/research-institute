package com.vida.ai.third.baidu.temp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.base.util.threadpool.Executors2;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.vida.ai.third.baidu.VThirdBaiduService;
import okhttp3.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heaven7
 */
public class BatchUpdateSample {

    private static final String TAG = "BatchUpdateSample";
    private static final String[] FORMATS = {
            "jpg", "png", "jpeg", "jpg.jpg", "png.png" ,"jpeg.jpeg"
    };

    private final ExecutorService mService = Executors2.newFixedThreadPool(1);
    private VThirdBaiduService mBaiduService = new VThirdBaiduService();

    public void reUpload(){
        String file = "E:\\test\\batch_upload\\failed2.txt";
        VisitServices.from(readTasks(file)).fire(new FireVisitor<Task>() {
            @Override
            public Boolean visit(Task task, Object param) {
                try {
                    detectObject(task.imagePath, task.jsonPath, new LogCallback(task.jsonPath){
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public static List<Task> readTasks(String file){
        List<String> lines = ResourceLoader.getDefault().loadFileAsStringLines(null, file);
        return VisitServices.from(lines).map(new ResultVisitor<String, Task>() {
            @Override
            public Task visit(String s, Object param) {
                int index = s.indexOf(".");
                String str = s.substring(0, index);
                String jsonPath = str + ".json";
                Task task = new Task();
                task.imagePath = s;
                task.jsonPath = jsonPath;
                return task;
            }
        }).getAsList();
    }
    public void detectAll(final int datasetId, String dir, String emptyLogFile, String failedLogFile){
        detectAll(datasetId, dir, emptyLogFile, failedLogFile, null);
    }
    public void detectAll(final int datasetId, String dir, String emptyLogFile,String failedLogFile, Runnable runner){
        StringBuilder sb_no_image = new StringBuilder();
        List<String> files = FileUtils.getFiles(new File(dir), "json");
        List<Task> tasks = VisitServices.from(files).map(new ResultVisitor<String, Task>() {
            @Override
            public Task visit(String s, Object param) {
                //replace empty
                final String oldJson = s;
                String fileName = FileUtils.getFileName(s);
                String dir = FileUtils.getFileDir(s, 1, true);
                String imagePath = findImagePath(fileName, dir);
                if(imagePath == null){
                    //replace white char for file name
                    fileName = fileName.replace(" ", "%20");
                    imagePath = findImagePath(fileName, dir);
                    if(imagePath == null){
                        //can't find at last.
                        Logger.w(TAG, "detactAll", "can't find proper image for json = " + s);
                        sb_no_image.append(oldJson).append("\r\n");
                        return null;
                    }
                }

                Task task = new Task();
                task.jsonPath = s;
                task.imagePath = imagePath;
                return task;
            }
        }).getAsList();
        //no mapping for json.
        if(sb_no_image.length() > 0){
            String file = "E:\\test\\batch_upload\\no_mapping.txt";
            FileUtils.writeTo(file, sb_no_image.toString());
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sb_failed = new StringBuilder();
        final int count = tasks.size();
        Logger.d(TAG, "detectAll", "start folder. count = " + count);
        final AtomicInteger varCount = new AtomicInteger();
        VisitServices.from(tasks).fire(new FireVisitor<Task>() {
            @Override
            public Boolean visit(Task task, Object param) {
                try {
                    detectObject(datasetId, task.imagePath, task.jsonPath, new LogCallback(task.jsonPath){
                        @Override
                        public void onEmptyLabel(String image, String json) {
                            sb.append(image).append("\r\n");
                            super.onEmptyLabel(image, json);
                        }

                        @Override
                        protected void onFailed(String jsonPath) {
                            sb_failed.append(task.imagePath).append("\r\n");
                        }
                        @Override
                        protected void postResponse() {
                            Logger.d(TAG, "postResponse", "index = "
                                    + varCount.get() + ", count = "+ count +" for folder = " + dir);
                            if(varCount.incrementAndGet() == count){
                                FileUtils.writeTo(emptyLogFile, sb.toString());
                                FileUtils.writeTo(failedLogFile, sb_failed.toString());
                                if(runner != null){
                                    runner.run();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public static String findImagePath(String fileName, String dir) {
        String imagePre = dir + File.separator + fileName;
        return VisitServices.from(FORMATS).map(new ResultVisitor<String, String>() {
            @Override
            public String visit(String s, Object param) {
                return imagePre + "." + s;
            }
        }).query(new PredicateVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                File file = new File(s);
                return file.exists() && file.isFile();
            }
        });
    }

    //添加数据API 物体检测-添加数据API
    public void detectObject(String imagePath, String jsonPath, Callback callback) throws Exception{
        detectObject(25245, imagePath, jsonPath, callback);
    }

    public void detectObject(int id, String imagePath, String jsonPath, Callback callback) throws Exception{
        //
        String json = ResourceLoader.getDefault().loadFileAsString(null, jsonPath);
        JsonLabels jl = new Gson().fromJson(json, JsonLabels.class);
        if(jl.isEmpty()){
            //if handle empty ,can continue.
            if(!callback.handleEmpty(jl)){
                callback.onEmptyLabel(imagePath, jsonPath);
                return;
            }
        }

        String url = "https://aip.baidubce.com/rpc/2.0/easydl/dataset/addentity";
        String realUrl = mBaiduService.getRealUrl(url);
        Logger.d(TAG , "detectObject", "realUrl = " + realUrl);

        byte[] imgData = FileUtil.readFileByBytes(imagePath);
        String imageString = Base64.getEncoder().encodeToString(imgData);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "OBJECT_DETECTION");
        jsonObject.addProperty("dataset_id", id);
        jsonObject.addProperty("entity_content", imageString);
        jsonObject.addProperty("entity_name", FileUtils.getFileName(imagePath));
        jsonObject.add("labels", jl.toJsonArray());

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        System.out.println("=================== jsonPath = " + jsonPath);
        Logger.d(TAG, "detectObject", jsonObject.toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        post(realUrl, header, requestBody ,callback);
    }

    /** for limit request count of bai-du-http */
    private void post(String url, Map<String, String> headers, RequestBody body, Callback callback){
        Request.Builder builder = new Request.Builder().url(url)
                .post(body);
        if(!headers.isEmpty()) {
            VisitServices.from(headers).fire(new MapFireVisitor<String, String>() {
                @Override
                public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                    builder.addHeader(pair.getKey(), pair.getValue());
                    return null;
                }
            });
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(mService))
                .build();
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(callback);
    }

    static class Task{
        String imagePath;
        String jsonPath;
    }

    @JsonAdapter(ItemAdapter.class)
    public static class Item{
        String label_name;
        int left;
        int top;
        int width;
        int height;
        Item(JsonLabel jl){
            this.label_name = jl.name;
           this.left = jl.x;
           this.top = jl.y;
           this.width = jl.x2;
           this.height = jl.y2;
        }

        public void write(JsonWriter out) throws IOException{
            out.name("label_name").value(label_name)
            .name("left").value(left)
            .name("top").value(top)
            .name("width").value(width)
            .name("height").value(height);
        }
    }
    static class JsonLabel{
        String name;
        int x;
        int x2;
        int y;
        int y2;
        public JsonLabel(){}
        //just set default.
        public JsonLabel(String name) {
            this.name = name;
            this.x = 0;
            this.y = 0;
            this.x2 = 100;
            this.y2 = 100;
        }
    }
    static class JsonLabels{
        List<JsonLabel> labels;
        boolean isEmpty(){
            return Predicates.isEmpty(labels);
        }

        JsonElement toJsonArray(){
            List<Item> items = VisitServices.from(labels).map(new ResultVisitor<JsonLabel, Item>() {
                @Override
                public Item visit(JsonLabel jsonLabel, Object param) {
                    return new Item(jsonLabel);
                }
            }).getAsList();
            return new Gson().toJsonTree(items);
        }

        public void ensureNotEmpty() {
            labels.add(new JsonLabel("_default"));
        }
    }
    public static class BDResponse{
        String log_id;

        public boolean hasLogId(){
            return log_id != null;
        }
    }
    public static class ItemAdapter extends TypeAdapter<Item>{
        @Override
        public void write(JsonWriter out, Item value) throws IOException {
            out.beginObject();
            if(value.left == 0 && value.width == 0){
                out.name("label_name").value(value.label_name);
            }else{
                value.write(out);
            }
            out.endObject();
        }
        @Override
        public Item read(JsonReader in) {
            return null;
        }
    }

    public interface Callback extends okhttp3.Callback{
        void onEmptyLabel(String image, String json);

        default boolean handleEmpty(JsonLabels labels){
            return false;
        }
    }

    public static class LogCallback implements Callback{
        private final String jsonPath;

        public LogCallback(String jsonPath) {
            this.jsonPath = jsonPath;
        }

        @Override
        public void onEmptyLabel(String image, String json) {
            preResponse();
            Logger.w(TAG, "onEmptyLabel", "empty json = " + json);
            postResponse();
        }

        public final void onFailure(Call call, IOException e) {
            preResponse();
            onFailed(jsonPath);
            Logger.w(TAG, "LogCallback_onFailure", Throwables.getStackTraceAsString(e));
            postResponse();
        }

        @Override
        public final void onResponse(Call call, Response response) throws IOException {
            try {
                preResponse();
                ResponseBody body = response.body();
                boolean success = false;
                if(body != null){
                    String json = body.string();
                    Logger.d(TAG, "onResponse", "" + json);
                    try{
                        BDResponse bdres = new Gson().fromJson(json, BDResponse.class);
                        success = bdres != null && bdres.hasLogId();
                    }catch (Exception e){
                       //ignore
                    }
                }
                if(success){
                    onSuccess(jsonPath);
                }else {
                    onFailed(jsonPath);
                }
            }finally {
                response.close();
                postResponse();
            }
        }
        protected void onSuccess(String jsonPath){

        }
        protected void onFailed(String jsonPath){

        }
        protected void postResponse() {

        }

        protected void preResponse() {
            System.out.println(" preResponse >>>>>>>>>> start jsonPath = " + jsonPath);
        }
    }
}
