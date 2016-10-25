package pl.janusz.hain.androidplayground.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * <br>
 * Listener for getting call when {@link RecyclerView} is scrolled to bottom.<br>
 * Also there is method to check if all views are visible, so RecyclerView can load more items if needed with larger screens.
 */
public class ListenerRecyclerViewScrolledToBottom extends RecyclerView.OnScrollListener {

    private RecyclerViewScrolledToBottomCallback scrolledToBottomCallback;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private Context context;
    private LinearLayoutManager layoutManager;

    public interface RecyclerViewScrolledToBottomCallback {
        void onScrolledToBottom();
    }

    public ListenerRecyclerViewScrolledToBottom(Fragment fragment, RecyclerView recyclerView, RecyclerView.Adapter recyclerViewAdapter) {
        try {
            scrolledToBottomCallback = (RecyclerViewScrolledToBottomCallback) fragment;
            this.recyclerView = recyclerView;
            this.recyclerView.addOnScrollListener(this);
            this.recyclerViewAdapter = recyclerViewAdapter;
            context = fragment.getContext();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement ListViewScrolledToBottomCallback");
        }
    }

    public ListenerRecyclerViewScrolledToBottom(Activity activity, RecyclerView recyclerView, RecyclerView.Adapter recyclerViewAdapter) {
        try {
            scrolledToBottomCallback = (RecyclerViewScrolledToBottomCallback) activity;
            this.recyclerView = recyclerView;
            this.recyclerView.addOnScrollListener(this);
            this.recyclerViewAdapter = recyclerViewAdapter;
            context = activity.getApplicationContext();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement RecyclerViewScrolledToBottomCallback");
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            checkScrolledToBottom();
        }
    }

    private void checkScrolledToBottom() {
        System.out.println("First position:" + layoutManager.findFirstVisibleItemPosition() + "==" + (recyclerViewAdapter.getItemCount() - 1));
        if (layoutManager.findLastVisibleItemPosition() == recyclerViewAdapter.getItemCount() - 1) {
            scrolledToBottomCallback.onScrolledToBottom();
        }
    }
}
