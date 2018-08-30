package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;

import java.util.List;

/**
 * help to sort shot
 *
 * @author heaven7
 */
public interface ShotSortDelegate {

    /**
     * the shot-sort rule is 'AESC' (near -> far)
     */
    int SHOT_SORT_RULE_AESC = 1;

    /**
     * the shot-sort rule is 'DESC' (far -> near)
     */
    int SHOT_SORT_RULE_DESC = 2;

    /**
     * the shot-sort rule is 'unknown'
     */
    int SHOT_SORT_RULE_UNKNOWN = 0;

    /**
     * sort shots. note there has two cases for sort:
     * <pre>
     *  1, sort according to chapter.
     *  2, sort according to story.
     * </pre>
     *
     * @param context  the context
     * @param sortRule the sort rule
     * @param items    the media items
     * @return the sorted part items
     */
    List<MediaPartItem> sortShots(Context context, int sortRule, List<MediaPartItem> items);


    static int getNextSortRule(int currentRule) {
        switch (currentRule) {
            case SHOT_SORT_RULE_UNKNOWN:
            case SHOT_SORT_RULE_DESC://far -> near
                return SHOT_SORT_RULE_AESC;
            case SHOT_SORT_RULE_AESC:
                return SHOT_SORT_RULE_DESC;

            default:
                throw new UnsupportedOperationException("wrong currentRule = " + currentRule);
        }
    }
    static String getRuleString(int currentRule) {
        switch (currentRule) {
            case SHOT_SORT_RULE_UNKNOWN:
                return "SHOT_SORT_RULE_UNKNOWN";

            case SHOT_SORT_RULE_DESC://near -> far
                return "SHOT_SORT_RULE_DESC";

            case SHOT_SORT_RULE_AESC:
                return "SHOT_SORT_RULE_AESC";

            default:
                throw new UnsupportedOperationException("wrong currentRule = " + currentRule);
        }
    }

}
