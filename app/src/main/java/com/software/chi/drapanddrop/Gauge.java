package com.software.chi.drapanddrop;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

interface IProtocol {
    void maxValue(int maxValue);
}

public class Gauge extends View implements IProtocol {
    private Paint mBg;
    private Paint mProgressPanel;
    private Paint mProgressPoint;
    private Paint mProgressPointBoard;
    private Paint mProgressPointBoardLine;

    private float mMaxValue;

    float radius;
    float center_y;
    float center_x;

    private RectF oval = new RectF();
    private RectF ovalProgress = new RectF();

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

    public Gauge(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        mProgressPointBoard.setShadowLayer(5, 0, 0, 0x55000000);
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
        mBg.setShadowLayer(8, 0, 0, 0x65000000);
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
    public void maxValue(int maxValue) {
        mMaxValue = maxValue;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        radius = getWidth() / 2 - 25;

        oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);
        radius -= 20;
        ovalProgress.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);

        float a = (float) (2.7 * mMaxValue) + 135;
        float x_point = (float) (center_x + radius * cos(toRadians(a)));
        float y_point = (float) (center_y + radius * sin(toRadians(a)));

        radius -= 60;
        float x_StartLine = (float) (center_x + radius * cos(toRadians(a)));
        float y_StartLine = (float) (center_y + radius * sin(toRadians(a)));
//        int[] colors = {0xFF7461F1, 0xFFF16973};
//        float[] positions = {0, 360};
//        SweepGradient sweepGradient = new SweepGradient(center_x, center_y, colors, positions);
//        mProgressPanel.setShader(sweepGradient);
//        mProgressPoint.setShader(sweepGradient);
//        mBg.setShader(sweepGradient);
        canvas.drawArc(oval, 0, 360, false, mBg);
        canvas.drawLine(x_point, y_point, x_StartLine, y_StartLine, mProgressPointBoardLine);
        canvas.drawArc(ovalProgress, 135, 270, false, mProgressPanel);
        canvas.drawCircle(x_point, y_point, 20, mProgressPoint);
        canvas.drawCircle(x_point, y_point, 20, mProgressPointBoard);

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        center_y = getWidth() / 2;
        center_x = getHeight() / 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
