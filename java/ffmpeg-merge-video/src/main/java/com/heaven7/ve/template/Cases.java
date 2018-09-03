package com.heaven7.ve.template;

import com.google.gson.Gson;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.heaven7.ve.colorgap.ShotRecognition.parseShotCategory;

/**
 * @author heaven7
 */
public class Cases {

    public static final String KEY_SHOT_CATEGORY = "shot_category";

    private final Map<String, String> cases;
    private final boolean or;

    public Cases( Map<String, String> cases, boolean or) {
        this.or = or;
        this.cases = cases;
    }

    public int getShotCategoryFlags(){
        String str = cases.get(KEY_SHOT_CATEGORY);
        if(str.contains("|")){
            String[] strs = str.split("\\|");
            return computeCategory(strs);
        }
        return parseShotCategory(str);
    }

    private static int computeCategory(String[] categoryies){
        return VisitServices.from(Arrays.asList(categoryies)).map(new ResultVisitor<String, Integer>() {
            @Override
            public Integer visit(String s, Object param) {
                return parseShotCategory(s);
            }
        }).pile(new PileVisitor<Integer>() {
            @Override
            public Integer visit(Object o, Integer integer, Integer integer2) {
                return integer + integer2;
            }
        });
    }

   /* public static void main(String[] args) {
        String json = ConfigUtil.loadResourcesAsString("table/template_dress.json");
        TemplateData data = new Gson().fromJson(json, TemplateData.class);
        System.out.println(data);

        String dir = "E:\\tmp\\face_data";
        List<String> files = FileUtils.getFiles(new File(dir), new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });
        VisitServices.from(files).fireWithIndex(new FireIndexedVisitor<String>() {
            @Override
            public Void visit(Object param, String s, int index, int size) {
                new File(s).renameTo(new File(s + "_rects.csv"));
                return null;
            }
        });
        System.out.println(files);
    }*/
}
