package tech.ducletran.travelgallery.CustomizedClass;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import tech.ducletran.travelgallery.Activities.DisplayImageActivity;

/**
 * CustomizedImageView is a pinch-to-zoom extension of {@link ImageView}, providing a smooth
 * user experience and a very natural feel when zooming and translating. It also supports
 * automatic resetting, and allows for exterior bounds restriction to keep the image within
 * visible window.
 */
public class CustomizedImageView extends AppCompatImageView implements ScaleGestureDetector.OnScaleGestureListener {

    private static final float MIN_SCALE = 0.6f;
    private static final float MAX_SCALE = 8f;
    private final int RESET_DURATION = 200;

    private ScaleType startScaleType;

    // These matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix startMatrix = new Matrix();

    private float[] matrixValues = new float[9];
    private float[] startValues = null;

    private float minScale = MIN_SCALE;
    private float maxScale = MAX_SCALE;

    //the adjusted scale bounds that account for an image's starting scale values
    private float calculatedMinScale = MIN_SCALE;
    private float calculatedMaxScale = MAX_SCALE;

    private final RectF bounds = new RectF();

    private boolean translatable;
    private boolean zoomable;
    private boolean doubleTapToZoom;
    private boolean restrictBounds;
    private boolean animateOnReset;
    private boolean autoCenter;
    private float doubleTapToZoomScaleFactor;
    @AutoResetMode
    private int autoResetMode;

    private PointF last = new PointF(0, 0);
    private float startScale = 1f;
    private float scaleBy = 1f;
    private float currentScaleFactor = 1f;
    private int previousPointerCount = 1;

    private ScaleGestureDetector scaleDetector;

    private GestureDetector gestureDetector;
    private boolean doubleTapDetected = false;
    private boolean singleTapDetected = false;

    private DisplayImageActivity activity;

    public void setActivity(DisplayImageActivity activity) {
        this.activity = activity;
    }



    public CustomizedImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomizedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomizedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        scaleDetector = new ScaleGestureDetector(context, this);
        gestureDetector = new GestureDetector(context, gestureListener);
        ScaleGestureDetectorCompat.setQuickScaleEnabled(scaleDetector, false);
        startScaleType = getScaleType();

        TypedArray values = context.obtainStyledAttributes(attrs, styleable.CustomizedImageView);


        zoomable = values.getBoolean(styleable.zoomable, true);
        translatable = values.getBoolean(styleable.translatable, true);
        animateOnReset = values.getBoolean(styleable.animateOnReset, true);
        autoCenter = values.getBoolean(styleable.autoCenter, true);
        restrictBounds = values.getBoolean(styleable.restrictBounds, false);
        doubleTapToZoom = values.getBoolean(styleable.doubleTapToZoom, true);
//        minScale = values.getFloat(styleable.minScale, MIN_SCALE);
        minScale = MIN_SCALE;
        maxScale = values.getFloat(styleable.maxScale, MAX_SCALE);
        doubleTapToZoomScaleFactor = values.getFloat(styleable.doubleTapToZoomScaleFactor, 3);
        autoResetMode = AutoResetMode.Parser.fromInt(values.getInt(styleable.autoResetMode, AutoResetMode.UNDER));

        verifyScaleRange();

