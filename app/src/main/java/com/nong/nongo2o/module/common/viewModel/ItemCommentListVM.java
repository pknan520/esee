package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.entities.common.DynamicComment;

/**
 * Created by Administrator on 2017-7-10.
 */

public class ItemCommentListVM implements ViewModel {

    private ItemClickListener listener;
    private DynamicComment comment;

    public final ObservableField<SpannableString> content = new ObservableField<>();

    public interface ItemClickListener {
        void itemClick(DynamicComment comment);
    }

    public ItemCommentListVM(ItemClickListener listener, DynamicComment comment) {
        this.listener = listener;
        this.comment = comment;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        setNameColor(comment.getUserName(), comment.getToUserName(), comment.getContent());
    }

    private void setNameColor(String name, String nameTarget, String comment) {
        if (TextUtils.isEmpty(nameTarget)) {
            String str = name + "：" + comment;
            SpannableString ss = new SpannableString(str);
            int nameIndex = str.indexOf(name);
            ss.setSpan(new ForegroundColorSpan(0xff576b95), nameIndex, nameIndex + name.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            content.set(ss);
        }else {
            String str = name + "回复" + nameTarget + "：" + comment;
            SpannableString ss = new SpannableString(str);
            int nameIndex = str.indexOf(name);
            int nameTargetIndex = str.indexOf(nameTarget);
            ss.setSpan(new ForegroundColorSpan(0xff576b95), nameIndex, nameIndex + name.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(0xff576b95), nameTargetIndex, nameTargetIndex + nameTarget.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            content.set(ss);
        }
    }

    /**
     * 点击评论回复
     */
    public final ReplyCommand commentClick = new ReplyCommand(() -> {
       if (listener != null) listener.itemClick(comment);
    });
}
