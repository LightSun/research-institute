package com.heaven7.ve.colorgap;


import com.heaven7.java.base.anno.Nullable;
import com.heaven7.ve.Context;
import com.heaven7.ve.template.VETemplate;

import java.util.List;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public interface MusicShader {


    /** the tint flag of 'allow release head and tail.' */
    int FLAG_ALLOW_RELEASE_HEAD_TAIL = 0x0001;

    /**
     * tint by {@linkplain GapColorFilter}.
     * @param context the resource context
     * @param template the template of script
     * @param musicInfos the cut music infos
     * @param tintFlags the tint flags. like {@linkplain #FLAG_ALLOW_RELEASE_HEAD_TAIL}
     * @return  tint  the plaids to template. never null.
     */
    VETemplate tint(Context context, @Nullable VETemplate template, List<CutInfo.PlaidInfo> musicInfos, int tintFlags);

}
