package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawingUtils;
import com.heaven7.vida.research.utils.TypefaceCacher;

import static java.lang.Math.max;

/**
 * the dynamic content view. often used to draw time, day and month.
 * Created by heaven7 on 2018/11/7 0007.
 */
public class DynamicContentView extends View {

    public static final int TYPE_CITY = 1;
    public static final int TYPE_MONTH = 2;           // include day
    public static final int TYPE_TIME = 3;
    public static final int TYPE_MONTH_WITH_TIME = 4;// day, month and time

    private static final byte SUB_TYPE_TIME  = 1;
    private static final byte SUB_TYPE_MONTH = 2;
    private static final byte SUB_TYPE_DAY   = 3;
    private static final String TAG = "DynamicContentView";

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextMeasureInfo mMeasureInfo = new TextMeasureInfo();

    private Content mContent;
    private TextSizeProvider mTextSizeProvider;
    private FontProvider mFontProvider;
    /** the base space between texts. */
    private int mSpace;
    /** the scale */
    private float mScale = 1f;


    public DynamicContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL); //stroke can effect text.

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicContentView);
        try {
            mSpace = a.getDimensionPixelSize(R.styleable.DynamicContentView_dcv_space, 0);
        }finally {
            a.recycle();
        }
    }

    public void setTextSizeProvider(TextSizeProvider p){
        this.mTextSizeProvider = p;
    }
    public void setFontProvider(FontProvider p){
        this.mFontProvider = p;
    }
    public void setContent(Content content){
        this.mContent = content;
        requestLayout();
        invalidate();
    }

    public void setLineSpace(int space){
        this.mSpace = space;
        requestLayout();
        invalidate();
    }

    public void scale(float scale){
        if(scale <= 0 ){
            throw new IllegalArgumentException();
        }
        this.mScale = scale;
        requestLayout();
        invalidate();
    }

    private int getLineSpaceActually(){
        return (int) (mSpace * mScale);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mContent == null){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else{
            Throwables.checkNull(mTextSizeProvider);
            Throwables.checkNull(mFontProvider);
            final int lineSpace = getLineSpaceActually();
            int w , h;
            switch (mContent.getType()){
                case TYPE_MONTH: {
                    //measure day
                    setPaint(SUB_TYPE_DAY);
                    Rect rect = DrawingUtils.measure(mPaint, mContent.getDay());
                    int dayW = rect.width();
                    int dayH = rect.height();
                    //measure month
                    setPaint(SUB_TYPE_MONTH);
                    rect = DrawingUtils.measure(mPaint, mContent.getMonth());
                    int monthW = rect.width();
                    int monthH = rect.height();

                    w = max(dayW, monthW);
                    h = dayH + monthH + lineSpace;
                    mMeasureInfo.dayW = dayW;
                    mMeasureInfo.dayH = dayH;
                    mMeasureInfo.monthW = monthW;
                    mMeasureInfo.monthH = monthH;
                    break;
                }

                case TYPE_TIME: {
                    setPaint(SUB_TYPE_TIME);
                    Rect rect = DrawingUtils.measure(mPaint, mContent.getTime());
                    w = rect.width();
                    h = rect.height();
                    mMeasureInfo.timeW = w;
                    mMeasureInfo.timeH = h;
                    break;
                }

                case TYPE_MONTH_WITH_TIME:
                    // time, day, month
                    setPaint(SUB_TYPE_TIME);
                    Rect rect = DrawingUtils.measure(mPaint, mContent.getTime());
                    int timeW = rect.width();
                    int timeH = rect.height();

                    setPaint(SUB_TYPE_DAY);
                    rect = DrawingUtils.measure(mPaint, mContent.getDay());
                    int dayW = rect.width();
                    int dayH = rect.height();

                    setPaint(SUB_TYPE_MONTH);
                    rect = DrawingUtils.measure(mPaint, mContent.getMonth());
                    int monthW = rect.width();
                    int monthH = rect.height();
                    w = max(max(timeW, dayW), monthW);
                    h = timeH + dayH + monthH + lineSpace * 2;

                    mMeasureInfo.dayW = dayW;
                    mMeasureInfo.dayH = dayH;
                    mMeasureInfo.monthW = monthW;
                    mMeasureInfo.monthH = monthH;
                    mMeasureInfo.timeW = timeW;
                    mMeasureInfo.timeH = timeH;
                    break;

                default:
                    throw new UnsupportedOperationException("");
            }
            w += getPaddingLeft() + getPaddingRight();
            h += getPaddingTop() + getPaddingBottom();
          /*  int widthSpec = View.MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);*/
            setMeasuredDimension(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mContent == null){
            return;
        }
        int padLeft = getPaddingLeft();
        int padTop = getPaddingTop();
        int padRight = getPaddingRight();
        int padBottom = getPaddingBottom();

        int w = getWidth() - padLeft - padRight;
        int h = getHeight() - padTop - padBottom;

        canvas.save();
        canvas.translate(padLeft, padTop);
        mMeasureInfo.draw(this, canvas, w, h);
        canvas.restore();
    }

    private void setPaint(int subType){
        Context context = getContext();
        Paint p = this.mPaint;
        int type = mContent.getType();
        TextSizeProvider textP = this.mTextSizeProvider;
        FontProvider fontP = this.mFontProvider;

        switch (subType){
            case SUB_TYPE_DAY:
                int daySize = textP.getDayTextSize(this, type);
                String dayPath = fontP.getDayFontPath(this, type);
                p.setTextSize(daySize * mScale);
                p.setTypeface(getTypeface0(context, dayPath));
                break;

            case SUB_TYPE_MONTH:
                int monthSize = textP.getMonthTextSize(this, type);
                String monthPath = fontP.getMonthFontPath(this, type);
                p.setTextSize(monthSize * mScale);
                p.setTypeface(getTypeface0(context, monthPath));
                break;

            case SUB_TYPE_TIME:
                int timeSize = textP.getTimeTextSize(this, type);
                String timePath = fontP.getTimeFontPath(this, type);
                p.setTextSize(timeSize * mScale);
                p.setTypeface(getTypeface0(context, timePath));
                break;

            default:
                throw new UnsupportedOperationException("");
        }
    }

    private static Typeface getTypeface0(Context context,String path){
        if(TextUtils.isEmpty(path)){
            return Typeface.DEFAULT;
        }
        return TypefaceCacher.getTypeface(context, path);
    }

    private static class TextMeasureInfo{
        int timeW,timeH;
        int dayW,dayH;
        int monthW,monthH;

        final Rect rect = new Rect();
        final Rect range = new Rect();
        final RectF rectF = new RectF();

        //w is the content width , h  is the content height.
        public void draw(DynamicContentView view, Canvas canvas, int w, int h) {
            final int lineSpace = view.getLineSpaceActually();
            switch (view.mContent.getType()) {
                 case TYPE_MONTH: {
                     //start draw day
                     drawDay(view, canvas, w, h);
                     canvas.translate(0, dayH + lineSpace);
                     //start draw month
                     drawMonth(view, canvas, w, h);
                    // canvas.translate(0, monthH+ view.mSpace);
                     break;
                 }

                 case TYPE_TIME: {
                     drawTime(view, canvas, w, h);
                     break;
                 }

                 case TYPE_MONTH_WITH_TIME: {
                     //time, day , month
                     //time
                     drawTime(view, canvas, w, h);
                     canvas.translate(0, timeH + lineSpace);
                     //day
                     drawDay(view, canvas, w, h);
                     canvas.translate(0, dayH + lineSpace);
                     //month
                     drawMonth(view, canvas, w, h);
                     break;
                 }

                 default:
                     throw new UnsupportedOperationException("");
             }
        }

        private void drawTime(DynamicContentView view,Canvas canvas, int w, int h){
            final Content mContent = view.mContent;
            final Paint p = view.mPaint;

            view.setPaint(SUB_TYPE_TIME);
            range.set(0, 0, w, timeH);
            rect.set(0, 0, timeW, timeH);
            DrawingUtils.center(rect, range);
            DrawingUtils.computeTextDrawingCoordinate(mContent.getTime(), p, rect, rectF);
            canvas.drawText(mContent.getTime(), rectF.left, rectF.top - p.ascent(), p);
        }

        private void drawDay(DynamicContentView view,Canvas canvas,int w, int h){
            final Content mContent = view.mContent;
            final Paint p = view.mPaint;

            view.setPaint(SUB_TYPE_DAY);
            range.set(0, 0, w, dayH);
            rect.set(0, 0, dayW, dayH);
            DrawingUtils.center(rect, range);
            DrawingUtils.computeTextDrawingCoordinate(mContent.getDay(), p, rect, rectF);
            Logger.d(TAG, "drawDay", "");
            canvas.drawText(mContent.getDay(), rectF.left, rectF.top - p.ascent(), p);
        }
        private void drawMonth(DynamicContentView view,Canvas canvas,int w, int h){
            final Content mContent = view.mContent;
            final Paint p = view.mPaint;

            view.setPaint(SUB_TYPE_MONTH);
            range.set(0, 0, w, monthH);
            rect.set(0, 0, monthW, monthH);
            DrawingUtils.center(rect, range);
            DrawingUtils.computeTextDrawingCoordinate(mContent.getMonth(), p, rect, rectF);
            canvas.drawText(mContent.getMonth(), rectF.left, rectF.top - p.ascent(), p);
        }
    }

    /**
     * the text size provider;
     * @
     */
    public interface TextSizeProvider{
        int getMonthTextSize(DynamicContentView view, int contentType);
        int getDayTextSize(DynamicContentView view,int contentType);
        int getTimeTextSize(DynamicContentView view, int contentType);
    }

    /**
     * the font provider;
     * if font path return null. means use default typeface.
     */
    public interface FontProvider{
        String getMonthFontPath(DynamicContentView view, int contentType);
        String getDayFontPath(DynamicContentView view, int contentType);
        String getTimeFontPath(DynamicContentView view, int contentType);
    }

    public static class Content{
        int type;
        int iconId;
        String city;
        String month;
        String day;
        String time;

        Content(Content.Builder builder) {
            this.type = builder.type;
            this.iconId = builder.iconId;
            this.city = builder.city;
            this.month = builder.month;
            this.day = builder.day;
            this.time = builder.time;
        }

        public int getType() {
            return this.type;
        }

        public int getIconId() {
            return this.iconId;
        }

        public String getCity() {
            return this.city;
        }

        public String getMonth() {
            return this.month;
        }

        public String getDay() {
            return this.day;
        }

        public String getTime() {
            return this.time;
        }

        public static class Builder {
            int type;
            int iconId;
            String city;
            String month;
            String day;
            String time;

            public Builder setType(int type) {
                this.type = type;
                return this;
            }

            public Builder setIconId(int iconId) {
                this.iconId = iconId;
                return this;
            }

            public Builder setCity(String city) {
                this.city = city;
                return this;
            }

            public Builder setMonth(String month) {
                this.month = month;
                return this;
            }

            public Builder setDay(String day) {
                this.day = day;
                return this;
            }

            public Builder setTime(String time) {
                this.time = time;
                return this;
            }

            public Content build() {
                return new Content(this);
            }
        }
    }
}
