/* $Id: $
 *
 */
package com.oreilly.demo.android.ch09;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.oreilly.demo.android.ch09.drawable.HelloAndroidTextDrawable;
import com.oreilly.demo.android.ch09.efx.RotationTransitionAnimation;
import com.oreilly.demo.android.ch09.widget.EffectsWidget;
import com.oreilly.demo.android.ch09.widget.GLDemoWidget;
import com.oreilly.demo.android.ch09.widget.TransformedViewWidget;
import com.oreilly.demo.android.ch09.widget.TransformedViewWidget.Transformation;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:bmeike@callmeike.net">Blake Meike</a>
 */
public class TransformIt extends Activity {
    /** the currently visible view */
    View cur;
    /** the view up next */
    View next;

    private GLDemoWidget glWidget;


    /** @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // get the current view
        cur = findViewById(R.id.xform_v);
        buildXformView(
            (LinearLayout) findViewById(R.id.xform_v_left),
            (LinearLayout) findViewById(R.id.xform_v_right));

        // and the next one
        next = findViewById(R.id.efx_v);
        next.setVisibility(View.GONE);
        buildEfxView(
            (LinearLayout) findViewById(R.id.efx_v_left),
            (LinearLayout) findViewById(R.id.efx_v_right));

        // install the animation click listener
        final View root = findViewById(R.id.main);
        findViewById(R.id.main).setOnClickListener(
            new OnClickListener() {
                @Override public void onClick(View v) {
                    new RotationTransitionAnimation(1, root, cur, next)
                        .runAnimation();
                    // exchange views
                    View t = cur;
                    cur = next;
                    next = t;
            } });
    }

    private void buildXformView(LinearLayout lv, LinearLayout rv) {
        Drawable drawable = new HelloAndroidTextDrawable();
        lv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() { return "identity"; }
                @Override public void transform(Canvas canvas) { }
            } ));
        lv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() {
                    return "scale(1.2,1.2)";
                }
                @Override public void transform(Canvas canvas) {
                    canvas.scale(1.2F, 1.2F);
                } }));
        lv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() {
                    return "translate(140,-20),rotate(90)";
                }
                @Override public void transform(Canvas canvas) {
                    canvas.translate(140.0F, -20.0F);
                    canvas.rotate(90.0F);
                } }));

        drawable = getResources().getDrawable(R.drawable.to);
        drawable.setBounds(-20, -20, 205, 145);
        rv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() { return "identity"; }
                @Override public void transform(Canvas canvas) { }
            } ));
        rv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() {
                    return "scale(1.2,1.2)";
                }
                @Override public void transform(Canvas canvas) {
                    canvas.scale(1.2F, 1.2F);
                } }));
        rv.addView(new TransformedViewWidget(
            this,
            drawable,
            new Transformation() {
                @Override public String describe() {
                    return "translate(140,-20),rotate(90)";
                }
                @Override public void transform(Canvas canvas) {
                    canvas.translate(140.0F, -20.0F);
                    canvas.rotate(90.0F);
                } }));
    }

    private void buildEfxView(LinearLayout lv, LinearLayout rv) {
        lv.addView(new EffectsWidget(
            this,
            1,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) {
                    paint.setShadowLayer(1, 3, 4, Color.BLUE);
                } }));
        lv.addView(new EffectsWidget(
            this,
            3,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) {
                    paint.setShader(
                        new LinearGradient(
                            0.0F,
                            0.0F,
                            160.0F,
                            80.0F,
                            new int[] {
                                Color.BLACK, Color.RED, Color.YELLOW },
                            new float[] { 0.2F, 0.3F, 0.2F },
                            Shader.TileMode.REPEAT));
                } }));
        lv.addView(new EffectsWidget(
            this,
            5,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) {
                    paint.setMaskFilter(
                        new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));
                } }));

        // the OpenGL widget
        glWidget = new GLDemoWidget(this);
        lv.addView(glWidget);

        rv.addView(new EffectsWidget(
            this,
            2,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) {
                    paint.setShadowLayer(3, -8, 7, Color.GREEN);
                } }));
        rv.addView(new EffectsWidget(
            this,
            4,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) {
                    paint.setShader(
                        new LinearGradient(
                            0.0F,
                            40.0F,
                            15.0F,
                            40.0F,
                            Color.BLUE,
                            Color.GREEN,
                            Shader.TileMode.MIRROR));
                } }));
        View w = new EffectsWidget(
            this,
            6,
            new EffectsWidget.PaintEffect() {
                @Override public void setEffect(Paint paint) { }
            });
        rv.addView(w);
        w.setBackgroundResource(R.drawable.throbber);

        //!!! Hack; should be fixed in Cupcake
        w.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                AnimationDrawable animation
                    = (AnimationDrawable) v.getBackground();
                if (animation.isRunning()) { animation.stop(); }
                else { animation.start(); }
            } });
    }

    /**
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (null != glWidget) { glWidget.onPause(); }
    }

    /**
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (null != glWidget) { glWidget.onResume(); }
    }
}
