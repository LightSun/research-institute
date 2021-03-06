package com.heaven7.ve.kingdom;

import com.google.gson.GsonBuilder;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.MetaInfoUtils;
import com.heaven7.ve.configs.DictionaryLoader;
import com.vida.common.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static com.heaven7.ve.kingdom.KingdomUtils.*;

/**
 * @author heaven7
 */
public abstract class Kingdom implements DictionaryLoader.Callback{

    public static final int TYPE_NOUN = 1;
    public static final int TYPE_ADJECTIVE = 2;
    public static final int TYPE_SCOPE = 3;
    public static final int TYPE_ALL = 4;

    private static final SparseArray<String> ID_TAG = new SparseArray<>();
    private static final HashMap<String, Integer> TAG_ID = new HashMap<>();

    //the [noun, adj , scope] mapDataDir
    private final HashMap<String, List<TagItem>> mNounMap = new HashMap<>();
    private final HashMap<String, List<TagItem>> mAdjMap = new HashMap<>();
    private final HashMap<String, List<TagItem>> mScopeMap = new HashMap<>();
    private final Set<String> mSubjects = new HashSet<>();
    private final Set<ModuleData> mModuleDatas = new HashSet<>();

    private final Set<Integer> mNounTagIds = new HashSet<>();
    private final Set<Integer> mAdjTagIds = new HashSet<>();
    private final Set<Integer> mScopeTagIds = new HashSet<>();

    private static Kingdom sDefault;

    /**
     * init tag sets.
     * override this must call super(). */
    protected void init() {
        for (List<TagItem> list : mNounMap.values()) {
            mNounTagIds.addAll(getIds(list));
        }
        for (List<TagItem> list : mAdjMap.values()) {
            mAdjTagIds.addAll(getIds(list));
        }
        for (List<TagItem> list : mScopeMap.values()) {
            mScopeTagIds.addAll(getIds(list));
        }
    }
    public void asDefault(){
        sDefault = this;
    }
    /** get person proportion. the tag index. */
    public ModuleData getModuleData(String name){
        return VisitServices.from(mModuleDatas).visitForQuery(new PredicateVisitor<ModuleData>() {
            @Override
            public Boolean visit(ModuleData pp, Object param) {
                return pp.getName().equals(name);
            }
        });
    }

    protected void addModuleDatas(Collection<ModuleData> data){
        mModuleDatas.addAll(data);
    }
    protected void addSubjects(Collection<String> subjects){
        mSubjects.addAll(subjects);
    }
    protected void putTagItems(int type, String tag, TagItem[] items){
        putTagItems(type, tag, Arrays.asList(items));
    }
    protected void putTagItems(int type, String tag, List<TagItem> items){
        switch (type) {
            case TYPE_NOUN:
                mNounMap.put(tag, items);
                break;

            case TYPE_ADJECTIVE:
                mAdjMap.put(tag, items);
                break;

            case TYPE_SCOPE:
                mScopeMap.put(tag, items);
                break;

            case TYPE_ALL:
                throw new UnsupportedOperationException("unsupport all for put data.");

            default:
                throw new IllegalArgumentException("wrong type = " + type);
        }
    }
    public static Kingdom getDefault(){
        return sDefault;
    }

    public static Kingdom fromKingdomData(KingdomData data) {
        return new JsonKingdom(data);
    }

    public static Kingdom fromKingdomData(Context context, String resPath, GsonBuilder builder) {
        String json = ResourceLoader.getDefault().loadFileAsString(context, resPath);
        KingdomData data = builder.create().fromJson(json, KingdomData.class);
        return new JsonKingdom(data);
    }

    public static int getTagId(String tag) {
        return TAG_ID.get(tag);
    }

    public static String getTagStr(int index) {
        return ID_TAG.get(index);
    }

    /**
     * indicate use montage as 'geLaiLiYa' or not. default is false.
     * @return true if used as 'GeLayLiYa'
     */
    public boolean isGeLaiLiYa(){
        return false;
    }

    /**
     * is subject or not
     *
     * @param index the tag index
     * @return true if is subject tag
     */
    public boolean isSubjectTag(int index) {
        return mSubjects.contains(getTagStr(index));
    }

    /**
     * get the tag ids as noun.
     *
     * @param tag the noun tag
     * @return the tag ids
     */
    public List<Integer> getTagIdsAsNoun(String tag) {
        return getTagIds(mNounMap, tag);
    }

