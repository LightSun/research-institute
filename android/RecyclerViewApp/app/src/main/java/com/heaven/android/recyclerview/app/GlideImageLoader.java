package com.heaven.android.recyclerview.app;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.ViewHelper;

/**
 * Created by heaven7 on 2019/7/22.
 */
public class GlideImageLoader implements ViewHelper.IImageLoader {
    private static final String TAG = "GlideImageLoader";

    @Override
    public void load(String s, final ImageView imageView) {
        int wh = DimenUtil.dip2px(imageView.getContext(), 80);
        Glide.with(imageView.getContext())
                .load(Uri.parse(s))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .signature(new StringSignature(s))
                .override(wh, wh)
                .listener(new RequestListener<Uri, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                        Logger.w(TAG, "onException",  model.toString());
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        Logger.d(TAG, "onResourceReady", "" + model.toString());
                        imageView.setImageBitmap(resource);
                        return false;
                    }
                }).preload();
    }
}
