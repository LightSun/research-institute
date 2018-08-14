package com.heaven7.ve.kingdom;

import java.util.Arrays;

/**
 * @author heaven7
 */
public class GelailiyaKingdom extends Kingdom {

    public GelailiyaKingdom() {
        addSubjects(Arrays.asList("Wedding"));
        //scopes
        putTagItems(TYPE_SCOPE, "婚礼", new TagItem[]{
                new TagItem(78, "Wedding", 0.0, "mediumShot", "mediaCloseUp"),
        });
        putTagItems(TYPE_SCOPE, "婚服", new TagItem[]{
                new TagItem(421, "Gown", 0.5, "mediumShot", "mediumLongShot"),
        });
        putTagItems(TYPE_SCOPE, "婚服（包含男士礼服）", new TagItem[]{
                new TagItem(590, "Wedding Dress", 1.0, "mediumShot", "mediumLongShot"),
        });
        putTagItems(TYPE_SCOPE, "礼服", new TagItem[]{
                new TagItem(153, "Dress", 0.5, "mediumShot", "mediaCloseUp"),
        });
        putTagItems(TYPE_SCOPE, "新娘", new TagItem[]{
                new TagItem(190, "Bride", 0.0, "mediumShot", "mediaCloseUp"),
        });

        //TYPE_NOUN
        putTagItems(TYPE_NOUN, "气球", new TagItem[]{
                new TagItem(695,"Balloon",2.0,"mediumShot","closeUp"),
        });
        putTagItems(TYPE_NOUN, "天空", new TagItem[]{
                new TagItem(223,"Weather",1.0,"longShot",""),
                new TagItem(502,"Sky",1.0,"longShot",""),
        });
        putTagItems(TYPE_NOUN, "跳舞", new TagItem[]{
                new TagItem(5,"Dance",1.5,"longShot","mediumShot"),
        });
        putTagItems(TYPE_NOUN, "摩天大楼", new TagItem[]{
                new TagItem(1214,"Skyscraper",1.0,"longShot",""),
        });
        putTagItems(TYPE_NOUN, "化妆", new TagItem[]{
                new TagItem(173,"Eye shadow",0.5,"closeUp",""),
                new TagItem(193,"Eye liner",0.5,"closeUp",""),
                new TagItem(195,"Mascara",0.5,"closeUp",""),
                new TagItem(235,"Lipstick",0.5,"closeUp",""),
        });

        putTagItems(TYPE_NOUN, "教堂", new TagItem[]{
                new TagItem(339,"Architecture",0.5,"longShot","mediumShot"),
                new TagItem(318,"Church (building)",0.5,"mediumLongShot",""),
        });
        putTagItems(TYPE_NOUN, "人物特写", new TagItem[]{
                new TagItem(282,"Skin",0.5,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "珠宝", new TagItem[]{
                new TagItem(1001,"Gemstone",0.5,"closeUp","mediaCloseUp"),
                new TagItem(1300,"Earring",0.5,"closeUp","mediaCloseUp"),
        });
        putTagItems(TYPE_NOUN, "游艇", new TagItem[]{
                new TagItem(87,"Boat",0.5,"mediumShot",""),
                new TagItem(498,"Motorboat",0.5,"mediumShot",""),
                new TagItem(579,"Yacht",0.5,"mediumShot",""),
        });

        putTagItems(TYPE_NOUN, "相机", new TagItem[]{
                new TagItem(155,"Camera",1.0,"closeUp",""),
                new TagItem(629,"Camera lens",1.0,"closeUp",""),
                new TagItem(566,"Digital camera",1.0,"closeUp",""),
        });
        putTagItems(TYPE_NOUN, "人群", new TagItem[]{
                new TagItem(46,"Choir",2.0,"mediumLongShot","mediumShot"),
        });
        putTagItems(TYPE_NOUN, "人群2", new TagItem[]{
                new TagItem(7,"Musician",2.0,"mediumLongShot","mediumShot"),
                new TagItem(49,"School",2.0,"mediumLongShot","mediumShot"),
                new TagItem(13,"Musical ensemble",2.0,"mediumLongShot","mediumShot"),
        });
        putTagItems(TYPE_NOUN, "树", new TagItem[]{
                new TagItem(137,"Tree",0.5,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "花艺", new TagItem[]{
                new TagItem(1403,"Flower bouquet",1.0,"mediaCloseUp","closeUp"),
                new TagItem(1023,"Rose",1.0,"mediaCloseUp","closeUp"),
                new TagItem(136,"Gardening",1.0,"mediaCloseUp","closeUp"),
                new TagItem(1545,"Floristry",1.0,"mediaCloseUp","closeUp"),
        });
        putTagItems(TYPE_NOUN, "旅馆", new TagItem[]{
                new TagItem(248,"Hotel",0.5,"longShot","mediumShot"),
        });
        putTagItems(TYPE_NOUN, "首饰与化妆", new TagItem[]{
                new TagItem(402,"Jewellery",1.0,"closeUp",""),
                new TagItem(907,"Necklace",1.0,"closeUp",""),
                new TagItem(829,"Bead",1.0,"closeUp",""),
                new TagItem(859,"Beadwork",1.0,"closeUp",""),
        });

        putTagItems(TYPE_NOUN,"手机", new TagItem[]{
                new TagItem(23,"Mobile phone",1.0,"mediaCloseUp",""),
                new TagItem(37,"Gadget",1.0,"mediaCloseUp",""),
                new TagItem(29,"Smartphone",1.0,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "水", new TagItem[]{
                new TagItem(42,"Fishing",0.5,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "中式婚服", new TagItem[]{
                new TagItem(2873,"Kimono",0.5,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "吃", new TagItem[]{
                new TagItem(215,"Eating",0.5,"mediaCloseUp","closeUp"),
        });
        putTagItems(TYPE_NOUN, "敞篷车", new TagItem[]{
                new TagItem(650,"Convertible",1.0,"mediumShot",""),
                new TagItem(2583,"Limousine",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "建筑", new TagItem[]{
                new TagItem(102,"Building",0.5,"veryLongShot","longShot"),
                new TagItem(425,"City",0.5,"veryLongShot","longShot"),
                new TagItem(113,"House",0.5,"veryLongShot","longShot"),
                new TagItem(2088,"City-Building game",0.5,"veryLongShot","longShot"),
        });
        putTagItems(TYPE_NOUN, "背景或脑后特写", new TagItem[]{
                new TagItem(106,"hairstyle",0.5,"mediaCloseUp",""),
                new TagItem(56,"hair",0.5,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "路面", new TagItem[]{
                new TagItem(108,"Skateboard",0.5,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "公寓", new TagItem[]{
                new TagItem(416,"Apartment",0.5,"mediumLongShot",""),
        });
        putTagItems(TYPE_NOUN, "桥", new TagItem[]{
                new TagItem(794,"Bridge",0.5,"longShot","mediumShot"),
        });
        putTagItems(TYPE_NOUN, "海", new TagItem[]{
                new TagItem(722,"Waterfall",1.5,"veryLongShot",""),
        });
        putTagItems(TYPE_NOUN, "食物", new TagItem[]{
                new TagItem(12,"Food",1.0,"mediumShot",""),
                new TagItem(32,"Recipe",1.0,"mediumShot",""),
                new TagItem(52,"Dish (food)",1.0,"mediumShot",""),
                new TagItem(58,"Cuisine",1.0,"mediumShot",""),
                new TagItem(26,"Cooking",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "风景", new TagItem[]{
                new TagItem(22,"Nature",1.0,"veryLongShot","mediaCloseUp"),
        });
        putTagItems(TYPE_NOUN, "鞋", new TagItem[]{
                new TagItem(206,"Shoe",1.0,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "植物", new TagItem[]{
                new TagItem(86,"Plant",0.5,"mediaCloseUp","mediumLongShot"),
        });
        putTagItems(TYPE_NOUN, "婚车", new TagItem[]{
                new TagItem(231,"SuperCar",0.5,"mediumShot","mediaCloseUp"),
                new TagItem(74,"Truck",0.5,"mediumShot","mediaCloseUp"),
                new TagItem(154,"Wheel",0.5,"mediumShot","mediaCloseUp"),
                new TagItem(70,"Driving",0.5,"mediumShot","mediaCloseUp"),
                new TagItem(11,"Motorsport",0.5,"mediumShot","mediaCloseUp"),
        });
        putTagItems(TYPE_NOUN, "戒指", new TagItem[]{
                new TagItem(1010,"Ring (jewellery)",2.5,"closeUp","mediaCloseUp"),
                new TagItem(3642,"Engagement ring",2.5,"closeUp","mediaCloseUp"),
                new TagItem(768,"Diamond",2.5,"closeUp","mediaCloseUp"),
        });
        putTagItems(TYPE_NOUN, "编织", new TagItem[]{
                new TagItem(405,"Braid",0.5,"closeUp",""),
        });
        putTagItems(TYPE_NOUN, "穹顶", new TagItem[]{
                new TagItem(144,"Amusement park",0.5,"longShot",""),
                new TagItem(321,"Roller coaster",0.5,"longShot",""),
        });
        putTagItems(TYPE_NOUN, "动物", new TagItem[]{
                new TagItem(10,"Animal",1.0,"mediumShot",""),
                new TagItem(71,"Dog",1.0,"mediumShot",""),
                new TagItem(166,"Cat",1.0,"mediumShot",""),
                new TagItem(48,"Pet",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "装饰", new TagItem[]{
                new TagItem(964,"Christmas tree",1.0,"mediaCloseUp",""),
                new TagItem(99,"Christmas",1.0,"mediaCloseUp",""),
        });
        putTagItems(TYPE_NOUN, "手", new TagItem[]{
                new TagItem(238,"Nail (anatomy)",1.0,"closeUp",""),
                new TagItem(324,"Finger",1.0,"closeUp",""),
                new TagItem(338,"Manicure",1.0,"closeUp",""),
                new TagItem(300,"Nail art",1.0,"closeUp",""),
                new TagItem(304,"Nail polish",1.0,"closeUp",""),
                new TagItem(307,"Hand",1.0,"closeUp",""),
        });
        putTagItems(TYPE_NOUN, "西装", new TagItem[]{
                new TagItem(986,"Suit (clothing)",0.5,"mediumShot",""),
                new TagItem(785,"Jeans",0.5,"mediumShot",""),
                new TagItem(880,"Trousers",0.5,"mediumShot",""),
        });
        putTagItems(TYPE_NOUN, "车", new TagItem[]{
                new TagItem(1,"Vehicle",0.5,"mediumShot","veryLongShot"),
                new TagItem(4,"Car",0.5,"mediumShot","veryLongShot"),
                new TagItem(41,"Sports car",0.5,"mediumShot","veryLongShot"),
        });
        putTagItems(TYPE_NOUN, "纹身", new TagItem[]{
                new TagItem(609,"Tattoo",1.0,"closeUp",""),
        });
        //adj
        putTagItems(TYPE_ADJECTIVE, "朦胧感的", new TagItem[]{
                new TagItem(6, "Animation", 0.5, "mediaCloseUp", ""),
        });
        putTagItems(TYPE_ADJECTIVE, "感动的", new TagItem[]{
                new TagItem(325,"Vampire",1.5,"closeUp",""),
        });
        putTagItems(TYPE_ADJECTIVE, "高品质的画面", new TagItem[]{
                new TagItem(20,"Trailer (promotion)",1.0,"mediaCloseUp","mediumLongShot"),
        });
        putTagItems(TYPE_ADJECTIVE, "电影感的", new TagItem[]{
                new TagItem(1014,"movieclips",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_ADJECTIVE, "可爱的", new TagItem[]{
                new TagItem(200,"Doll",2.0,"mediaCloseUp","mediumShot"),
        });
        putTagItems(TYPE_ADJECTIVE, "艺术性的画面", new TagItem[]{
                new TagItem(9,"Music Video",0.5,"mediaCloseUp",""),
        });
        putTagItems(TYPE_ADJECTIVE, "模特般的", new TagItem[]{
                new TagItem(254,"Runway",1.0,"mediumShot",""),
                new TagItem(390,"Model (person)",1.0,"mediumShot",""),
        });
        
        putTagItems(TYPE_ADJECTIVE, "人物特写", new TagItem[]{
                new TagItem(45,"Cosmetics",1.0,"closeUp",""),
        });
        putTagItems(TYPE_ADJECTIVE, "有灯光氛围的", new TagItem[]{
                new TagItem(3,"concert",0.5,"longShot","mediumLongShot"),
        });
        putTagItems(TYPE_ADJECTIVE, "戏剧的", new TagItem[]{
                new TagItem(364,"Sitcom",0.5,"mediumShot",""),
        });
        putTagItems(TYPE_ADJECTIVE, "时尚性的画面", new TagItem[]{
                new TagItem(28,"Fashion",0.5,"mediaCloseUp",""),
        });
        putTagItems(TYPE_ADJECTIVE, "优秀风光片", new TagItem[]{
                new TagItem(18,"outdoor recreation",1.0,"mediumLongShot","longShot"),
        });
        putTagItems(TYPE_ADJECTIVE, "更标准的人像", new TagItem[]{
                new TagItem(293,"Slide show",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_ADJECTIVE, "更婚礼的人像", new TagItem[]{
                new TagItem(2269,"Wedding photography",1.0,"mediumShot",""),
        });
        putTagItems(TYPE_ADJECTIVE, "色彩浓郁的画面", new TagItem[]{
                new TagItem(66,"Bollywood",1.0,"mediumShot","longShot"),
        });
        putTagItems(TYPE_ADJECTIVE, "更艺术的人像", new TagItem[]{
                new TagItem(121,"Photography",0.5,"mediumShot",""),
        });

        init();
    }

    @Override
    public boolean isGeLaiLiYa() {
        return true;
    }

    //override and add shot type score
    @Override
    public TagItem getTagItem(int index, int type) {
        TagItem item = super.getTagItem(index, type);
        if(item == null){
            return null;
        }
        switch (type){
            case TYPE_ALL:
                return item;

            case TYPE_NOUN:
                item.setShotTypeScore(3f);
                break;
            case TYPE_ADJECTIVE:
            case TYPE_SCOPE:
                item.setShotTypeScore(1f);
                break;

            default:
                throw new UnsupportedOperationException("wrong type = " + type);
        }
        return item;
    }
}
