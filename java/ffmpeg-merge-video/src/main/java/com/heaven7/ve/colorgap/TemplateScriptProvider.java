package com.heaven7.ve.colorgap;

import java.util.List;

/**
 * the template script provider.
 * Created by heaven7 on 2018/4/18 0018.
 */
@Deprecated
public interface TemplateScriptProvider {

    /**
     * provide the script.
     * @return the script items
     */
    List<RawScriptItem> provideScript();

}
