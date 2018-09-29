package com.heaven7.ve.kingdom;

import com.google.gson.Gson;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.anno.CallOnce;
import com.heaven7.ve.anno.FileResource;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.filter.ShotCategoryFilter;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.cross_os.VEFactory;
import com.heaven7.ve.template.Cases;
import com.heaven7.ve.template.EffectData;
import com.heaven7.ve.template.TemplateData;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * @author heaven7
 */
public class FileResourceManager {

    @FileResource
    private String templateName; //the name of template config
    /** the name of effect file name */
    @FileResource
    private String effectName;
    private VETemplate mTemplate;

    private TemplateData templateData;
    private EffectData effectData;

    public void setTemplate(String template) {
        this.templateName = template;
    }
    public void setEffect(String effect) {
        this.effectName = effect;
    }

    public TemplateData getTemplateData() {
        return templateData;
    }
    public EffectData getEffectData() {
        return effectData;
    }

    /**
     * resolve some data with context
     * @param context the context.
     */
    @CallOnce
    public void resolve(ColorGapContext context) {
        //template
        String templateDir = context.getInitializeParam().getTemplateDir();
        String templatePath = templateDir + "/" + this.templateName + ".json";
        String templateJson = ResourceLoader.getDefault().loadFileAsString(context, templatePath);
        templateData = new Gson().fromJson(templateJson, TemplateData.class);
        //effect
        String effectDir = context.getInitializeParam().getEffectDir();
        String effectPath = effectDir + "/" + this.effectName + ".json";
        String effectJson = ResourceLoader.getDefault().loadFileAsString(context, effectPath);
        effectData = new Gson().fromJson(effectJson, EffectData.class);
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
                    IPlaidInfo info = VEFactory.getDefault().newPlaidInfo();
                    info.addColorFilter(new ShotCategoryFilter(new ShotCategoryFilter.ShotCategoryCondition(shotCategory)));
                    ls.addPlaidInfo(info);
                }
                return ls;
            }
        }).getAsList();
        VETemplate template = new VETemplate();
        template.setLogicSentences(list);
        template.setChapterFillType(templateData.getChapterFillType());
        template.setReleaseSentence(templateData.isReleaseSentence());
        mTemplate = template;
        return template;
    }

}
