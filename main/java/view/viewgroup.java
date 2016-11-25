package view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class viewgroup extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContext;
    private int mScreenWidth;
    private int mMenuRightPadding=100;
    private boolean once=false;
    private int mMenuWidth;

    public viewgroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth=displayMetrics.widthPixels;
        mMenuRightPadding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,context.getResources().getDisplayMetrics());
    }
    public viewgroup(Context context){
      super(context,null);
        /*WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth=displayMetrics.widthPixels;
        mMenuRightPadding= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,context.getResources().getDisplayMetrics());*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if(!once){
                mWapper= (LinearLayout) getChildAt(0);
                mMenu= (ViewGroup) mWapper.getChildAt(0);
                mContext= (ViewGroup) mWapper.getChildAt(1);
                mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPadding;
                mContext.getLayoutParams().width=mScreenWidth;
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                once=true;
            }
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(mMenuWidth,0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int sx=getScrollX();
                if(sx>=mMenuWidth/2){
                    this.smoothScrollTo(mMenuWidth,0);
                }
                else{
                    this.smoothScrollTo(0,0);
                }
                return  true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);
        float s=l*1.0f/mMenuWidth;
        ViewHelper.setTranslationX(mMenu,mScreenWidth*s*0.7f);
        float s2=0.7f+0.3f*s;
        float s3=1.0f-0.3f*s;
        float s4=0.6f+0.4f*(1-s);
        ViewHelper.setScaleX(mMenu,s3);
        ViewHelper.setScaleY(mMenu,s3);
        ViewHelper.setAlpha(mMenu,s4);
        ViewHelper.setPivotX(mContext,0);
        ViewHelper.setPivotY(mContext,mContext.getHeight()/2);
        ViewHelper.setScaleX(mContext,s2);
        ViewHelper.setScaleY(mContext,s2);
    }
}
