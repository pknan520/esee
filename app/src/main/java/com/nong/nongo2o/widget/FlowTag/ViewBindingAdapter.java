package com.nong.nongo2o.widget.FlowTag;

import android.databinding.BindingAdapter;

import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

/**
 * Created by Administrator on 2017-7-12.
 */

public class ViewBindingAdapter {

//    @BindingAdapter(value = {"layoutRes", "tagList"}, requireAll = true)
//    public static void setAdapter(TagFlowLayout tag, @LayoutRes int layoutRes, List<String> tagList) {
//        tag.setAdapter(new TagAdapter<String>(tagList) {
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//
//                return null;
//            }
//        });
//    }

    @BindingAdapter(value = {"setAdapter"}, requireAll = false)
    public static void setAdapter(TagFlowLayout tagLayout, TagAdapter adapter) {
        tagLayout.setAdapter(adapter);
    }

}

