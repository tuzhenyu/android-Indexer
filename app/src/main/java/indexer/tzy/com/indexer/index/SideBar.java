package indexer.tzy.com.indexer.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SectionIndexer;
import android.widget.TextView;

import indexer.tzy.com.indexer.R;

/**
 * 类描述：该SideBar把 SectionIndexer 和 Adapter 分开，提高灵活性
 * <p/>
 * Created by tzy on 2016/11/12.
 */
public class SideBar extends View {
    //    'A','B','C','D','E','F','G','H','I','J','K','L'
//            ,'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    private Object[] mIndexChar = new Object[]{};
    private SectionIndexer sectionIndexer;
    private ListView mListView;
    private TextView mTextView;
    private PopupWindow mPopupWindow;

    private int mWidth = 50;
    private int mHeight = 0;
    //每一个字母所占的高度
    private int mItemHeight;
    private Paint mPaint;
    private float mBaseLineHeight;
    private boolean isAddHead = false;

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setHead(boolean isHead) {
        isAddHead = isHead;
    }
    public void setIndexChar(Object[] indexChar) {
        mIndexChar = indexChar;
    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_text, null);
        mTextView = (TextView) view.findViewById(R.id.text);
        mPopupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        mPaint = new Paint();
        mPaint.setColor(0xFFA6A9AA);
        mPaint.setTextSize(getResources().getDimensionPixelOffset( R.dimen.sidbar_textsize));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mItemHeight = (int) Math.ceil(mPaint.descent() - mPaint.ascent());
        mBaseLineHeight = Math.abs(mPaint.ascent());
    }

    public void setListView(ListView _list) {
        mListView = _list;
    }


    public void onDataSetChanged(){

        if(sectionIndexer == null){
            return;
        }
        Object[] sections = sectionIndexer.getSections();
        if(sections != null && sections.length > 0){
            mIndexChar = sections;
        }
        mHeight = mItemHeight * (mIndexChar == null ? 0 : mIndexChar.length);
        measure(0, 0);
        invalidate();
    }

    public SectionIndexer getSectionIndexer() {
        return sectionIndexer;
    }

    public void setSectionIndexer(SectionIndexer sectionIndexer) {
        this.sectionIndexer = sectionIndexer;
        onDataSetChanged();
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (mItemHeight == 0){
            return false;
        }
        int y = (int) event.getY();
        int positionY = y / mItemHeight;
        if (positionY >= mIndexChar.length) {
            positionY = mIndexChar.length - 1;
        } else if (positionY < 0) {
            positionY = 0;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mPopupWindow.isShowing()) {
                    mPopupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
                }
            case MotionEvent.ACTION_MOVE:
                int position = sectionIndexer.getPositionForSection(positionY);
                mTextView.setText(String.valueOf(mIndexChar[positionY]));
                if (position != -1) {
                    if (isAddHead) {
                        mListView.setSelection(position + 1);
                    } else {
                        mListView.setSelection(position);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    protected void onDraw(Canvas canvas) {

        if (mIndexChar != null) {
            int len = mIndexChar.length;
            for (int i = 0; i < len; i++) {
                float x = mWidth / 2;
                float y = i * mItemHeight;
                String text = String.valueOf(mIndexChar[i]);
                if(TextUtils.isEmpty(text)){
                    continue;
                }
                canvas.drawText(text, x, y + mBaseLineHeight, mPaint);
            }
        }
    }

}