package ie.iamshanedoyle.craftbeers.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * This is a custom ScrollView that accepts a scroll listener. Heavily based off an article
 * by Cyril Mottier. So thank you Cyril!
 * <p/>
 * http://cyrilmottier.com/2013/05/24/pushing-the-actionbar-to-the-next-level/
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class ObservableScrollView extends ScrollView {

    private ScrollListener mScrollListener;
    private boolean mIsOverScrollEnabled = true;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mScrollListener != null) {
            mScrollListener.onScrollChanged(t);
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void addCallbacks(ScrollListener listener) {
        mScrollListener = listener;
    }

    public boolean isOverScrollEnabled() {
        return mIsOverScrollEnabled;
    }

    public void setOverScrollEnabled(boolean enabled) {
        mIsOverScrollEnabled = enabled;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(
                deltaX,
                deltaY,
                scrollX,
                scrollY,
                scrollRangeX,
                scrollRangeY,
                mIsOverScrollEnabled ? maxOverScrollX : 0,
                mIsOverScrollEnabled ? maxOverScrollY : 0,
                isTouchEvent);
    }

    /**
     * A simple interface to be used as a Scroll Listener.
     */
    public static interface ScrollListener {
        public void onScrollChanged(int deltaY);
    }
}