        values.recycle();
    }

    private void verifyScaleRange() {
        if (minScale >= maxScale) {
            throw new IllegalStateException("minScale must be less than maxScale");
        }

        if (minScale < 0) {
            throw new IllegalStateException("minScale must be greater than 0");
        }

        if (maxScale < 0) {
            throw new IllegalStateException("maxScale must be greater than 0");
        }

        if (doubleTapToZoomScaleFactor > maxScale) {
            doubleTapToZoomScaleFactor = maxScale;
        }

        if (doubleTapToZoomScaleFactor < minScale) {
            doubleTapToZoomScaleFactor = minScale;
        }
    }

    @Override
    public void setScaleType(@Nullable ScaleType scaleType) {
        if (scaleType != null) {
            super.setScaleType(scaleType);
            startScaleType = scaleType;
            startValues = null;
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);

        if (!enabled) {
            setScaleType(startScaleType);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setScaleType(startScaleType);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setScaleType(startScaleType);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setScaleType(startScaleType);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        setScaleType(startScaleType);
    }

    private void updateBounds(final float[] values) {
        if (getDrawable() != null) {
            bounds.set(values[Matrix.MTRANS_X],
                    values[Matrix.MTRANS_Y],
                    getDrawable().getIntrinsicWidth() * values[Matrix.MSCALE_X] + values[Matrix.MTRANS_X],
                    getDrawable().getIntrinsicHeight() * values[Matrix.MSCALE_Y] + values[Matrix.MTRANS_Y]);
        }
    }

    private float getCurrentDisplayedWidth() {
        if (getDrawable() != null)
            return getDrawable().getIntrinsicWidth() * matrixValues[Matrix.MSCALE_X];
        else
            return 0;
    }

    private float getCurrentDisplayedHeight() {
        if (getDrawable() != null)
            return getDrawable().getIntrinsicHeight() * matrixValues[Matrix.MSCALE_Y];
        else
            return 0;
    }

    private void setStartValues() {
        startValues = new float[9];
        startMatrix = new Matrix(getImageMatrix());
        startMatrix.getValues(startValues);
        calculatedMinScale = minScale * startValues[Matrix.MSCALE_X];
        calculatedMaxScale = maxScale * startValues[Matrix.MSCALE_X];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isClickable() && isEnabled() && (zoomable || translatable)) {
            if (getScaleType() != ScaleType.MATRIX) {
                super.setScaleType(ScaleType.MATRIX);
            }

            if (startValues == null) {
                setStartValues();
            }

            //get the current state of the image matrix, its values, and the bounds of the drawn bitmap
            matrix.set(getImageMatrix());
            matrix.getValues(matrixValues);
            updateBounds(matrixValues);

            scaleDetector.onTouchEvent(event);
            gestureDetector.onTouchEvent(event);

            if (doubleTapToZoom && doubleTapDetected) {
                doubleTapDetected = false;
                singleTapDetected = false;
                if (matrixValues[Matrix.MSCALE_X] != startValues[Matrix.MSCALE_X]) {
                    reset();
                } else {
                    Matrix zoomMatrix = new Matrix(matrix);
                    zoomMatrix.postScale(doubleTapToZoomScaleFactor, doubleTapToZoomScaleFactor, scaleDetector.getFocusX(), scaleDetector.getFocusY());
                    animateScaleAndTranslationToMatrix(zoomMatrix, RESET_DURATION);
                }
                return true;
            } else if (!singleTapDetected){
                /* if the event is a down touch, or if the number of touch points changed,
                 * we should reset our start point, as event origins have likely shifted to a
                 * different part of the screen*/
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                        event.getPointerCount() != previousPointerCount) {
                    last.set(scaleDetector.getFocusX(), scaleDetector.getFocusY());
                } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {

                    final float focusx = scaleDetector.getFocusX();
                    final float focusy = scaleDetector.getFocusY();

                    if (translatable) {
                        //calculate the distance for translation
                        float xdistance = getXDistance(focusx, last.x);
                        float ydistance = getYDistance(focusy, last.y);
                        matrix.postTranslate(xdistance, ydistance);
                    }

                    if (zoomable) {
                        matrix.postScale(scaleBy, scaleBy, focusx, focusy);
                        currentScaleFactor = matrixValues[Matrix.MSCALE_X] / startValues[Matrix.MSCALE_X];
                    }

                    setImageMatrix(matrix);

                    last.set(focusx, focusy);
                }

                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    scaleBy = 1f;
                    resetImage();
                }
            } else {
                if (activity!= null) {
                    activity.toggleActionBar();
                }
            }

            //this tracks whether they have changed the number of fingers down
            previousPointerCount = event.getPointerCount();

            return true;
        }

        return super.onTouchEvent(event);
    }

    private void resetImage() {
        switch (autoResetMode) {
            case AutoResetMode.UNDER:
                if (matrixValues[Matrix.MSCALE_X] <= startValues[Matrix.MSCALE_X]) {
                    reset();
                } else {
                    center();
                }
                break;
            case AutoResetMode.OVER:
                if (matrixValues[Matrix.MSCALE_X] >= startValues[Matrix.MSCALE_X]) {
                    reset();
                } else {
                    center();
                }
                break;
            case AutoResetMode.ALWAYS:
                reset();
                break;
            case AutoResetMode.NEVER:
                center();
        }
    }

    private void center() {
        if (autoCenter) {
            animateTranslationX();
            animateTranslationY();
        }
    }

    public void reset() {
        reset(animateOnReset);
    }

    public void reset(final boolean animate) {
        if (animate) {
            animateToStartMatrix();
        }
        else {
            setImageMatrix(startMatrix);
        }
    }

    private void animateToStartMatrix() {
        animateScaleAndTranslationToMatrix(startMatrix, RESET_DURATION);
    }

    private void animateScaleAndTranslationToMatrix(final Matrix targetMatrix, final int duration) {

        final float[] targetValues = new float[9];
        targetMatrix.getValues(targetValues);

        final Matrix beginMatrix = new Matrix(getImageMatrix());
        beginMatrix.getValues(matrixValues);

        //difference in current and original values
        final float xsdiff = targetValues[Matrix.MSCALE_X] - matrixValues[Matrix.MSCALE_X];
        final float ysdiff = targetValues[Matrix.MSCALE_Y] - matrixValues[Matrix.MSCALE_Y];
        final float xtdiff = targetValues[Matrix.MTRANS_X] - matrixValues[Matrix.MTRANS_X];
        final float ytdiff = targetValues[Matrix.MTRANS_Y] - matrixValues[Matrix.MTRANS_Y];

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            final Matrix activeMatrix = new Matrix(getImageMatrix());
            final float[] values = new float[9];

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (Float) animation.getAnimatedValue();
                activeMatrix.set(beginMatrix);
                activeMatrix.getValues(values);
                values[Matrix.MTRANS_X] = values[Matrix.MTRANS_X] + xtdiff * val;
                values[Matrix.MTRANS_Y] = values[Matrix.MTRANS_Y] + ytdiff * val;
                values[Matrix.MSCALE_X] = values[Matrix.MSCALE_X] + xsdiff * val;
                values[Matrix.MSCALE_Y] = values[Matrix.MSCALE_Y] + ysdiff * val;
                activeMatrix.setValues(values);
                setImageMatrix(activeMatrix);
            }
        });

        anim.addListener(new SimpleAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                setImageMatrix(targetMatrix);
            }
        });

        anim.setDuration(duration);
        anim.start();
    }

    private void animateTranslationX() {
        if (getCurrentDisplayedWidth() > getWidth()) {
            //the left edge is too far to the interior
            if (bounds.left > 0) {
                animateMatrixIndex(Matrix.MTRANS_X, 0);
            }
            //the right edge is too far to the interior
            else if (bounds.right < getWidth()) {
                animateMatrixIndex(Matrix.MTRANS_X, bounds.left + getWidth() - bounds.right);
            }
        } else {
            //left edge needs to be pulled in, and should be considered before the right edge
            if (bounds.left < 0) {
                animateMatrixIndex(Matrix.MTRANS_X, 0);
            }
            //right edge needs to be pulled in
            else if (bounds.right > getWidth()) {
                animateMatrixIndex(Matrix.MTRANS_X, bounds.left + getWidth() - bounds.right);
            }
        }
    }

    private void animateTranslationY() {
        if (getCurrentDisplayedHeight() > getHeight()) {
            //the top edge is too far to the interior
            if (bounds.top > 0) {
                animateMatrixIndex(Matrix.MTRANS_Y, 0);
            }
            //the bottom edge is too far to the interior
            else if (bounds.bottom < getHeight()) {
                animateMatrixIndex(Matrix.MTRANS_Y, bounds.top + getHeight() - bounds.bottom);
            }
        } else {
            //top needs to be pulled in, and needs to be considered before the bottom edge
            if (bounds.top < 0) {
                animateMatrixIndex(Matrix.MTRANS_Y, 0);
            }
            //bottom edge needs to be pulled in
            else if (bounds.bottom > getHeight()) {
                animateMatrixIndex(Matrix.MTRANS_Y, bounds.top + getHeight() - bounds.bottom);
            }
        }
    }

    private void animateMatrixIndex(final int index, final float to) {
        ValueAnimator animator = ValueAnimator.ofFloat(matrixValues[index], to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            final float[] values = new float[9];
            Matrix current = new Matrix();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                current.set(getImageMatrix());
                current.getValues(values);
                values[index] = (Float) animation.getAnimatedValue();
                current.setValues(values);
                setImageMatrix(current);
            }
        });
        animator.setDuration(RESET_DURATION);
        animator.start();
    }

    private float getXDistance(final float toX, final float fromX) {
        float xdistance = toX - fromX;

        if (restrictBounds) {
            xdistance = getRestrictedXDistance(xdistance);
        }

        //prevents image from translating an infinite distance offscreen
        if (bounds.right + xdistance < 0) {
            xdistance = -bounds.right;
        }
        else if (bounds.left + xdistance > getWidth()) {
            xdistance = getWidth() - bounds.left;
        }

        return xdistance;
    }

    private float getRestrictedXDistance(final float xdistance) {
        float restrictedXDistance = xdistance;

        if (getCurrentDisplayedWidth() >= getWidth()) {
            if (bounds.left <= 0 && bounds.left + xdistance > 0 && !scaleDetector.isInProgress()) {
                restrictedXDistance = -bounds.left;
            } else if (bounds.right >= getWidth() && bounds.right + xdistance < getWidth() && !scaleDetector.isInProgress()) {
                restrictedXDistance = getWidth() - bounds.right;
            }
        } else if (!scaleDetector.isInProgress()) {
            if (bounds.left >= 0 && bounds.left + xdistance < 0) {
                restrictedXDistance = -bounds.left;
            } else if (bounds.right <= getWidth() && bounds.right + xdistance > getWidth()) {
                restrictedXDistance = getWidth() - bounds.right;
            }
        }

        return restrictedXDistance;
    }

    private float getYDistance(final float toY, final float fromY) {
        float ydistance = toY - fromY;

        if (restrictBounds) {
            ydistance = getRestrictedYDistance(ydistance);
        }

        //prevents image from translating an infinite distance offscreen
        if (bounds.bottom + ydistance < 0) {
            ydistance = -bounds.bottom;
        }
        else if (bounds.top + ydistance > getHeight()) {
            ydistance = getHeight() - bounds.top;
        }

        return ydistance;
    }

    private float getRestrictedYDistance(final float ydistance) {
        float restrictedYDistance = ydistance;

        if (getCurrentDisplayedHeight() >= getHeight()) {
            if (bounds.top <= 0 && bounds.top + ydistance > 0 && !scaleDetector.isInProgress()) {
                restrictedYDistance = -bounds.top;
            } else if (bounds.bottom >= getHeight() && bounds.bottom + ydistance < getHeight() && !scaleDetector.isInProgress()) {
                restrictedYDistance = getHeight() - bounds.bottom;
            }
        } else if (!scaleDetector.isInProgress()) {
            if (bounds.top >= 0 && bounds.top + ydistance < 0) {
                restrictedYDistance = -bounds.top;
            } else if (bounds.bottom <= getHeight() && bounds.bottom + ydistance > getHeight()) {
                restrictedYDistance = getHeight() - bounds.bottom;
            }
        }

        return restrictedYDistance;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        //calculate value we should scale by, ultimately the scale will be startScale*scaleFactor
        scaleBy = (startScale * detector.getScaleFactor()) / matrixValues[Matrix.MSCALE_X];

        //what the scaling should end up at after the transformation
        final float projectedScale = scaleBy * matrixValues[Matrix.MSCALE_X];

        //clamp to the min/max if it's going over
        if (projectedScale < calculatedMinScale) {
            scaleBy = calculatedMinScale / matrixValues[Matrix.MSCALE_X];
        } else if (projectedScale > calculatedMaxScale) {
            scaleBy = calculatedMaxScale / matrixValues[Matrix.MSCALE_X];
        }

        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        startScale = matrixValues[Matrix.MSCALE_X];
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        scaleBy = 1f;
    }

    private final GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                doubleTapDetected = true;
            }

            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            singleTapDetected = true;
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            singleTapDetected = false;
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    };

    private class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {}

        @Override
        public void onAnimationEnd(Animator animation) {}

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    }

    public static final class styleable {
        private styleable() {}
        public static final int[] CustomizedImageView = { 0x7f04030b, 0x7f04030c, 0x7f04030d, 0x7f04030e, 0x7f04030f, 0x7f040310, 0x7f040311, 0x7f040312, 0x7f040313, 0x7f040314 };
        public static final int animateOnReset = 0;
        public static final int autoCenter = 1;
        public static final int autoResetMode = 2;
        public static final int doubleTapToZoom = 3;
        public static final int doubleTapToZoomScaleFactor = 4;
        public static final int maxScale = 5;
        public static final int minScale = 6;
        public static final int restrictBounds = 7;
        public static final int translatable = 8;
        public static final int zoomable = 9;
    }
}