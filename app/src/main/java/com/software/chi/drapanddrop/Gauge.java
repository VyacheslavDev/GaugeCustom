package com.software.chi.drapanddrop;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

interface IProtocol {
    void setVolume(int value);
}

public class Gauge extends View implements IProtocol {
    private Paint mBg;
    private Paint mProgressPanel;
    private Paint mProgressPoint;
    private Paint mProgressPointBoard;
    private Paint mProgressPointBoardLine;

    private float mCurrentValue;

    private float mRadius, mRadiusOvalProgress, mRadiusBlackLine;
    private float center_y;
    private float center_x;
    private float mCorner;

    private float xPointProgress, yPointProgress;
    private float xBlackLine, yBlackLine;

    private RectF oval = new RectF();
    private RectF ovalProgress = new RectF();

    private int ORGANIC_BLUE = 0xFF6045EC;
    private int ORGANIC_PINK = 0xFFFF5471;

    public Gauge(Context context) {
        super(context);
        init();
    }

    public Gauge(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Gauge(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initBg();
        initProgressPanel();
        initProgressPoint();
        initProgressPointBoard();
        initLineProgress();
        setLayerType(LAYER_TYPE_SOFTWARE, mProgressPointBoard);
    }

    private void initProgressPointBoard() {
        mProgressPointBoard = new Paint();
        mProgressPointBoard.setColor(Color.WHITE);
        mProgressPointBoard.setStrokeWidth(8);
        mProgressPointBoard.setStyle(Paint.Style.STROKE);
        mProgressPointBoard.setAntiAlias(true);
        int transparentBlack = 0x55000000;
        mProgressPointBoard.setShadowLayer(5, 0, 0, transparentBlack);
    }

    private void initLineProgress() {
        mProgressPointBoardLine = new Paint();
        mProgressPointBoardLine.setColor(Color.BLACK);
        mProgressPointBoardLine.setStrokeWidth(4);
        mProgressPointBoardLine.setAntiAlias(true);
    }

    private void initProgressPoint() {
        mProgressPoint = new Paint();
        mProgressPoint.setColor(Color.GRAY);
        mProgressPoint.setStyle(Paint.Style.FILL);
        mProgressPoint.setAntiAlias(true);
    }


    private void initBg() {
        mBg = new Paint();
        mBg.setColor(Color.WHITE);
        mBg.setStrokeWidth(5);
        mBg.setStyle(Paint.Style.FILL);
        mBg.setAntiAlias(true);
        int transparentBlack = 0x65000000;
        mBg.setShadowLayer(8, 0, 0, transparentBlack);
    }

    private void initProgressPanel() {
        mProgressPanel = new Paint();
        mProgressPanel.setColor(Color.GRAY);
        mProgressPanel.setStrokeWidth(15);
        mProgressPanel.setStyle(Paint.Style.STROKE);
        mProgressPanel.setAntiAlias(true);
        mProgressPanel.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void setVolume(int value) {
        mCurrentValue = value;
        recount();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas.getWidth() <= 0) {
            return;
        }
        canvas.drawArc(oval, 0, 360, false, mBg);
        canvas.drawLine(xPointProgress, yPointProgress, xBlackLine, yBlackLine, mProgressPointBoardLine);
        canvas.drawArc(ovalProgress, 135, 270, false, mProgressPanel);
        canvas.drawCircle(xPointProgress, yPointProgress, dpToPx(10), mProgressPoint);
        canvas.drawCircle(xPointProgress, yPointProgress, dpToPx(10), mProgressPointBoard);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        center_x = MeasureSpec.getSize(widthMeasureSpec) / 2;
        center_y = MeasureSpec.getSize(heightMeasureSpec) / 2;
        mRadius = center_x - dpToPx(10);
        initView();
        recount();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void recount() {
        mCorner = (float) (2.7 * mCurrentValue) + 135;
        mProgressPoint.setColor((int) new ArgbEvaluator().evaluate(mCurrentValue / 100, ORGANIC_BLUE, ORGANIC_PINK));
        xPointProgress = (float) (center_x + mRadiusOvalProgress * cos(toRadians(mCorner)));
        yPointProgress = (float) (center_y + mRadiusOvalProgress * sin(toRadians(mCorner)));
        xBlackLine = (float) (center_x + mRadiusBlackLine * cos(toRadians(mCorner)));
        yBlackLine = (float) (center_y + mRadiusBlackLine * sin(toRadians(mCorner)));
    }

    private void initView() {
        oval.set(center_x - mRadius, center_y - mRadius, center_x + mRadius, center_y + mRadius);
        mRadiusOvalProgress = mRadius - dpToPx(10);
        ovalProgress.set(center_x - mRadiusOvalProgress, center_y - mRadiusOvalProgress, center_x + mRadiusOvalProgress, center_y + mRadiusOvalProgress);
        mRadiusBlackLine = mRadius - dpToPx(35);
        int[] colors = {ORGANIC_BLUE, ORGANIC_PINK};
        float[] positions = {45 * 0.0036f, 1 - (45 * 0.0036f)};
        SweepGradient sweepGradient = new SweepGradient(center_x, center_y, colors, positions);
        Matrix matrix = new Matrix();
        matrix.setRotate(90, center_x, center_y);
        sweepGradient.setLocalMatrix(matrix);
        mProgressPanel.setShader(sweepGradient);
    }

    private static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
