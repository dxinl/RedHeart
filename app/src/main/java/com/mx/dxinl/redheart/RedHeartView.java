package com.mx.dxinl.redheart;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Deng Xinliang on 2016/10/24.
 *
 * A custom view implementing the design in dribbble
 * https://dribbble.com/shots/2054499-Heart-Constructions
 */

public class RedHeartView extends View {
    private int timer;
    private int padding;

    private Paint paint;
    private RectF oval;

    public RedHeartView(Context context) {
        super(context);
        init();
    }

    public RedHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RedHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RedHeartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        padding = getResources().getDimensionPixelSize(R.dimen.red_heart_padding);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);

        oval = new RectF();

        ValueAnimator animator = ValueAnimator.ofInt(0, 600).setDuration(8000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                timer = (int) animation.getAnimatedValue();
                RedHeartView.super.invalidate();
            }
        });
        animator.start();
    }

    @Override
    public void invalidate() {
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);

        ValueAnimator animator = ValueAnimator.ofInt(0, 600).setDuration(8000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                timer = (int) animation.getAnimatedValue();
                RedHeartView.super.invalidate();
            }
        });
        animator.start();
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() - padding * 2;
        int height = getHeight() - padding * 2;
        int size = width < height ? width : height;
        int unit = size >> 3;
        int radius = unit * 2;
        int centerX = width >> 1;
        int centerY = padding + unit * 5;

        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        if (timer <= 250) {
            drawRectangle(canvas, radius, centerX, centerY);
            drawCircle(canvas, radius, centerX, centerY);
            drawDoubleCircle(canvas, radius, centerX, centerY);
        } else if (timer <= 600) {
            rotate(canvas, centerX, centerY);

            if (timer <= 500) {
                paint.setStrokeWidth(3);
                drawRectangle(canvas, radius, centerX, centerY);
                drawDoubleCircle(canvas, radius, centerX, centerY);
            }

            if (timer > 300) {
                paint.setAlpha(255);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);

                drawLeftHalfCircle(canvas, radius, centerX, centerY);
                drawBottomLine(canvas, radius, centerX, centerY);
                drawLeftLine(canvas, radius, centerX, centerY);
                drawTopHalfCircle(canvas, radius, centerX, centerY);
                drawWholeHeart(canvas, radius, centerX, centerY);
            }
        }
    }

    private void drawRectangle(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer > 100) {
            oval.left = centerX - radius;
            oval.top = centerY - radius;
            oval.right = centerX + radius;
            oval.bottom = centerY + radius;
            canvas.drawRect(oval, paint);
            return;
        }

        float cell = radius * 2f / 25f;
        if (timer > 75) {
            float startX = centerX + radius;
            float startY = centerY - radius;
            int factor = timer - 75;
            float endX = startX - cell * (factor > 25 ? 25 : factor);
            canvas.drawLine(startX, startY, endX, startY, paint);
        }

        if (timer > 50) {
            float startX = centerX + radius;
            float startY = centerY + radius;
            int factor = timer - 50;
            float endY = startY - cell * (factor > 25 ? 25 : factor);
            canvas.drawLine(startX, startY, startX, endY, paint);
        }

        if (timer > 25) {
            float startX = centerX - radius;
            float startY = centerY + radius;
            int factor = timer - 25;
            float endX = startX + cell * (factor > 25 ? 25 : factor);
            canvas.drawLine(startX, startY, endX, startY, paint);
        }

        if (timer > 0) {
            float startX = centerX - radius;
            float startY = centerY - radius;
            float endY = startY + cell * (timer > 25 ? 25 : timer);
            canvas.drawLine(startX, startY, startX, endY, paint);
        }
    }

    private void drawCircle(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 100 || timer > 200) {
            return;
        }

        float factor = timer - 100;
        factor = factor > 100 ? 100 : factor;
        oval.left = centerX - radius;
        oval.top = centerY - radius;
        oval.right = centerX + radius;
        oval.bottom = centerY + radius;
        canvas.drawArc(oval, 270, factor * 3.6f, false, paint);
    }

    private void drawDoubleCircle(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 200) {
            return;
        }

        float factor = timer - 200;
        factor = factor > 50 ? 50 : factor;
        float cell = radius / 50f;
        canvas.drawCircle(centerX, centerY - cell * factor, radius, paint);
        canvas.drawCircle(centerX + cell * factor, centerY, radius, paint);
    }

    private void drawLeftHalfCircle(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 300) {
            return;
        }

        int factor = timer - 300;
        factor = factor > 50 ? 50 : factor;
        float cell = 180 / 50f;

        oval.left = centerX;
        oval.top = centerY - radius;
        oval.right = centerX + radius * 2;
        oval.bottom = centerY + radius;
        canvas.drawArc(oval, 270, cell * factor, false, paint);
    }

    private void drawBottomLine(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 350) {
            return;
        }

        int factor = timer - 350;
        factor = factor > 50 ? 50 : factor;
        float cell = radius * 2 / 50f;
        float startX = centerX + radius;
        float startY = centerY + radius;
        float endX = startX - cell * factor;
        canvas.drawLine(startX, startY, endX, startY, paint);
    }

    private void drawLeftLine(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 400) {
            return;
        }

        int factor = timer - 400;
        factor = factor > 50 ? 50 : factor;
        float cell = radius * 2 / 50f;
        float startX = centerX - radius;
        float startY = centerY + radius;
        float endY = startY - cell * factor;
        canvas.drawLine(startX, startY, startX, endY, paint);
    }

    private void drawTopHalfCircle(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 450) {
            return;
        }

        int factor = timer - 450;
        factor = factor > 50 ? 50 : factor;
        float cell = 180 / 50f;

        oval.left = centerX - radius;
        oval.top = centerY - radius * 2;
        oval.right = centerX + radius;
        oval.bottom = centerY;
        canvas.drawArc(oval, -180, cell * factor, false, paint);
    }

    private void drawWholeHeart(Canvas canvas, int radius, int centerX, int centerY) {
        if (timer <= 500) {
            return;
        }

        int factor = timer - 500;
        factor = factor > 100 ? 100 : factor;
        float cell = 255 / 100f;

        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha((int) cell * factor);

        oval.left = centerX;
        oval.top = centerY - radius;
        oval.right = centerX + radius * 2;
        oval.bottom = centerY + radius;
        canvas.drawArc(oval, 270, 180, true, paint);

        oval.left = centerX - radius;
        oval.top = centerY - radius * 2;
        oval.right = centerX + radius;
        oval.bottom = centerY;
        canvas.drawArc(oval, -180, 180, true, paint);

        oval.left = centerX - radius;
        oval.top = centerY - radius;
        oval.right = centerX + radius;
        oval.bottom = centerY + radius;
        canvas.drawRect(oval, paint);
    }

    private void rotate(Canvas canvas, int centerX, int centerY) {
        if (timer <= 250) {
            return;
        }

        int factor = timer - 250;
        factor = factor > 50 ? 50 : factor;
        float alphaCell = 225 / 50f;
        paint.setAlpha(255 - (int) alphaCell * factor);

        float cell = -45 / 50f;
        canvas.rotate(factor * cell, centerX, centerY);
    }
}
