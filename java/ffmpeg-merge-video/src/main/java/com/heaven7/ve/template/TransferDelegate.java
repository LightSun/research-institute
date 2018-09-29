package com.heaven7.ve.template;

import com.heaven7.java.base.util.Logger;
import com.heaven7.ve.cross_os.IPlaidInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * the transfer delegate of effect, filter ,transition, color filter.
 * Created by heaven7 on 2018/3/24 0024.
 */

public class TransferDelegate {

    private static final String TAG = "TransferDelegate";
    private final List<Callback> mCallbacks = new ArrayList<>();

    public TransferDelegate() {
        mCallbacks.add(new ColorFilterCallback());
        mCallbacks.add(new FilterCallback());
        mCallbacks.add(new EffectCallback());
        mCallbacks.add(new TransitionCallback());
    }

    public void transferAll(VETemplate.LogicSentence from, VETemplate.LogicSentence to) {
        final List<IPlaidInfo> targetPlaids = to.getPlaids();
        if (targetPlaids.size() == 0) {
            //no need
            return;
        }
        ProportionConfig pc = from.getProportionConfig();
        pc.setActualScale(targetPlaids.size() * 1f /from.getPlaidCount());
        for (Callback callback : mCallbacks) {
            transfer(from, to, callback);
        }
    }

    private void transfer(VETemplate.LogicSentence from, VETemplate.LogicSentence to, Callback callback) {
        final List<IPlaidInfo> targetPlaids = to.getPlaids();
        if (targetPlaids.size() == 0) {
            //no need
            return;
        }
        final List<IPlaidInfo> plaids = from.getPlaids();
        final ProportionConfig pc = from.getProportionConfig();

        boolean scaleDown = pc.getScale() < 1;
            /* the target filter count  */
        int count = callback.getScaledCount(pc);
        if (count > 0) {
            //do fill .prefer high weight, then head and tail
            List<ProportionConfig.Rule> tmp_rules = null;
            if (scaleDown) {
                //judge the count to deduplicate, this is because if duplicate all. the left may not enough.
                int delta = callback.getRawCount(pc) - count;
                if (delta > 0) {
                    tmp_rules = new ArrayList<>();
                    List<ProportionConfig.Rule> rules = callback.getRules(pc);
                    for (ProportionConfig.Rule rule : rules) {
                        if (!rule.filled) {
                            continue;
                        }
                        //if deduplicate reach the count. add it directly
                        if (delta == 0) {
                            tmp_rules.add(rule);
                        } else {
                            int index = tmp_rules.indexOf(rule);
                            if (index >= 0) {
                                ProportionConfig.Rule old = tmp_rules.get(index);
                                //head and tail is important
                                if (!old.isHeadOrTail()) {
                                    if (rule.index > old.index) {
                                        tmp_rules.set(index, rule);
                                    }
                                }
                                delta--;
                            } else {
                                tmp_rules.add(rule);
                            }
                        }
                    }
                } else if (delta < 0) {
                    throw new IllegalStateException();
                }
            }
            if (tmp_rules == null) {
                tmp_rules = new ArrayList<>(callback.getRules(pc));
                //filter not filled
                for (Iterator<ProportionConfig.Rule> it = tmp_rules.iterator(); it.hasNext(); ) {
                    if (!it.next().filled) {
                        it.remove();
                    }
                }
            }
            if (tmp_rules.isEmpty()) {
                //no filled rule . no need handle
                return;
            }

            //sort rules by weight.desc
            Collections.sort(tmp_rules, (o1, o2) -> {
                IPlaidInfo p1 = plaids.get(o1.index);
                IPlaidInfo p2 = plaids.get(o2.index);

                return Integer.compare(callback.getWeight(p2), callback.getWeight(p1));
            });

            //set the color filter with the position by scale(transfer)
            int maxIndex = targetPlaids.size() - 1;
           // Logger.d(TAG, "transfer", "start >>> " + callback.getClass().getSimpleName() + ",actually scale = " + pc.getScale());
            //for some case , the index may be small than 0.5. eg:  one sentence has 5 plaid, scale = 0.4 . so plaid count is 2.
            // the first effect index = 0.4 * 1 - 1.  so must in case this.
            List<Integer> holdIndexes = new ArrayList<>();
            for (ProportionConfig.Rule rule : tmp_rules) {
                int targetIndex;
                if (rule.isHead()) {
                    targetIndex = 0;
                } else {
                    targetIndex = Math.round((rule.index + 1) * rule.getScale()) - 1;
                    // Logger.d(TAG, "transfer", "first compute result >> targetIndex = " + targetIndex);
                    if (targetIndex < 0) {
                        //adjust index from -1 to 0
                        targetIndex = 0;
                    }
                }
                if (targetIndex > maxIndex) {
                    targetIndex--;
                }
                if (holdIndexes.contains(targetIndex)) {
                    if (targetIndex == maxIndex) {
                        targetIndex--;
                    } else {
                        targetIndex++;
                    }
                    if (holdIndexes.contains(targetIndex)) {
                        Logger.w(TAG, "transfer", "adjust index failed");
                        continue;
                    }
                }
                if (targetIndex > maxIndex) {
                    throw new IllegalStateException("invalid target index, targetIndex = " + targetIndex
                            + " ,maxIndex = " + maxIndex);
                }
                holdIndexes.add(targetIndex);
                callback.apply(plaids.get(rule.index), targetPlaids.get(targetIndex));
              /*  Logger.d(TAG, "transferColorFilter", "fll [color filter ] from index = "
                        + rule.index + " to index = " + targetIndex);*/
                count--;
                if (count == 0) {
                    //reach the limit
                    break;
                }
            }
        }
    }

