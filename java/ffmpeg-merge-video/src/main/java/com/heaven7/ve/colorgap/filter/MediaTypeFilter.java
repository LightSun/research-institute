package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;

import static com.heaven7.ve.colorgap.MetaInfo.FLAG_MEDIA_TYPE;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class MediaTypeFilter extends GapColorFilter {

    public MediaTypeFilter(MediaTypeCondition condition) {
        super(condition);
    }

    @Override
    public int getFlag() {
        return FLAG_MEDIA_TYPE;
    }

    /**
     * the media type see {@linkplain com.heaven7.ve.PathTimeTraveller#TYPE_VIDEO} and etc.
     * Created by heaven7 on 2018/3/16 0016.
     */
    public static class MediaTypeCondition extends GapColorFilter.GapColorCondition {

        public MediaTypeCondition(int type) {
            super(type);
        }
        @Override
        protected int getCategory(int type) {
            return type;
        }
    }

}
