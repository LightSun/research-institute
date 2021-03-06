package com.heaven7.ve.template;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.heaven7.ve.colorgap.filter.GroupFilter;
import com.heaven7.ve.cross_os.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2018/3/26 0026.
 */

public class TemplateFormatter {

    //测试环境
    public static final boolean TEST = true;
    public static final int COLOR_FILTER_GPS = 1;

    public String toJson(VETemplate template) {
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(IPlaidInfo.class, new PlaidAdapter())
                .create();
        return gson.toJson(template);
    }

    private static class PlaidAdapter extends TypeAdapter<IPlaidInfo> {
        @Override
        public void write(JsonWriter out, IPlaidInfo value) throws IOException {
            out.beginObject();
            //here just mock color filter by type
            out.name("color_filter_type").value(COLOR_FILTER_GPS);
            if (value.hasEffect()) {
                out.name("effects");
                writeEffects(out, value.getEffects());
            }
            if (value.getFilter() != null) {
                out.name("filter");
                writeEffect(out, "filter_type", value.getFilter());
            }
            if (value.getTransitionInfo() != null) {
                out.name("transition");
                writeEffect(out, "transition_type", value.getTransitionInfo());
            }
            out.name("colorFilterWeight").value(value.getColorFilterWeight())
                    .name("filterWeight").value(value.getFilterWeight())
                    .name("effectWeight").value(value.getEffectWeight())
                    .name("transitionWeight").value(value.getTransitionWeight());
            out.endObject();
        }

        @Override
        public IPlaidInfo read(JsonReader in) throws IOException {
            in.beginObject();
            IPlaidInfo info = VEFactory.getDefault().newPlaidInfo();
            while (in.hasNext()) {
                String key = in.nextName();
                switch (key) {
                    case "color_filter_type":
                        info.setColorFilter(new GroupFilter());
                        break;

                    case "effects":
                        readEffects(in, info);
                        break;

                    case "filter":
                        IFilterInfo fi = VEFactory.getDefault().newFilterInfo();
                        readEffect(in, fi, "filter_type");
                        info.setFilter(fi);
                        break;

                    case "transition":
                        ITransitionInfo ti = VEFactory.getDefault().newTransitionInfo();
                        readEffect(in, ti, "transition_type");
                        info.setTransitionInfo(ti);
                        break;

                    case "colorFilterWeight":
                        info.setColorFilterWeight(in.nextInt());
                        break;
                    case "filterWeight":
                        info.setFilterWeight(in.nextInt());
                        break;
                    case "effectWeight":
                        info.setEffectWeight(in.nextInt());
                        break;
                    case "transitionWeight":
                        info.setTransitionWeight(in.nextInt());
                        break;

                    default:
                        in.skipValue();
                }
            }
            in.endObject();
            return info;
        }

        private void readEffects(JsonReader in, IPlaidInfo info) throws IOException {
            List<IEffectInfo> list = new ArrayList<>();
            in.beginArray();
            while (in.hasNext()) {
                IEffectInfo effect = VEFactory.getDefault().newEffectInfo();
                readEffect(in, effect, "effect_type");
                list.add(effect);
            }
            in.endArray();
            info.setEffects(list);
        }

        private void readEffect(JsonReader in, IEffectInfo effect, String type) throws IOException {
            in.beginObject();
            while (in.hasNext()) {
                String key = in.nextName();
                if (key.equals(type)) {
                    effect.setType(in.nextInt());
                } else if (key.equals("start_time")) {
                    effect.setStartTime(in.nextLong());
                } else if (key.equals("end_time")) {
                    effect.setEndTime(in.nextLong());
                } else if (key.equals("max_duration")) {
                    effect.setMaxDuration(in.nextLong());
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
        }

        private void writeEffects(JsonWriter out, List<IEffectInfo> value) throws IOException {
            out.beginArray();
            for (IEffectInfo info : value) {
                writeEffect(out, "effect_type", info);
            }
            out.endArray();
        }

        private void writeEffect(JsonWriter out, String type_key, IEffectInfo info) throws IOException {
            out.beginObject();
            out.name(type_key).value(info.getType());
            out.name("start_time").value(info.getStartTime());
            out.name("end_time").value(info.getEndTime());
            out.name("max_duration").value(info.getMaxDuration());
            out.endObject();
        }
    }

}
