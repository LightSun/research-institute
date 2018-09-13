package com.heaven7.ve.configs;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.utils.IPreRunDelegate;
import com.heaven7.ve.utils.MapRecognizer;
import com.heaven7.ve.utils.StringMapGsonAdapter;
import com.vida.common.Platform;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;

/**
 * the bootstrap data.
 * @author heaven7
 */
public class BootStrapData implements IPreRunDelegate{

    @SerializedName("test_type")
    private int testType;
    @SerializedName("template_dir")
    private String templateDir;
    @SerializedName("effect_dir")
    private String effectDir;
    private boolean debug;
    @SerializedName("effect_res_dir")
    private String effectResourceDir;

    @SerializedName("music_dir_map")
    @JsonAdapter(StringMapGsonAdapter.class)
    private Map<String, String> musicDirMap;
    private MapRecognizer<String> musicDirReg;

    @SerializedName("face_data_dir")
    private String faceDataDir; //can be null
    @SerializedName("highlight_data_dir")
    private String highLightDataDir; //can be null
    @SerializedName("tag_data_dir")
    private String tagDataDir; //can be null

    @SerializedName("debug_flags")
    private String debugFlags; //can be null. like: 32|16

    @SerializedName("default_kingdom_type")
    private int kingdomType; //can be null

    @SerializedName("media_sdk_dir")
    private String mediaSdkDir;
    @SerializedName("test_dir")
    private String testDir;

    //-------------------------------------------------------
    private static BootStrapData INSTANCE;
    private static WeakReference<Context> sWeakContext;

    public static BootStrapData get(Context context){
        if(Platform.getSystemType() == Platform.ANDROID){
            Throwables.checkNull(context);
            sWeakContext = new WeakReference<>(context);
            throw new RuntimeException("Android not support now.");
        }else{
            if(INSTANCE == null){
                String json = ConfigUtil.loadResourcesAsString("table/init.json");
                INSTANCE = new Gson().fromJson(json, BootStrapData.class);
                INSTANCE.prepare(context);
            }
            return INSTANCE;
        }
    }

    @Override
    public void prepare(Context param) {
         if(musicDirMap != null){
             musicDirReg = new MapRecognizer<String>(musicDirMap){};
         }
    }

    public String getTestDir() {
        return testDir;
    }
    public void setTestDir(String testDir) {
        this.testDir = testDir;
    }

    public String getMediaSdkDir() {
        return mediaSdkDir;
    }
    public void setMediaSdkDir(String mediaSdkDir) {
        this.mediaSdkDir = mediaSdkDir;
    }

    public String getMusicDir(){
        return musicDirReg != null ? musicDirReg.getValue(testType+"") : null;
    }
    public int getTestType() {
        return testType;
    }
    public void setTestType(int testType) {
        this.testType = testType;
    }

    public String getTemplateDir() {
        return templateDir;
    }
    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public String getEffectDir() {
        return effectDir;
    }
    public void setEffectDir(String effectDir) {
        this.effectDir = effectDir;
    }

    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getEffectResourceDir() {
        return effectResourceDir;
    }
    public void setEffectResourceDir(String effectResourceDir) {
        this.effectResourceDir = effectResourceDir;
    }
    public String getFaceDataDir() {
        return faceDataDir;
    }
    public void setFaceDataDir(String faceDataDir) {
        this.faceDataDir = faceDataDir;
    }

    public String getHighLightDataDir() {
        return highLightDataDir;
    }

    public void setHighLightDataDir(String highLightDataDir) {
        this.highLightDataDir = highLightDataDir;
    }

    public String getTagDataDir() {
        return tagDataDir;
    }

    public void setTagDataDir(String tagDataDir) {
        this.tagDataDir = tagDataDir;
    }

    public int getDebugFlags() {
        if(TextUtils.isEmpty(debugFlags)){
            return 0;
        }
        String[] strs = debugFlags.split("\\|");
        return VisitServices.from(Arrays.asList(strs)).map(new ResultVisitor<String, Integer>() {
            @Override
            public Integer visit(String s, Object param) {
                return Integer.valueOf(s);
            }
        }).pile(PileVisitor.INT_ADD);
    }

    public int getKingdomType() {
        return kingdomType;
    }
    public void setKingdomType(int kingdomType) {
        this.kingdomType = kingdomType;
    }
}
