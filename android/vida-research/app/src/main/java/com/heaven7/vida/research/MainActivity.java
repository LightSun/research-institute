package com.heaven7.vida.research;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.heaven7.core.util.Logger;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag0);

        findViewById(R.id.box1).setOnTouchListener(this);
        findViewById(R.id.box2).setOnTouchListener(this);
        findViewById(R.id.box3).setOnTouchListener(this);

        findViewById(R.id.leftContainer).setOnDragListener(this);
        findViewById(R.id.rightContainer).setOnDragListener(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view){
                @Override
                public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
                    super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);
                }
                @Override
                public void onDrawShadow(Canvas canvas) {
                    super.onDrawShadow(canvas);
                }
            };

            ClipData data = ClipData.newPlainText("id", view.getResources().getResourceEntryName(view.getId()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            } else {
                view.startDrag(data, shadowBuilder, view, 0);
            }

            view.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        //here v is the Parent view.
        /**
         * ACTION_DRAG_STARTED ->ACTION_DRAG_ENTERED -> ACTION_DRAG_EXITED -> ACTION_DRAG_ENDED
         * ACTION_DROP 手释放时调用
         */
        switch (event.getAction()) {
            // signal for the start of a drag and drop operation
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                Logger.i("MainActivity", "onDrag", "ACTION_DRAG_STARTED");
                break;

            // the drag point has entered the bounding box of the View
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(0xFFFFF6F9);
                Logger.i("MainActivity", "onDrag", "ACTION_DRAG_ENTERED");
                break;

            // the user has moved the drag shadow outside the bounding box of the View
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(v.getId() == R.id.leftContainer ? 0xFFE8E6E7 : 0xFFB1BEC4);
                Logger.i("MainActivity", "onDrag", "ACTION_DRAG_EXITED");
                break;

            // the drag and drop operation has concluded
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(v.getId() == R.id.leftContainer ? 0xFFE8E6E7 : 0xFFB1BEC4);
                Logger.i("MainActivity", "onDrag", "ACTION_DRAG_ENDED");
                break;

            //drag shadow has been released,the drag point is within the bounding box of the View
            case DragEvent.ACTION_DROP:
                Logger.i("MainActivity", "onDrag", "ACTION_DROP");
                // child view
                View view = (View) event.getLocalState();
                // we want to make sure it is dropped only to left and right parent view
                if (v.getId() == R.id.leftContainer || v.getId() == R.id.rightContainer) {

                    ViewGroup source = (ViewGroup) view.getParent();
                    source.removeView(view);

                    LinearLayout target = (LinearLayout) v;
                    target.addView(view);

                    String id = event.getClipData().getItemAt(0).getText().toString();
                    Toast.makeText(this, id + " dropped!", Toast.LENGTH_SHORT).show();
                }
                // make view visible as we set visibility to invisible while starting drag
                view.setVisibility(View.VISIBLE);
                break;
        }

        return true;
    }
}
