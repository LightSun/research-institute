package com.heaven7.ve.template;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * the sentence
 * proportion rule of skill (filter, effect, filter, transition)
 * Created by heaven7 on 2018/3/23 0023.
 */

public class ProportionConfig {

    public static final int TYPE_COMPLEX = -1;
    public static final int TYPE_NONE    = -2;

    //总体比例，每一份的格子数, 填充数组（1, 0）,
    // = target.size / from_temp.size , so < 1 means scale down
    private final float totalScale;
    public int colorFilterCount;
    public int effectCount;
    public int filterCount;
    public int transitionCount;
    private List<Rule> colorFilterRules;
    private List<Rule> effectRules;
    private List<Rule> filterRules;
    private List<Rule> transitionRules;
    /** the actually scale */
    private float actualScale = -1f;

    public ProportionConfig(float totalScale) {
        this.totalScale = totalScale;
    }
    /** scale up has limit max */
    public int getScaledColorFilterCount(){
        if(colorFilterCount == 0){
            return 0;
        }
        int target = Math.round(colorFilterCount * getScale());
        return Math.min(Math.max(target, 1), colorFilterCount);
    }
    /** scale up has limit max */
    public int getScaledEffectCount(){
        if(effectCount == 0){
            return 0;
        }
        int target = Math.round(effectCount * getScale());
        return Math.min(Math.max(target, 1), effectCount);
    }
    /** scale up has limit max */
    public int getScaledFilterCount(){
        if(filterCount == 0){
            return 0;
        }
        int target = Math.round(filterCount * getScale());
        return Math.min(Math.max(target, 1), filterCount);
    }
    /** scale up has limit max */
    public int getScaledTransitionCount() {
        if(transitionCount == 0){
            return 0;
        }
        int target = Math.round(transitionCount * getScale());
        return Math.min(Math.max(target, 1), transitionCount);
    }
    public final float getTotalScale() {
        return totalScale;
    }

    public List<Rule> getColorFilterRules() {
        return colorFilterRules;
    }
    public List<Rule> getEffectRules() {
        return effectRules;
    }
    public List<Rule> getFilterRules() {
        return filterRules;
    }
    public List<Rule> getTransitionRules() {
        return transitionRules;
    }
    public void addColorFilterRule(Rule rule){
        if(colorFilterRules == null){
            colorFilterRules = new ArrayList<>();
        }
        colorFilterRules.add(rule);
    }
    public void addFilterRule(Rule rule){
        if(filterRules == null){
            filterRules = new ArrayList<>();
        }
        filterRules.add(rule);
    }
    public void addEffectRule(Rule rule){
        if(effectRules == null){
            effectRules = new ArrayList<>();
        }
        effectRules.add(rule);
    }
    public void addTransitionRule(Rule rule){
        if(transitionRules == null){
            transitionRules = new ArrayList<>();
        }
        transitionRules.add(rule);
    }

    /** set the actual scale after adjust plaid. */
    public void setActualScale(float scale) {
        if(actualScale < 0) {
            this.actualScale = scale;
        }
    }
    /** get the scale . may be actually scale or total scale. */
    public final float getScale() {
        return actualScale > 0 ? actualScale : totalScale;
    }

    public class Rule{
        /** plaid index of logic sentence (from 0)*/
        public final int index;
        /** filled or not, eg: color filter, effect, filter, transition.*/
        public final boolean filled;
        /** the type of rule ,eg for filter, effect, transition. the type is that type. */
        public final int type;

        private final boolean isTail;

        public Rule(int index, int maxIndex, int type) {
            this.index = index;
            this.type = type;
            this.filled = type != TYPE_NONE;
            this.isTail = index == maxIndex;
        }
        public float getScale(){
            return ProportionConfig.this.getScale();
        }
        public boolean isHeadOrTail(){
            return index == 0 || isTail;
        }
        public boolean isHead(){
            return index == 0;
        }
        public boolean isTail(){
            return isTail;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Rule rule = (Rule) o;

            if (filled != rule.filled) return false;
            return type == rule.type;
        }
        @Override
        public int hashCode() {
            int result = (filled ? 1 : 0);
            result = 31 * result + type;
            return result;
        }
    }
}
