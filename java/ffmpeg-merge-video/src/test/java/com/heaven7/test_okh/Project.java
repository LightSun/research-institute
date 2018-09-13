package com.heaven7.test_okh;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * project info
 *
 * @author heaven7
 */
public class Project{

    public static final int STATE_NEW = 1;              // 首次制作完成后。
    public static final int STATE_UNPROCESSED = 2;      // 创建后没有处理
    public static final int STATE_DONE = 3;             // 制作完成后，new以后点击了预览等
    public static final int STATE_DOING = 4;            // 制作中
    public static final int STATE_GENERATE_FAILED = 5;  // 制作失败

    public static final int RATE_TYPE_3_4 = 1;
    public static final int RATE_TYPE_16_9 = 2;
    public static final int RATE_TYPE_7_9 = 3;

    public static final int COLOR_TYPE_WHITE = 1;
    public static final int COLOR_TYPE_BLACK = 2;
    public static final int COLOR_TYPE_NONE = 0;

    public static final int TEMPLATE_DEFAULT = 1;

    public static final int TYPE_JD = 1;
    public static final int TYPE_TAOBAO = 2;
    public static final int SERIES_TYPE_SINGLE = 0; //单品
    public static final int SERIES_TYPE_SERIES = 1; //系列

    private Long uid;

    @SerializedName("project_id")
    private Long id;

    private int state = STATE_UNPROCESSED; //项目状态
    private String name;
    private long create_time;
    private long update_time;
    private String coverUrl; // 封面图

    private String logoPath;

    private Integer bgColor = COLOR_TYPE_WHITE; //  背景色

    private String worksPath; // 完成的作品路径

    // 比例类型
    private Integer rate_type = RATE_TYPE_16_9;

    private Long duration = 30 * 1000L; //25s

    private Integer template_type = TEMPLATE_DEFAULT; //模版类型
    private Integer commodity_series = SERIES_TYPE_SINGLE;

    private ThirdCommodity commodity;

    private List<CommodityCategory> categories;

    private List<MediaInfo> mediaInfos;

    private CommodityCategory category;
    private CommodityCategory categorySec;

    public CommodityCategory getCategory() {
        return category;
    }
    public void setCategory(CommodityCategory category) {
        this.category = category;
    }

    public CommodityCategory getCategorySec() {
        return categorySec;
    }
    public void setCategorySec(CommodityCategory categorySec) {
        this.categorySec = categorySec;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long categoryId;
    public long categorySecId;

    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategorySecId() {
        return categorySecId;
    }
    public void setCategorySecId(long categorySecId) {
        this.categorySecId = categorySecId;
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

    public String getWorksPath() {
        return worksPath;
    }

    public void setWorksPath(String worksPath) {
        this.worksPath = worksPath;
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


    public List<MediaInfo> getMedia_infos() {
        return mediaInfos;
    }
    public void setMedia_infos(List<MediaInfo> media_infos) {
        this.mediaInfos = media_infos;
    }

    public ThirdCommodity getCommodity() {
        return commodity;
    }

    public void setCommodity(ThirdCommodity commodity) {
        this.commodity = commodity;
    }

    public void addMediaInfos(List<MediaInfo> infos) {
        if(mediaInfos == null){
            mediaInfos = new ArrayList<>();
        }
        mediaInfos.addAll(infos);
    }

    public static String getStateString(int state) {
        switch (state) {
            case Project.STATE_DOING:
                return "STATE_DOING";

            case Project.STATE_DONE:
                return "STATE_DONE";

            case Project.STATE_NEW:
                return "STATE_NEW";

            case Project.STATE_UNPROCESSED:
                return "STATE_UNPROCESSED";

            case Project.STATE_GENERATE_FAILED:
                return "STATE_GENERATE_FAILED";

            default:
                return "unknown";
        }
    }

    public static class MediaInfo {
        public static final int TYPE_IMAGE = 1;
        public static final int TYPE_VIDEO = 2;

        private Long id;

        private Integer type;
        private String savePath;
        private boolean isRelativePath;

        private String filename;

        //only for batch image
        private String tfsConfigPath;

        //only for batch image
        private String faceConfigPath;

        private String facePath;

        private String tagPath;

        private String tfrecordPath;

        private String pid;  //project_id

        private String file_md5;

        public String getFile_md5() {
            return file_md5;
        }
        public void setFile_md5(String file_md5) {
            this.file_md5 = file_md5;
        }

        public String getPid() {
            return pid;
        }
        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public boolean isRelativePath() {
            return isRelativePath;
        }

        public void setRelativePath(boolean relativePath) {
            isRelativePath = relativePath;
        }

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public String getTfrecordPath() {
            return tfrecordPath;
        }

        public void setTfrecordPath(String tfrecordPath) {
            this.tfrecordPath = tfrecordPath;
        }

        public String getTfsConfigPath() {
            return tfsConfigPath;
        }

        public void setTfsConfigPath(String tfsConfigPath) {
            this.tfsConfigPath = tfsConfigPath;
        }

        public String getFaceConfigPath() {
            return faceConfigPath;
        }

        public void setFaceConfigPath(String faceConfigPath) {
            this.faceConfigPath = faceConfigPath;
        }

        public String getFacePath() {
            return facePath;
        }

        public void setFacePath(String facePath) {
            this.facePath = facePath;
        }

        public String getTagPath() {
            return tagPath;
        }

        public void setTagPath(String tagPath) {
            this.tagPath = tagPath;
        }
    }

    public static class CommodityCategory {
        private Long id;
        private Long uid;
        private Long fid;       //parent_id
        private String name;
        private Long cid;       // category id
        private Long index_id;       // 排序
        private int type = TYPE_JD;  //平台分类
        private long level;
        private boolean is_parent;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public Long getFid() {
            return fid;
        }

        public void setFid(Long fid) {
            this.fid = fid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getCid() {
            return cid;
        }

        public void setCid(Long cid) {
            this.cid = cid;
        }

        public Long getIndex_id() {
            return index_id;
        }

        public void setIndex_id(Long index_id) {
            this.index_id = index_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getLevel() {
            return level;
        }

        public void setLevel(long level) {
            this.level = level;
        }

        public boolean isIs_parent() {
            return is_parent;
        }

        public void setIs_parent(boolean is_parent) {
            this.is_parent = is_parent;
        }
    }

    // 第三方商品信息
    public static class ThirdCommodity {

        private Long id;

        private Long commodity_id;
        private Long category_id;
        private int series_type = SERIES_TYPE_SINGLE;    // 系列还是单品。
        private int type = TYPE_JD;                      // jd, taobao
        private String transparent_images;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCommodity_id() {
            return commodity_id;
        }

        public void setCommodity_id(Long commodity_id) {
            this.commodity_id = commodity_id;
        }

        public Long getCategory_id() {
            return category_id;
        }

        public void setCategory_id(Long category_id) {
            this.category_id = category_id;
        }

        public int getSeries_type() {
            return series_type;
        }

        public void setSeries_type(int series_type) {
            this.series_type = series_type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTransparent_images() {
            return transparent_images;
        }

        public void setTransparent_images(String transparent_images) {
            this.transparent_images = transparent_images;
        }
    }
}
