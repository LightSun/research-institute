package com.heaven7.ve.colorgap.impl;


import com.heaven7.ve.colorgap.RawScriptItem;
import com.heaven7.ve.colorgap.TemplateScriptProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2018/5/16 0016.
 */

public class TemplateProviderImpl implements TemplateScriptProvider {

    @Override
    public List<RawScriptItem> provideScript() {
        List<RawScriptItem> items = new ArrayList<>();

        RawScriptItem item = new RawScriptItem();
        item.setEvent("churchIn");
        item.setAirShotCount(2);
        item.setPercentage(30);
        items.add(item);

        item = new RawScriptItem();
        item.setEvent("churchOut");
        item.setAirShotCount(2);
        item.setPercentage(30);
        items.add(item);

        item = new RawScriptItem();
        item.setEvent("character");
        item.setPercentage(20);
        items.add(item);

        item = new RawScriptItem();
        item.setEvent("dinner");
        item.setPercentage(20);
        items.add(item);

        return items;
    }
}
