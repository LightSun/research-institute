package com.heaven7.test_okh;

import com.google.gson.annotations.Expose;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;


/**
 * project info
 *
 * @author heaven7
 */
public class Project {

    public static final int STATE_NEW = 1;           // 首次制作完成后。
    public static final int STATE_UNPROCESSED = 2;   // 创建后没有处理
    public static final int STATE_DONE = 3;          // 制作完成后，new以后点击了预览等
    public static final int STATE_DOING = 4;         // 制作中
    //public static final int STATE_ANALYSING   = 5;   // 分析数据中.

    public static final int RATE_TYPE_3_4 = 1;
    public static final int RATE_TYPE_16_9 = 2;
    public static final int RATE_TYPE_7_9 = 2;

    public static final int TEMPLATE_DEFAULT = 1;

    public static final int TYPE_JD = 1;
    public static final int TYPE_TAOBAO = 2;
    public static final int SERIES_TYPE_SINGLE = 0; //单品
    public static final int SERIES_TYPE_SERIES = 1; //系列

    private int state = STATE_UNPROCESSED; //项目状态
    private String name;
    private long create_time;
    private long update_time;
    private String coverUrl; // 封面图

    private String logoPath;

    private Integer bgColor; //  背景色

    @Expose(serialize = false, deserialize = false)
    private String mediaIds; // , 资源ids

    private String worksUrl; // 完成的作品路径

    @Expose(serialize = false, deserialize = false)
    private String third_category_ids; // ,

    // 比例类型
    private Integer rate_type = RATE_TYPE_16_9;

    private Long duration = 25 * 1000L; //25s

    private Integer template_type = TEMPLATE_DEFAULT; //模版类型
    private List<CommodityCategory> categories;

    private List<Long> publicMediaIds;
    private Long logoId;

    public Long getLogoId() {
        return logoId;
    }
    public void setLogoId(Long logoId) {
        this.logoId = logoId;
    }

    public List<Long> getPublicMediaIds() {
        return publicMediaIds;
    }

    public void setPublicMediaIds(List<Long> publicMediaIds) {
        this.publicMediaIds = publicMediaIds;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(String mediaIds) {
        this.mediaIds = mediaIds;
    }

    public String getWorksUrl() {
        return worksUrl;
    }

    public void setWorksUrl(String worksUrl) {
        this.worksUrl = worksUrl;
    }

    public String getThird_category_ids() {
        return third_category_ids;
    }

    public void setThird_category_ids(String third_category_ids) {
        this.third_category_ids = third_category_ids;
    }

    public int getRate_type() {
        return rate_type;
    }

    public void setRate_type(int rate_type) {
        this.rate_type = rate_type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(int template_type) {
        this.template_type = template_type;
    }

    public List<CommodityCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CommodityCategory> categories) {
        this.categories = categories;
    }


    public static class CommodityCategory {
        private Long id;
        private String name;
        private Long cid;       // category id
        private Long fid ;

        public Long getFid() {
            return fid;
        }
        public void setFid(Long fid) {
            this.fid = fid;
        }

        public Long getCid() {
            return cid;
        }

        public void setCid(Long cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

}
