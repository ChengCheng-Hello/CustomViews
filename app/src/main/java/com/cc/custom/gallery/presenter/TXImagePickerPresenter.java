package com.cc.custom.gallery.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cc.custom.gallery.TXImagePickerManager;
import com.cc.custom.gallery.listener.TXAlbumTaskListener;
import com.cc.custom.gallery.listener.TXImageTaskListener;
import com.cc.custom.gallery.model.TXAlbumModel;
import com.cc.custom.gallery.model.TXImageModel;
import com.cc.custom.gallery.task.TXImageTask;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cheng on 17/3/24.
 */

public class TXImagePickerPresenter implements TXImagePickerContract.Presenter {

    private static final String TAG = "TXImagePickerPresenter";

    private TXImagePickerContract.View mView;

    private int mTotalPage;
    private int mCurrentPage;
    private boolean mIsLoadingNextPage;

    private String mCurrentAlbumId;
    private LoadAlbumTask mAlbumTask;
    private LoadImagesTask mImageTask;

    public TXImagePickerPresenter(TXImagePickerContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        mAlbumTask = new LoadAlbumTask(this);
        mImageTask = new LoadImagesTask(this);
    }

    @Override
    public void loadAlbums() {
        TXImagePickerManager.getInstance().loadAlbum(mAlbumTask);
    }

    @Override
    public void loadImages(String albumId, int page) {
        mCurrentAlbumId = albumId;
        if (page == 0) {
            mView.clearImages();
        }

        TXImagePickerManager.getInstance().loadImages(albumId, page, mImageTask);
    }

    @Override
    public boolean hasNextPage() {
        return mCurrentPage < mTotalPage;
    }

    @Override
    public boolean canLoadNextPage() {
        return !mIsLoadingNextPage;
    }

    @Override
    public void onLoadNextPage() {
        mCurrentPage++;
        mIsLoadingNextPage = true;
        loadImages(mCurrentAlbumId, mCurrentPage);
    }

    @Override
    public void checkSelectedImages(List<TXImageModel> allImageList, List<TXImageModel> selectedImageList) {
        if (allImageList == null || allImageList.size() == 0) {
            return;
        }
        if (selectedImageList == null || selectedImageList.size() == 0) {
            return;
        }

        Map<String, TXImageModel> tmpMap = new HashMap<>();
        for (TXImageModel item : allImageList) {
            item.setSelected(false);
            tmpMap.put(item.getFilePath(), item);
        }
        for (int i = 0, len = selectedImageList.size(); i < len; i++) {
            TXImageModel item = selectedImageList.get(i);
            String filePath = item.getFilePath();
            if (tmpMap.containsKey(filePath)) {
                TXImageModel itemInAll = tmpMap.get(filePath);
                itemInAll.setSelected(true);
                itemInAll.setCurrentPosition(i + 1);
                selectedImageList.set(i, itemInAll);
            }
        }
        tmpMap = null;
    }

    @Override
    public void resetPosition(@NonNull List<TXImageModel> selectedImageList) {
        for (int i = 0, len = selectedImageList.size(); i < len; i++) {
            TXImageModel imageModel = selectedImageList.get(i);
            imageModel.setCurrentPosition(i + 1);
        }
    }

    private static class LoadImagesTask implements TXImageTaskListener {

        private WeakReference<TXImagePickerPresenter> mWr;

        public LoadImagesTask(TXImagePickerPresenter presenter) {
            this.mWr = new WeakReference<>(presenter);
        }

        private TXImagePickerPresenter getPresenter() {
            if (mWr == null) {
                return null;
            }
            return mWr.get();
        }

        @Override
        public void postImages(List<TXImageModel> imageList, int count, String bucketId) {
            TXImagePickerPresenter presenter = getPresenter();
            if (presenter == null) {
                return;
            }

            if (!presenter.mCurrentAlbumId.equals(bucketId)) {
                return;
            }

            TXImagePickerContract.View view = presenter.mView;
            if (view != null) {
                view.showImages(imageList, count);
            }
            presenter.mTotalPage = count / TXImageTask.PAGE_LIMIT;
            presenter.mIsLoadingNextPage = false;
        }

        @Override
        public boolean needFilter(String path) {
            return TextUtils.isEmpty(path) || !new File(path).exists();
        }
    }

    private static class LoadAlbumTask implements TXAlbumTaskListener {

        private WeakReference<TXImagePickerPresenter> mWr;

        public LoadAlbumTask(TXImagePickerPresenter presenter) {
            this.mWr = new WeakReference<>(presenter);
        }

        private TXImagePickerPresenter getPresenter() {
            if (mWr == null) {
                return null;
            }
            return mWr.get();
        }

        @Override
        public void postAlbumList(List<TXAlbumModel> list) {
            TXImagePickerPresenter presenter = getPresenter();
            if (presenter == null) {
                return;
            }
            TXImagePickerContract.View view = presenter.mView;
            if (view != null) {
                view.showAlbums(list);
            }
        }
    }
}
