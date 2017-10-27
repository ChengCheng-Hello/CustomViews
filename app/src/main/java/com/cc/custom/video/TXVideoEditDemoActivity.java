package com.cc.custom.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cc.custom.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 类的一句话描述
 * <p>
 * TODO: 类的功能和使用详细描述
 * <p>
 * Created by Cheng on 2017/10/9.
 */
public class TXVideoEditDemoActivity extends FragmentActivity {

    private static final String TAG = "TXVideoEditDemoActivity";
    private MediaMetadataRetriever mRetriever;
    private MyAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TXVideoEditDemoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_edit_demo);

        ImageView ivThumb = (ImageView) findViewById(R.id.iv_thumb);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new MyAdapter();
        rv.setAdapter(mAdapter);

        mRetriever = new MediaMetadataRetriever();

        mRetriever.setDataSource("/mnt/sdcard/2.mp4");

        String width = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String duration = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        System.out.println(TAG + " w " + width + ", h " + height + ", duration " + duration);

        int dur = Integer.parseInt(duration);
        dur = dur / 1000;

        Bitmap bitmap = mRetriever.getFrameAtTime(1);
        ivThumb.setImageBitmap(bitmap);

        File file = new File("/mnt/sdcard/DCIM/Camera/test.jpeg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {

            List<String> list;

            @Override
            protected Void doInBackground(Integer...params) {
                int dur = params[0];
                list = new ArrayList<>(dur);

                for (int i = 0; i < dur; i++) {
                    File file = new File("/mnt/sdcard/Pictures/test_" + i + ".jpg");
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap thbBitmap = Bitmap.createScaledBitmap(mRetriever.getFrameAtTime(i * 1000 * 1000 + 1), 50, 100, false);

                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(file);
                        thbBitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    list.add(file.getAbsolutePath());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.setData(list);
            }
        };

        task.execute(dur);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRetriever.release();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        private List<String> list;

        public void setData(List<String> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_edit, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            String path = list.get(position);

            holder.iv.setImageBitmap(BitmapFactory.decodeFile(path));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {

        private ImageView iv;

        public MyHolder(View itemView) {
            super(itemView);

            iv = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }
}
