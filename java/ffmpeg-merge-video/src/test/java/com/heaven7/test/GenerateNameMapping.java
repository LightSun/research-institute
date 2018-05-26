package com.heaven7.test;

import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.test.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成名称映射
 */
public class GenerateNameMapping {

  public static void main(String[] args) {
      //F:\videos\story2
      File dir = new File("F:\\videos\\story4");
      List<String> videos = new ArrayList<>();
      FileHelper.getVideos(dir, videos);

      VisitServices.from(videos).groupService(new ResultVisitor<String, String>() {
          @Override
          public String visit(String s, Object param) {
              return VEGapUtils.getFileDir(s, 1, false);
          }
      }).fire(new MapFireVisitor<String, List<String>>() {
          @Override
          public Boolean visit(KeyValuePair<String, List<String>> pair, Object param) {
              String key = pair.getKey();
              List<String> list = pair.getValue();
              StringBuilder sb = new StringBuilder();
              for(int i = 0 , size = list.size() ; i < size ; i ++){
                  sb.append(VEGapUtils.getFileName(list.get(i)));
                  if(i != size - 1){
                      sb.append(",");
                  }
              }
              DefaultPrinter.getDefault().println(key + " = " + sb.toString());
              return true;
          }
      });
  }
}
