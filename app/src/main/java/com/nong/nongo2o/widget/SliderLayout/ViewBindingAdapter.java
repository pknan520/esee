package com.nong.nongo2o.widget.SliderLayout;

import android.databinding.BindingAdapter;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.List;

/**
 * Created by Administrator on 2017-7-8.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"defaultSliderView"})
    public static void addDefaultSliderView(SliderLayout sliderLayout, List<DefaultSliderView> views) {
        if (views != null && !views.isEmpty()) {
            for (DefaultSliderView view : views) {
                sliderLayout.addSlider(view);
            }
        }
    }
}
