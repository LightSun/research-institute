package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.vida.research.R;

/**
 * Created by heaven7 on 2018/2/7 0007.
 */

public class RoundView extends View{

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Drawable mDrawable;
    private Bitmap mBitmap;

    private final Path path = new Path();
    private final RectF rectF = new RectF();
    private final Rect rect = new Rect();

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDrawable = context.getResources().getDrawable(R.drawable.ic_test);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = (right - left ) / 2 - 20;
        mBitmap = Bitmap.createScaledBitmap(drawableToBitmap(mDrawable), width, bottom - top, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2 - 20;
        int height = getHeight();

        rectF.set(0, 0, width, height);
        rect.set(0, 0, width, height);
        path.reset();
        path.addRoundRect(rectF, 20, 20, Path.Direction.CW);

        //draw first round image
        //canvas.translate(50, 0);
        canvas.save();
        canvas.clipPath(path);
        canvas.drawBitmap(mBitmap, null, rect, null);
        canvas.restore();

        path.reset();
        rect.set(width + 20, 0, getRight(), getBottom());
        rectF.set(rect);
        path.addRoundRect(rectF, 20, 20, Path.Direction.CW);

        //draw second round image
        canvas.save();
        canvas.clipPath(path);
        canvas.drawBitmap(mBitmap, null, rect, null);
        canvas.restore();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
