package com.heaven7.ve.kingdom;

import com.google.gson.Gson;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.filter.ShotCategoryFilter;
import com.heaven7.ve.template.Cases;
import com.heaven7.ve.template.TemplateData;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * @author heaven7
 */
public class FileResourceManager {

    private String templateName; //the name of template config
    private TemplateData templateData;
    private VETemplate mTemplate;

    public String getTemplate() {
        return templateName;
    }
    public void setTemplate(String template) {
        this.templateName = template;
    }

    public TemplateData getTemplateData() {
        return templateData;
    }

    /**
     * resolve some data with context
     * @param context the context.
     */
    public void resolve(ColorGapContext context) {
        String templateDir = context.getInitializeParam().getTemplateDir();
        String json = ConfigUtil.loadResourcesAsString(templateDir + "/" + this.templateName + ".json");
        templateData = new Gson().fromJson(json, TemplateData.class);
    }

    public VETemplate getVETemplate(){
        if(mTemplate != null){
            return mTemplate;
        }
        List<TemplateData.Item> items = templateData.getItems();
        if(Predicates.isEmpty(items)){
            return null;
        }
        List<VETemplate.LogicSentence> list = VisitServices.from(items).map(
                new ResultVisitor<TemplateData.Item, VETemplate.LogicSentence>() {
            @Override
            public VETemplate.LogicSentence visit(TemplateData.Item item, Object param) {
                VETemplate.LogicSentence ls = new VETemplate.LogicSentence();
                Cases cases = new Cases(item.getCases(), item.getRelationship().equals("or"));
                int shotCategory = cases.getShotCategoryFlags();
                ls.setShotCategoryFlags(shotCategory);
                int count = (int) (item.getProportion() * 100);
                for (int i = count - 1; i >= 0; i--) {
                    CutInfo.PlaidInfo info = new CutInfo.PlaidInfo();
                    info.addColorFilter(new ShotCategoryFilter(new ShotCategoryFilter.ShotCategoryCondition(shotCategory)));
                    ls.addPlaidInfo(info);
                }
                return ls;
            }
        }).getAsList();
        VETemplate template = new VETemplate();
        template.setLogicSentences(list);
        template.setChapterFillType(templateData.getChapterFillType());
        mTemplate = template;
        return template;
    }
}