    public interface Callback {

        /**
         * get the scaled count
         *
         * @param pc the proportion config
         * @return the scaled count
         */
        int getScaledCount(ProportionConfig pc);

        int getRawCount(ProportionConfig pc);

        List<ProportionConfig.Rule> getRules(ProportionConfig pc);

        int getWeight(IPlaidInfo info);

        /**
         * applyEffects the skill from one to another.
         *
         * @param from the from plaid info
         * @param to   the to plaid info
         */
        void apply(IPlaidInfo from, IPlaidInfo to);
    }

    public static class ColorFilterCallback implements Callback {
        @Override
        public int getScaledCount(ProportionConfig pc) {
            return pc.getScaledColorFilterCount();
        }

        @Override
        public int getRawCount(ProportionConfig pc) {
            return pc.colorFilterCount;
        }

        @Override
        public List<ProportionConfig.Rule> getRules(ProportionConfig pc) {
            return pc.getColorFilterRules();
        }

        @Override
        public int getWeight(IPlaidInfo info) {
            return info.getColorFilterWeight();
        }
        @Override
        public void apply(IPlaidInfo from, IPlaidInfo to) {
            to.setColorFilter(from.getColorFilter());
        }
    }

    public static class EffectCallback implements Callback {

        @Override
        public int getScaledCount(ProportionConfig pc) {
            return pc.getScaledEffectCount();
        }

        @Override
        public int getRawCount(ProportionConfig pc) {
            return pc.effectCount;
        }

        @Override
        public List<ProportionConfig.Rule> getRules(ProportionConfig pc) {
            return pc.getEffectRules();
        }

        @Override
        public int getWeight(IPlaidInfo info) {
            return info.getEffectWeight();
        }

        @Override
        public void apply(IPlaidInfo from, IPlaidInfo to) {
            to.setEffects(from.getEffects());
        }
    }

    public static class FilterCallback implements Callback {
        @Override
        public int getScaledCount(ProportionConfig pc) {
            return pc.getScaledFilterCount();
        }

        @Override
        public int getRawCount(ProportionConfig pc) {
            return pc.filterCount;
        }

        @Override
        public List<ProportionConfig.Rule> getRules(ProportionConfig pc) {
            return pc.getFilterRules();
        }

        @Override
        public int getWeight(IPlaidInfo info) {
            return info.getFilterWeight();
        }

        @Override
        public void apply(IPlaidInfo from, IPlaidInfo to) {
            to.setFilter(from.getFilter());
        }
    }

    public static class TransitionCallback implements Callback {
        @Override
        public int getScaledCount(ProportionConfig pc) {
            return pc.getScaledTransitionCount();
        }

        @Override
        public int getRawCount(ProportionConfig pc) {
            return pc.transitionCount;
        }

        @Override
        public List<ProportionConfig.Rule> getRules(ProportionConfig pc) {
            return pc.getTransitionRules();
        }

        @Override
        public int getWeight(IPlaidInfo info) {
            return info.getTransitionWeight();
        }

        @Override
        public void apply(IPlaidInfo from, IPlaidInfo to) {
            to.setTransitionInfo(from.getTransitionInfo());
        }
    }

}
