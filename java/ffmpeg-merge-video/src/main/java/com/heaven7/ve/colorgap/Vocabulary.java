package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 领域词汇表
 * Created by heaven7 on 2018/4/11 0011.
 */

public class Vocabulary {

    /**
     * 名词
     */
    public static final int TYPE_WEDDING_NOUN = 1;
    /**
     * 领域
     */
    public static final int TYPE_WEDDING_DOMAIN = 2;
    /**
     * 形容词
     */
    public static final int TYPE_WEDDING_ADJ = 3;
    /**
     * 包含上面3种
     */
    public static final int TYPE_WEDDING_ALL = 4;

    public static final String VOCABULARY_FILE_NAME = "vocabulary.csv";
    /**
     * 婚礼名词 mapDataDir
     */
    private static final HashMap<String, WeddingTagItem[]> WEDDING_NOUN_DICT;
    /**
     * 婚礼形容词map
     */
    private static final HashMap<String, WeddingTagItem[]> WEDDING_ADJ_DICT;
    /**
     * 领域词map
     */
    private static final HashMap<String, WeddingTagItem[]> WEDDING_DOMAIN_DICT;

    /** 主词，在计算 故事分类时需要去掉.这个tag的影响 */
    private static final Set<String> DOMAIN_TAGS = new HashSet<>(Arrays.asList("Wedding"));

    private static final SparseArray<String> ID_TAG = new SparseArray<>();
    private static final HashMap<String, Integer> TAG_ID = new HashMap<>();

    private static final Set<Integer> WEDDING_NOUN_TAGS = new HashSet<>();
    private static final Set<Integer> WEDDING_DOMAIN_TAGS = new HashSet<>();
    private static final Set<Integer> WEDDING_ADJ_TAGS = new HashSet<>();

    public static int getTagId(String tag) {
        return TAG_ID.get(tag);
    }

    public static String getTagStr(int index) {
        return ID_TAG.get(index);
    }
    /** 是否是主词 */
    public static boolean isDomainTag(int index){
        return DOMAIN_TAGS.contains(getTagStr(index));
    }

    //=========================== start wedding ============================

    /**
     * 获取名词tag. ids
     */
    public static int[] getTagIdsFromWeddingNounTag(String weddingTag) {
        WeddingTagItem[] items = WEDDING_NOUN_DICT.get(weddingTag);
        if (items == null) {
            return null;
        }
        int[] result = new int[items.length];
        int i = 0;
        for (WeddingTagItem item : items) {
            result[i++] = item.getIndex();
        }
        return result;
    }

    /**
     * 获取形容词tag. ids
     */
    public static int[] getTagIdsFromWeddingAdjTag(String weddingTag) {
        WeddingTagItem[] items = WEDDING_ADJ_DICT.get(weddingTag);
        if (items == null) {
            return null;
        }
        int[] result = new int[items.length];
        int i = 0;
        for (WeddingTagItem item : items) {
            result[i++] = item.getIndex();
        }
        return result;
    }
    // 判断tag是否在某个词表之类
    public static boolean isTagInDict(int tagIdx, int type){
        Set<Integer> set;
        switch (type){
            case TYPE_WEDDING_NOUN:
                set = WEDDING_NOUN_TAGS;
                break;

            case TYPE_WEDDING_DOMAIN:
                set = WEDDING_DOMAIN_TAGS;
                break;

            case TYPE_WEDDING_ADJ:
                set = WEDDING_ADJ_TAGS;
                break;

            case TYPE_WEDDING_ALL:
                set = new HashSet<>();
                set.addAll(WEDDING_NOUN_TAGS);
                set.addAll(WEDDING_DOMAIN_TAGS);
                set.addAll(WEDDING_ADJ_TAGS);
                break;

            default:
                throw new UnsupportedOperationException("type = " + type);
        }
        return set.contains(tagIdx);
    }

