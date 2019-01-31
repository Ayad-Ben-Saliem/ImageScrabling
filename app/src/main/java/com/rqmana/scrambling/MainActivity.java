package com.rqmana.scrambling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MyTAG";

    private final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS_CODE = 123;

    private final ArrayList<String> bottomImageNames = new ArrayList<>();
    private final ArrayList<String> topImageNames = new ArrayList<>();

    private TopRecyclerViewAdapter topAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE_PERMISSIONS_CODE)) return;

        bottomImageNames.clear();
        bottomImageNames.addAll(Utils.getAllShownImagesPath(this));

        RecyclerView topRecyclerView = findViewById(R.id.topRecyclerView);
        RecyclerView bottomRecyclerView = findViewById(R.id.bottomRecyclerView);

        topRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.setHasFixedSize(true);

        topRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        bottomRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));

        topAdapter = new TopRecyclerViewAdapter();
        topRecyclerView.setAdapter(topAdapter);
        bottomRecyclerView.setAdapter(new BottomRecyclerViewAdapter());
    }

    private class TopRecyclerViewAdapter extends RecyclerView.Adapter<TopViewHolder> {

        @NonNull
        @Override
        public TopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new TopViewHolder(getLayoutInflater().inflate(R.layout.top_image_view, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TopViewHolder topViewHolder, int index) {
            topViewHolder.setImage(index);
        }

        @Override
        public int getItemCount() {
            return topImageNames.size();
        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;

        TopViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (AppCompatImageView) itemView;
        }

        void setImage(int index) {
            imageView.setImageBitmap(Utils.scramble(BitmapFactory.decodeFile(topImageNames.get(index))));
        }
    }

    private class BottomRecyclerViewAdapter extends RecyclerView.Adapter<BottomViewHolder> {

        @NonNull
        @Override
        public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewTypoe) {
            return new BottomViewHolder(getLayoutInflater().inflate(R.layout.buttom_image_view, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BottomViewHolder bottomViewHolder, int index) {
            bottomViewHolder.setImage(index);
        }

        @Override
        public int getItemCount() {
            return bottomImageNames.size();
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder {

        private boolean isChecked;
        private View itemView;

        BottomViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        void setImage(int index) {
            AppCompatImageView iv = itemView.findViewById(R.id.iv);
            iv.setImageBitmap(BitmapFactory.decodeFile(bottomImageNames.get(index)));

            itemView.findViewById(R.id.iv).setOnClickListener(v -> {
                isChecked = !isChecked;
                if (isChecked) {
                    itemView.findViewById(R.id.signIV).setVisibility(View.VISIBLE);
                    topImageNames.add(bottomImageNames.get(index));
                } else {
                    itemView.findViewById(R.id.signIV).setVisibility(View.INVISIBLE);
                    topImageNames.remove(bottomImageNames.get(index));
                }
            });
        }
    }

    public void onScrambleBtnClicked(View view) {
        topAdapter.notifyDataSetChanged();
    }

    public void onRecoverBtnClicked(View view) {
        topAdapter.notifyDataSetChanged();;

    }

    public void onSaveBtnClicked(View view) {
        for (String imagePath : topImageNames) {
            Utils.saveImage(this, Utils.scramble(BitmapFactory.decodeFile(imagePath)));
            Utils.deleteImage(this, imagePath);
        }
        topImageNames.clear();
        topAdapter.notifyDataSetChanged();

        bottomImageNames.clear();
        bottomImageNames.addAll(Utils.getAllShownImagesPath(this));
    }
}
