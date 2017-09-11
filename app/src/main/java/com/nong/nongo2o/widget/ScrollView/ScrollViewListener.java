package com.nong.nongo2o.widget.ScrollView;

/**
 * Created by Administrator on 2017-7-4.
 */

public interface ScrollViewListener {

    void onScrollChanged(ObservableScrollView view, int x, int y, int oldX, int oldY);
    void onScrollBottom();
}
