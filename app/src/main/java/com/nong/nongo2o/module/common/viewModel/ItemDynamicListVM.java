package com.nong.nongo2o.module.common.viewModel;

import android.app.Fragment;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entities.request.LikeDynamic;
import com.nong.nongo2o.entities.response.DynamicContent;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.dynamic.activity.DynamicDetailActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-30.
 */

public class ItemDynamicListVM implements ViewModel {

    private Fragment fragment;
    private Moment dynamic;

    @DrawableRes
    public final int imgMainPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgMain = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    @DrawableRes
    public final int imgHeadPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> imgHead = new ObservableField<>();
    public final ObservableField<String> author = new ObservableField<>();
    public final ObservableField<Integer> likeNum = new ObservableField<>();
    @DrawableRes
    public final int imgIsLike = R.mipmap.icon_dianzan;
    @DrawableRes
    public final int imgUnLike = R.mipmap.icon_like_p;


    public ItemDynamicListVM(Fragment fragment, Moment dynamic) {
        this.fragment = fragment;
        this.dynamic = dynamic;

        initData();
    }

    private void initData() {
        if (dynamic.getHeaderImg() != null) {
            List<String> headerImgList = new Gson().fromJson(dynamic.getHeaderImg(), new TypeToken<List<String>>() {
            }.getType());
            imgMain.set(headerImgList.get(0));
        }
        title.set(dynamic.getTitle());
        if (dynamic.getContent() != null) {
            List<DynamicContent> contentList = new Gson().fromJson(dynamic.getContent(), new TypeToken<List<DynamicContent>>() {
            }.getType());
            content.set(contentList.get(0).getContent());
        }
        imgHead.set(dynamic.getUser().getAvatar());
        author.set(dynamic.getUser().getUserNick());
//        viewStyle.isLike.set(dynamic.getIsFavorite() == 1);
        likeNum.set(dynamic.getFavorite());
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isLike = new ObservableBoolean(false);
    }

    public final ReplyCommand detailClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(DynamicDetailActivity.newIntent(fragment.getActivity(), dynamic));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public final ReplyCommand focusClick = new ReplyCommand(() -> {
        if (!viewStyle.isLike.get()) {
            //  点赞
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(new LikeDynamic(dynamic.getMomentCode(), User.getInstance().getUserCode())));

            RetrofitHelper.getDynamicAPI()
                    .likeDynamic(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
//                        dynamic.setIsFavorite(1);
                        viewStyle.isLike.set(true);
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    });
}