    public static WeddingTagItem getWeddingTagItem(int index, int type) {
        switch (type) {
            case TYPE_WEDDING_NOUN: {
                WeddingTagItem item = getWeddingTagItem0(index, WEDDING_NOUN_DICT);
                if (item != null) {
                    item.shotTypeScore = 3f;
                    return item;
                }
            } break;

            case TYPE_WEDDING_ADJ: {
                WeddingTagItem item = getWeddingTagItem0(index, WEDDING_ADJ_DICT);
                if (item != null) {
                    item.shotTypeScore = 1f;
                    return item;
                }
            } break;

            case TYPE_WEDDING_DOMAIN:{
                WeddingTagItem item = getWeddingTagItem0(index, WEDDING_DOMAIN_DICT);
                if (item != null) {
                    item.shotTypeScore = 1f;
                    return item;
                }
            } break;

            case TYPE_WEDDING_ALL: {
                //noun -> domain -> adj
                WeddingTagItem item = getWeddingTagItem(index, TYPE_WEDDING_NOUN);
                if (item == null) {
                    item = getWeddingTagItem(index, TYPE_WEDDING_DOMAIN);
                }
                if (item == null) {
                    item = getWeddingTagItem(index, TYPE_WEDDING_ADJ);
                }
                return item;
            }

            default:
                throw new UnsupportedOperationException("type = " + type);
        }
        return null;
    }

    private static WeddingTagItem getWeddingTagItem0(int index, Map<String, WeddingTagItem[]> dict) {
        for (Map.Entry<String, WeddingTagItem[]> en : dict.entrySet()) {
            String key = en.getKey();
            for (WeddingTagItem item : en.getValue()) {
                if (item.getIndex() == index) {
                    return new WeddingTagItem(item, key);
                }
            }
        }
        return null;
    }

    /**
     * 根据index获取 形容词tag.item
     */
    public static WeddingTagItem getAdjWeddingTagItem(int index) {
        for (Map.Entry<String, WeddingTagItem[]> en : WEDDING_ADJ_DICT.entrySet()) {
            for (WeddingTagItem item : en.getValue()) {
                if (item.getIndex() == index) {
                    return new WeddingTagItem(item, en.getKey());
                }
            }
        }
        return null;
    }

    /**
     * 根据index获取 名词tag.item
     */
    public static WeddingTagItem getNounWeedingTagItem(int index) {
        for (Map.Entry<String, WeddingTagItem[]> en : WEDDING_NOUN_DICT.entrySet()) {
            for (WeddingTagItem item : en.getValue()) {
                if (item.getIndex() == index) {
                    return new WeddingTagItem(item, en.getKey());
                }
            }
        }
        return null;
    }
    static {
        // loadVocabulary("data/table/" + VOCABULARY_FILE_NAME);
        WEDDING_NOUN_DICT = new HashMap<>();
        WEDDING_ADJ_DICT = new HashMap<>();
        WEDDING_DOMAIN_DICT = new HashMap<>();
        initDicts();
        initWeddingTagSets();
    }

    private static void initWeddingTagSets() {
        //noun
        for(WeddingTagItem[] val : WEDDING_NOUN_DICT.values()){
            for(WeddingTagItem item : val){
                WEDDING_NOUN_TAGS.add(item.getIndex());
            }
        }
        //domain
        for(WeddingTagItem[] val : WEDDING_DOMAIN_DICT.values()){
            for(WeddingTagItem item : val){
                WEDDING_DOMAIN_TAGS.add(item.getIndex());
            }
        }
        //adj
        for(WeddingTagItem[] val : WEDDING_ADJ_DICT.values()){
            for(WeddingTagItem item : val){
                WEDDING_ADJ_TAGS.add(item.getIndex());
            }
        }
    }

