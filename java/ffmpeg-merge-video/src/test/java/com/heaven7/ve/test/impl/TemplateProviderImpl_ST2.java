package com.heaven7.ve.test.impl;

import com.heaven7.ve.colorgap.RawScriptItem;
import com.heaven7.ve.colorgap.TemplateScriptProvider;

import java.util.ArrayList;
import java.util.List;

public class TemplateProviderImpl_ST2 implements TemplateScriptProvider {

  @Override
  public List<RawScriptItem> provideScript() {
    List<RawScriptItem> items = new ArrayList<>();

    RawScriptItem item = new RawScriptItem();
    item.setEvent("welcome");
   // item.setAirShotCount(0);
    item.setPercentage(15);
    items.add(item);

    item = new RawScriptItem();
    item.setEvent("church");
    item.setPercentage(35);
    items.add(item);

    item = new RawScriptItem();
    item.setEvent("dinner");
    // item.setAirShotCount(0);
    item.setPercentage(30);
    items.add(item);

    return items;
  }
}