    /**
     * get the tag ids as noun.
     *
     * @param tag the adjective tag
     * @return the tag ids
     */
    public List<Integer> getTagIdsAsAdjective(String tag) {
        return getTagIds(mAdjMap, tag);
    }

    /**
     * get the tag ids as noun.
     *
     * @param tag the scope tag
     * @return the tag ids
     */
    public List<Integer> getTagIdsAsScope(String tag) {
        return getTagIds(mScopeMap, tag);
    }

    public boolean isTagExists(int type, int tagId) {
        Set<Integer> tagIds;
        switch (type) {
            case TYPE_NOUN:
                tagIds = mNounTagIds;
                break;

            case TYPE_ADJECTIVE:
                tagIds = mAdjTagIds;
                break;

            case TYPE_SCOPE:
                tagIds = mScopeTagIds;
                break;

            case TYPE_ALL:
                tagIds = new HashSet<>();
                tagIds.addAll(mNounTagIds);
                tagIds.addAll(mAdjTagIds);
                tagIds.addAll(mScopeTagIds);
                break;

            default:
                throw new IllegalArgumentException("wrong type = " + type);
        }
        return tagIds.contains(tagId);
    }

    /**
     * get tag item by target index and type.
     * @param index the tag index
     * @param type the type. see {@linkplain #TYPE_NOUN} and etc.
     * @return the tag item. may be null if not found.
     */
    public TagItem getTagItem(int index, int type) {
        switch (type) {
            case TYPE_NOUN:
                return KingdomUtils.getTagItem(mNounMap, index);

            case TYPE_ADJECTIVE:
                return KingdomUtils.getTagItem(mAdjMap, index);

            case TYPE_SCOPE:
                return KingdomUtils.getTagItem(mScopeMap, index);

            case TYPE_ALL:
                TagItem result = getTagItem(index, TYPE_NOUN);
                if (result == null) {
                    result = getTagItem(index, TYPE_SCOPE);
                }
                if (result == null) {
                    result = getTagItem(index, TYPE_ADJECTIVE);
                }
                return result;
            default:
                throw new IllegalArgumentException("wrong type = " + type);
        }
    }

    @Override
    public final void onLoad(int index, String name) {
        ID_TAG.put(index, name);
        TAG_ID.put(name, index);
    }
    //-------------------------- special ===========================

    /**
     * get the score of shot-type
     * @param shotType the shot type.
     * @return the score of shot type.
     */
    public float getShotTypeScore(int shotType){
        switch (shotType){
            case MetaInfo.SHOT_TYPE_CLOSE_UP:
            case MetaInfo.SHOT_TYPE_MEDIUM_CLOSE_UP:
            case MetaInfo.SHOT_TYPE_LONG_SHORT:
            case MetaInfo.SHOT_TYPE_BIG_LONG_SHORT:
                return  1.5f;

            case MetaInfo.SHOT_TYPE_MEDIUM_SHOT:
            case MetaInfo.SHOT_TYPE_MEDIUM_LONG_SHOT:
                return  1f;

            case MetaInfo.SHOT_TYPE_NONE:
                return 0f;
        }
        throw new UnsupportedOperationException("unsupport shot type = " + MetaInfoUtils.getShotTypeString(shotType));
    }

    /**
     * get main face score
     * @param mainFaceCount the main face count
     * @return the main face score
     */
    public final float getMainFaceScore(int mainFaceCount){
        if(mainFaceCount <= 0){
            return 0f;
        }
        return getMainFaceScoreImpl(mainFaceCount);
    }


    /**
     * get person count score
     * @param personCount the person count
     * @return the person count score
     */
    public final float getPersonCountScore(int personCount){
        if(personCount <= 0){
            return 0f;
        }
        return getPersonCountScoreImpl(personCount);
    }

    protected float getPersonCountScoreImpl(int personCount) {
        if(personCount == 1){
            return 0f;
        }else if(personCount == 2){
            return 1f;
        }else if(personCount > 2 && personCount < 5){
            return 1f;
        }else{
            return 1f;
        }
    }

    protected float getMainFaceScoreImpl(int mainFaceCount) {
        if(mainFaceCount == 1){
            return 0f;
        }else if(mainFaceCount == 2){
            return 1f;
        }else if(mainFaceCount > 2 && mainFaceCount < 5){
            return 1f;
        }else{
            return 1f;
        }
    }

    //------------------------- private ----------------------------

}
