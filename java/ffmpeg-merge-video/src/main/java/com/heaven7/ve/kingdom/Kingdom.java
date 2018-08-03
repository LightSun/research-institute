package com.heaven7.ve.kingdom;

import com.google.gson.GsonBuilder;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.Context;
import com.heaven7.utils.TextUtils;
import com.vida.common.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * @author heaven7
 */
public abstract class Kingdom {

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

    /** override this must call super(). */
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

    public static Kingdom fromKingdomData(String resPath, GsonBuilder builder) {
        Reader reader = null;
        try {
            String json = IOUtils.readString(reader = new InputStreamReader(ConfigUtil.loadResourcesAsStream(resPath)));
            KingdomData data = builder.create().fromJson(json, KingdomData.class);
            return new JsonKingdom(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void loadVocabulary(Context context, String path) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(ConfigUtil.loadResourcesAsStream(path)));
            String line;
            while ((line = in.readLine()) != null) {
                String[] strs = line.split(",");
                final int index;
                try {//guard  index
                    index = Integer.parseInt(strs[0]);
                } catch (NumberFormatException e) {
                    continue;
                }
                String name = strs[3];
                if (!TextUtils.isEmpty(name)) {
                    ID_TAG.put(index, name);
                    TAG_ID.put(name, index);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static int getTagId(String tag) {
        return TAG_ID.get(tag);
    }

    public static String getTagStr(int index) {
        return ID_TAG.get(index);
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
                return getTagItem(mNounMap, index);

            case TYPE_ADJECTIVE:
                return getTagItem(mAdjMap, index);

            case TYPE_SCOPE:
                return getTagItem(mScopeMap, index);

            case TYPE_ALL:
                TagItem result = getTagItem(mNounMap, index);
                if (result == null) {
                    result = getTagItem(mAdjMap, index);
                }
                if (result == null) {
                    result = getTagItem(mScopeMap, index);
                }
                return result;
            default:
                throw new IllegalArgumentException("wrong type = " + type);
        }
    }

    private static List<Integer> getTagIds(Map<String, List<TagItem>> map, String tag) {
        List<TagItem> items = map.get(tag);
        if (items == null) {
            return null;
        }
        return getIds(items);
    }

    private static TagItem getTagItem(Map<String, List<TagItem>> map, int index) {
        for (List<TagItem> list : map.values()) {
            for (TagItem item : list) {
                if (item.getIndex() == index) {
                    return item;
                }
            }
        }
        return null;
    }

    private static List<Integer> getIds(List<TagItem> list) {
        return VisitServices.from(list).map((tagItem, param) -> tagItem.getIndex()).getAsList();
    }

}
