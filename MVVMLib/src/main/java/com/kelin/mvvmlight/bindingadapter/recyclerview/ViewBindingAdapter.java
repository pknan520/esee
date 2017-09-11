package com.kelin.mvvmlight.bindingadapter.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.kelin.mvvmlight.command.ReplyCommand;

import java.util.concurrent.TimeUnit;

import io.reactivex.processors.PublishProcessor;

/**
 * Created by kelin on 16-4-26.
 */
public class ViewBindingAdapter {

    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final RecyclerView recyclerView,
                                             final ReplyCommand<ScrollDataWrapper> onScrollChangeCommand,
                                             final ReplyCommand<Integer> onScrollStateChangedCommand) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int state;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChangeCommand != null) {
                    try {
                        onScrollChangeCommand.execute(new ScrollDataWrapper(dx, dy, state));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (onScrollStateChangedCommand != null) {
                    onScrollChangeCommand.equals(newState);
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    @BindingAdapter({"onLoadMoreCommand"})
    public static void onLoadMoreCommand(final RecyclerView recyclerView, final ReplyCommand<Integer> onLoadMoreCommand) {
        RecyclerView.OnScrollListener listener = new OnScrollListener(onLoadMoreCommand);
        recyclerView.addOnScrollListener(listener);

    }

    public static class OnScrollListener extends RecyclerView.OnScrollListener {

        private PublishProcessor<Integer> methodInvoke = PublishProcessor.create();

        private ReplyCommand<Integer> onLoadMoreCommand;

        private enum layoutManagerType {
            LINEAR_LAYOUT,
            GRID_LAYOUT,
            STAGGERED_GRID_LAYOUT
        }

        private layoutManagerType mLayoutManagerType;
        private int[] lastPositions;
        private int lastVisibleItemPosition;
        private int currentScrollState = 0;

        public OnScrollListener(ReplyCommand<Integer> onLoadMoreCommand) {
            this.onLoadMoreCommand = onLoadMoreCommand;
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(c -> onLoadMoreCommand.execute(c));
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            int visibleItemCount = layoutManager.getChildCount();
//            int totalItemCount = layoutManager.getItemCount();
//            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                if (onLoadMoreCommand != null) {
//                    methodInvoke.onNext(recyclerView.getAdapter().getItemCount());
//                }
//            }

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (mLayoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    mLayoutManagerType = layoutManagerType.LINEAR_LAYOUT;
                } else if (layoutManager instanceof GridLayoutManager) {
                    mLayoutManagerType = layoutManagerType.GRID_LAYOUT;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    mLayoutManagerType = layoutManagerType.STAGGERED_GRID_LAYOUT;
                } else {
                    throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (mLayoutManagerType) {
                case LINEAR_LAYOUT:
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case GRID_LAYOUT:
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID_LAYOUT:
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if (visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.getAdapter().getItemCount());
                }
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }

    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;
        public int state;

        public ScrollDataWrapper(float scrollX, float scrollY, int state) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.state = state;
        }
    }
}
