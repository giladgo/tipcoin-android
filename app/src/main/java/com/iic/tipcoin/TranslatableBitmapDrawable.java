package com.iic.tipcoin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by ggoldberg on 17/6/15.
 */
public class TranslatableBitmapDrawable extends BitmapDrawable {

    private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
    private boolean mRebuildShader = true;

    private Matrix mMatrix = new Matrix();

    private int mTranslateX = 0;
    private int mTranslateY = 0;

    public TranslatableBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }


    public TranslatableBitmapDrawable(Resources res, String filepath) {
        super(res, filepath);
    }

    public int getTranslateX() {
        return mTranslateX;
    }

    public void setTranslateX(int mTranslateX) {
        this.mTranslateX = mTranslateX;
        this.invalidateSelf();
    }

    public int getTranslateY() {
        return mTranslateY;
    }

    public void setTranslateY(int mTranslateY) {
        this.mTranslateY = mTranslateY;
        this.invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {

        Bitmap bitmap = getBitmap();


        if (bitmap == null) {
            return;
        }

        if (mRebuildShader) {
            mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
            mRebuildShader = false;
        }

        canvas.save();

        mMatrix.setTranslate(mTranslateX, mTranslateY);
        mPaint.getShader().setLocalMatrix(mMatrix);
        canvas.drawRect(getBounds(), mPaint);
        canvas.restore();
    }


}
