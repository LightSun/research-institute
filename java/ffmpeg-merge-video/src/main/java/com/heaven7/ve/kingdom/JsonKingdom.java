package com.heaven7.ve.kingdom;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.helper.MainFaceRecognizer;
import com.heaven7.ve.colorgap.helper.PersonCountRecognizer;
import com.heaven7.ve.colorgap.helper.ShotTypeRecognizer;
import com.heaven7.ve.template.TemplateData;

import java.util.List;

/**
 * create kingdom from json data.
 * @author heaven7
 */
public class JsonKingdom extends Kingdom{

    private ShotTypeRecognizer mShotTypeReg;
    private PersonCountRecognizer mPersonCountReg;

    public JsonKingdom(KingdomData data) {
        super();
        List<String> subjects = data.getSubjects();
        if(!Predicates.isEmpty(subjects)){
            addSubjects(subjects);
        }
        List<KingdomData.MapData> maps = data.getMaps();
        if(!Predicates.isEmpty(maps)){
            VisitServices.from(maps).fire(new FireVisitor<KingdomData.MapData>() {
                @Override
                public Boolean visit(KingdomData.MapData mapData, Object param) {
                    final int type = mapData.getType();
                    List<KingdomData.TagItemData> itemDatas = mapData.getItemDatas();
                    if (Predicates.isEmpty(itemDatas)) {
                        return null;
                    }
                    VisitServices.from(itemDatas).fire(new FireVisitor<KingdomData.TagItemData>() {
                        @Override
                        public Boolean visit(KingdomData.TagItemData tagItemData, Object param) {
                            putTagItems(type, tagItemData.getTag(), tagItemData.getItems());
                            return null;
                        }
                    });
                    return null;
                }
            });
        }

        List<ModuleData> moduleDatas = data.getModuleDatas();
        if(!Predicates.isEmpty(moduleDatas)){
            addModuleDatas(moduleDatas);
        }
        //Recognizers
        if(data.getShotTypeMap() != null){
            mShotTypeReg = new ShotTypeRecognizer(data.getShotTypeMap());
        }
        if(data.getPersonCountMap() != null){
            mPersonCountReg = new PersonCountRecognizer(data.getPersonCountMap());
        }
        //init
        init();
    }

    @Override
    protected float getPersonCountScoreImpl(int personCount) {
        if(mPersonCountReg != null){
            return mPersonCountReg.getScore(personCount);
        }
        return super.getPersonCountScoreImpl(personCount);
    }

    @Override
    public float getShotTypeScore(int shotType) {
        if(mShotTypeReg != null){
            return mShotTypeReg.getScore(shotType);
        }
        return super.getShotTypeScore(shotType);
    }
}