    private static void initDicts() {
        //领域词表
        WEDDING_DOMAIN_DICT.put("婚礼", new WeddingTagItem[]{
                new WeddingTagItem(78, "Wedding", 0.0, "mediumShot", "mediaCloseUp"),
        });
        WEDDING_DOMAIN_DICT.put("婚服", new WeddingTagItem[]{
                new WeddingTagItem(421, "Gown", 0.5, "mediumShot", "mediumLongShot"),
        });
        WEDDING_DOMAIN_DICT.put("婚服（包含男士礼服）", new WeddingTagItem[]{
                new WeddingTagItem(590, "Wedding Dress", 1.0, "mediumShot", "mediumLongShot"),
        });
        WEDDING_DOMAIN_DICT.put("礼服", new WeddingTagItem[]{
                new WeddingTagItem(153, "Dress", 0.5, "mediumShot", "mediaCloseUp"),
        });
        WEDDING_DOMAIN_DICT.put("新娘", new WeddingTagItem[]{
                new WeddingTagItem(190, "Bride", 0.0, "mediumShot", "mediaCloseUp"),
        });

        //名词
        WEDDING_NOUN_DICT.put("气球", new WeddingTagItem[]{
                new WeddingTagItem(695,"Balloon",2.0,"mediumShot","closeUp"),
        });
        WEDDING_NOUN_DICT.put("天空", new WeddingTagItem[]{
                new WeddingTagItem(223,"Weather",1.0,"longShot",""),
                new WeddingTagItem(502,"Sky",1.0,"longShot",""),
        });
        WEDDING_NOUN_DICT.put("跳舞", new WeddingTagItem[]{
                new WeddingTagItem(5,"Dance",1.5,"longShot","mediumShot"),
        });
        WEDDING_NOUN_DICT.put("摩天大楼", new WeddingTagItem[]{
                new WeddingTagItem(1214,"Skyscraper",1.0,"longShot",""),
        });
        WEDDING_NOUN_DICT.put("化妆", new WeddingTagItem[]{
                new WeddingTagItem(173,"Eye shadow",0.5,"closeUp",""),
                new WeddingTagItem(193,"Eye liner",0.5,"closeUp",""),
                new WeddingTagItem(195,"Mascara",0.5,"closeUp",""),
                new WeddingTagItem(235,"Lipstick",0.5,"closeUp",""),
        });

        WEDDING_NOUN_DICT.put("教堂", new WeddingTagItem[]{
                new WeddingTagItem(339,"Architecture",0.5,"longShot","mediumShot"),
                new WeddingTagItem(318,"Church (building)",0.5,"mediumLongShot",""),
        });
        WEDDING_NOUN_DICT.put("人物特写", new WeddingTagItem[]{
                new WeddingTagItem(282,"Skin",0.5,"mediaCloseUp",""),
        });
        WEDDING_NOUN_DICT.put("珠宝", new WeddingTagItem[]{
                new WeddingTagItem(1001,"Gemstone",0.5,"closeUp","mediaCloseUp"),
                new WeddingTagItem(1300,"Earring",0.5,"closeUp","mediaCloseUp"),
        });
        WEDDING_NOUN_DICT.put("游艇", new WeddingTagItem[]{
                new WeddingTagItem(87,"Boat",0.5,"mediumShot",""),
                new WeddingTagItem(498,"Motorboat",0.5,"mediumShot",""),
                new WeddingTagItem(579,"Yacht",0.5,"mediumShot",""),
        });

        WEDDING_NOUN_DICT.put("相机", new WeddingTagItem[]{
                new WeddingTagItem(155,"Camera",1.0,"closeUp",""),
                new WeddingTagItem(629,"Camera lens",1.0,"closeUp",""),
                new WeddingTagItem(566,"Digital camera",1.0,"closeUp",""),
        });
        WEDDING_NOUN_DICT.put("人群", new WeddingTagItem[]{
                new WeddingTagItem(46,"Choir",2.0,"mediumLongShot","mediumShot"),
        });
        WEDDING_NOUN_DICT.put("人群2", new WeddingTagItem[]{
                new WeddingTagItem(7,"Musician",2.0,"mediumLongShot","mediumShot"),
                new WeddingTagItem(49,"School",2.0,"mediumLongShot","mediumShot"),
                new WeddingTagItem(13,"Musical ensemble",2.0,"mediumLongShot","mediumShot"),
        });
        WEDDING_NOUN_DICT.put("树", new WeddingTagItem[]{
                new WeddingTagItem(137,"Tree",0.5,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("花艺", new WeddingTagItem[]{
                new WeddingTagItem(1403,"Flower bouquet",1.0,"mediaCloseUp","closeUp"),
                new WeddingTagItem(1023,"Rose",1.0,"mediaCloseUp","closeUp"),
                new WeddingTagItem(136,"Gardening",1.0,"mediaCloseUp","closeUp"),
                new WeddingTagItem(1545,"Floristry",1.0,"mediaCloseUp","closeUp"),
        });
        WEDDING_NOUN_DICT.put("旅馆", new WeddingTagItem[]{
                new WeddingTagItem(248,"Hotel",0.5,"longShot","mediumShot"),
        });
        WEDDING_NOUN_DICT.put("首饰与化妆", new WeddingTagItem[]{
                new WeddingTagItem(402,"Jewellery",1.0,"closeUp",""),
                new WeddingTagItem(907,"Necklace",1.0,"closeUp",""),
                new WeddingTagItem(829,"Bead",1.0,"closeUp",""),
                new WeddingTagItem(859,"Beadwork",1.0,"closeUp",""),
        });
        WEDDING_NOUN_DICT.put("手机", new WeddingTagItem[]{
                new WeddingTagItem(23,"Mobile phone",1.0,"mediaCloseUp",""),
                new WeddingTagItem(37,"Gadget",1.0,"mediaCloseUp",""),
                new WeddingTagItem(29,"Smartphone",1.0,"mediaCloseUp",""),
        });
        WEDDING_NOUN_DICT.put("水", new WeddingTagItem[]{
                new WeddingTagItem(42,"Fishing",0.5,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("中式婚服", new WeddingTagItem[]{
                new WeddingTagItem(2873,"Kimono",0.5,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("吃", new WeddingTagItem[]{
                new WeddingTagItem(215,"Eating",0.5,"mediaCloseUp","closeUp"),
        });
        WEDDING_NOUN_DICT.put("敞篷车", new WeddingTagItem[]{
                new WeddingTagItem(650,"Convertible",1.0,"mediumShot",""),
                new WeddingTagItem(2583,"Limousine",1.0,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("建筑", new WeddingTagItem[]{
                new WeddingTagItem(102,"Building",0.5,"veryLongShot","longShot"),
                new WeddingTagItem(425,"City",0.5,"veryLongShot","longShot"),
                new WeddingTagItem(113,"House",0.5,"veryLongShot","longShot"),
                new WeddingTagItem(2088,"City-Building game",0.5,"veryLongShot","longShot"),
        });
        WEDDING_NOUN_DICT.put("背景或脑后特写", new WeddingTagItem[]{
                new WeddingTagItem(106,"hairstyle",0.5,"mediaCloseUp",""),
                new WeddingTagItem(56,"hair",0.5,"mediaCloseUp",""),
        });

        WEDDING_NOUN_DICT.put("路面", new WeddingTagItem[]{
                new WeddingTagItem(108,"Skateboard",0.5,"mediaCloseUp",""),
        });
        WEDDING_NOUN_DICT.put("公寓", new WeddingTagItem[]{
                new WeddingTagItem(416,"Apartment",0.5,"mediumLongShot",""),
        });
        WEDDING_NOUN_DICT.put("桥", new WeddingTagItem[]{
                new WeddingTagItem(794,"Bridge",0.5,"longShot","mediumShot"),
        });
        WEDDING_NOUN_DICT.put("海", new WeddingTagItem[]{
                new WeddingTagItem(722,"Waterfall",1.5,"veryLongShot",""),
        });
        WEDDING_NOUN_DICT.put("食物", new WeddingTagItem[]{
                new WeddingTagItem(12,"Food",1.0,"mediumShot",""),
                new WeddingTagItem(32,"Recipe",1.0,"mediumShot",""),
                new WeddingTagItem(52,"Dish (food)",1.0,"mediumShot",""),
                new WeddingTagItem(58,"Cuisine",1.0,"mediumShot",""),
                new WeddingTagItem(26,"Cooking",1.0,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("风景", new WeddingTagItem[]{
                new WeddingTagItem(22,"Nature",1.0,"veryLongShot","mediaCloseUp"),
        });
        WEDDING_NOUN_DICT.put("鞋", new WeddingTagItem[]{
                new WeddingTagItem(206,"Shoe",1.0,"mediaCloseUp",""),
        });
        WEDDING_NOUN_DICT.put("植物", new WeddingTagItem[]{
                new WeddingTagItem(86,"Plant",0.5,"mediaCloseUp","mediumLongShot"),
        });

        WEDDING_NOUN_DICT.put("婚车", new WeddingTagItem[]{
                new WeddingTagItem(231,"SuperCar",0.5,"mediumShot","mediaCloseUp"),
                new WeddingTagItem(74,"Truck",0.5,"mediumShot","mediaCloseUp"),
                new WeddingTagItem(154,"Wheel",0.5,"mediumShot","mediaCloseUp"),
                new WeddingTagItem(70,"Driving",0.5,"mediumShot","mediaCloseUp"),
                new WeddingTagItem(11,"Motorsport",0.5,"mediumShot","mediaCloseUp"),
        });
        WEDDING_NOUN_DICT.put("戒指", new WeddingTagItem[]{
                new WeddingTagItem(1010,"Ring (jewellery)",2.5,"closeUp","mediaCloseUp"),
                new WeddingTagItem(3642,"Engagement ring",2.5,"closeUp","mediaCloseUp"),
                new WeddingTagItem(768,"Diamond",2.5,"closeUp","mediaCloseUp"),
        });
        WEDDING_NOUN_DICT.put("编织", new WeddingTagItem[]{
                new WeddingTagItem(405,"Braid",0.5,"closeUp",""),
        });
        WEDDING_NOUN_DICT.put("穹顶", new WeddingTagItem[]{
                new WeddingTagItem(144,"Amusement park",0.5,"longShot",""),
                new WeddingTagItem(321,"Roller coaster",0.5,"longShot",""),
        });

        WEDDING_NOUN_DICT.put("动物", new WeddingTagItem[]{
                new WeddingTagItem(10,"Animal",1.0,"mediumShot",""),
                new WeddingTagItem(71,"Dog",1.0,"mediumShot",""),
                new WeddingTagItem(166,"Cat",1.0,"mediumShot",""),
                new WeddingTagItem(48,"Pet",1.0,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("装饰", new WeddingTagItem[]{
                new WeddingTagItem(964,"Christmas tree",1.0,"mediaCloseUp",""),
                new WeddingTagItem(99,"Christmas",1.0,"mediaCloseUp",""),
        });
        WEDDING_NOUN_DICT.put("手", new WeddingTagItem[]{
                new WeddingTagItem(238,"Nail (anatomy)",1.0,"closeUp",""),
                new WeddingTagItem(324,"Finger",1.0,"closeUp",""),
                new WeddingTagItem(338,"Manicure",1.0,"closeUp",""),
                new WeddingTagItem(300,"Nail art",1.0,"closeUp",""),
                new WeddingTagItem(304,"Nail polish",1.0,"closeUp",""),
                new WeddingTagItem(307,"Hand",1.0,"closeUp",""),
        });
        WEDDING_NOUN_DICT.put("西装", new WeddingTagItem[]{
                new WeddingTagItem(986,"Suit (clothing)",0.5,"mediumShot",""),
                new WeddingTagItem(785,"Jeans",0.5,"mediumShot",""),
                new WeddingTagItem(880,"Trousers",0.5,"mediumShot",""),
        });
        WEDDING_NOUN_DICT.put("车", new WeddingTagItem[]{
                new WeddingTagItem(1,"Vehicle",0.5,"mediumShot","veryLongShot"),
                new WeddingTagItem(4,"Car",0.5,"mediumShot","veryLongShot"),
                new WeddingTagItem(41,"Sports car",0.5,"mediumShot","veryLongShot"),
        });
        WEDDING_NOUN_DICT.put("纹身", new WeddingTagItem[]{
                new WeddingTagItem(609,"Tattoo",1.0,"closeUp",""),
        });

        //形容词
        WEDDING_ADJ_DICT.put("朦胧感的", new WeddingTagItem[]{
                new WeddingTagItem(6, "Animation", 0.5, "mediaCloseUp", "")
        });
        WEDDING_ADJ_DICT.put("感动的", new WeddingTagItem[]{
                new WeddingTagItem(325,"Vampire",1.5,"closeUp","")
        });
        WEDDING_ADJ_DICT.put("高品质的画面", new WeddingTagItem[]{
                new WeddingTagItem(20,"Trailer (promotion)",1.0,"mediaCloseUp","mediumLongShot")
        });
        WEDDING_ADJ_DICT.put("电影感的", new WeddingTagItem[]{
                new WeddingTagItem(1014,"movieclips",1.0,"mediumShot","")
        });
        WEDDING_ADJ_DICT.put("可爱的", new WeddingTagItem[]{
                new WeddingTagItem(200,"Doll",2.0,"mediaCloseUp","mediumShot")
        });
        WEDDING_ADJ_DICT.put("艺术性的画面", new WeddingTagItem[]{
                new WeddingTagItem(9,"Music Video",0.5,"mediaCloseUp","")
        });
        WEDDING_ADJ_DICT.put("模特般的", new WeddingTagItem[]{
                new WeddingTagItem(254,"Runway",1.0,"mediumShot",""),
                new WeddingTagItem(390,"Model (person)",1.0,"mediumShot",""),
        });

        WEDDING_ADJ_DICT.put("人物特写", new WeddingTagItem[]{
                new WeddingTagItem(45,"Cosmetics",1.0,"closeUp",""),
        });
        WEDDING_ADJ_DICT.put("有灯光氛围的", new WeddingTagItem[]{
                new WeddingTagItem(3,"concert",0.5,"longShot","mediumLongShot"),
        });
        WEDDING_ADJ_DICT.put("戏剧的", new WeddingTagItem[]{
                new WeddingTagItem(364,"Sitcom",0.5,"mediumShot",""),
        });
        WEDDING_ADJ_DICT.put("时尚性的画面", new WeddingTagItem[]{
                new WeddingTagItem(28,"Fashion",0.5,"mediaCloseUp",""),
        });
        WEDDING_ADJ_DICT.put("优秀风光片", new WeddingTagItem[]{
                new WeddingTagItem(18,"outdoor recreation",1.0,"mediumLongShot","longShot"),
        });
        WEDDING_ADJ_DICT.put("更标准的人像", new WeddingTagItem[]{
                new WeddingTagItem(293,"Slide show",1.0,"mediumShot",""),
        });
        WEDDING_ADJ_DICT.put("更婚礼的人像", new WeddingTagItem[]{
                new WeddingTagItem(2269,"Wedding photography",1.0,"mediumShot",""),
        });
        WEDDING_ADJ_DICT.put("色彩浓郁的画面", new WeddingTagItem[]{
                new WeddingTagItem(66,"Bollywood",1.0,"mediumShot","longShot"),
        });
        WEDDING_ADJ_DICT.put("更艺术的人像", new WeddingTagItem[]{
                new WeddingTagItem(121,"Photography",0.5,"mediumShot",""),
        });
    }

    public static class WeddingTagItem {
        private int index;
        private String name;
        private double score;
        private String shotType1;
        private String shotType2;
        private String desc;
        private float shotTypeScore; // 3 or 1

        public WeddingTagItem(int index, String name, double score, String shotType1, String shotType2) {
            this(index, name, score, shotType1, shotType2, null);
        }

        public WeddingTagItem(int index, String name, double score, String shotType1, String shotType2, String desc) {
            this.index = index;
            this.name = name;
            this.score = score;
            this.shotType1 = shotType1;
            this.shotType2 = shotType2;
            this.desc = desc;
        }

        public WeddingTagItem(WeddingTagItem item, String desc) {
            this(item.index, item.name, item.score, item.shotType1, item.shotType2, desc);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getShotType1() {
            return shotType1;
        }

        public void setShotType1(String shotType1) {
            this.shotType1 = shotType1;
        }

        public String getShotType2() {
            return shotType2;
        }

        public void setShotType2(String shotType2) {
            this.shotType2 = shotType2;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Float getShotTypeScore() {
            return shotTypeScore;
        }
    }
}
