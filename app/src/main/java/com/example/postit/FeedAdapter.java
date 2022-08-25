package com.example.postit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.postit.entities.Post;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter <FeedAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView when;
        TextView text;
        ImageView profile;
        ImageView img;
        ProgressBar progress;
        int likes;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.feed_post_name);
            when = itemView.findViewById(R.id.feed_post_when);
            profile = itemView.findViewById(R.id.feed_post_profile_img);
            img = itemView.findViewById(R.id.feed_post_img);
            text = itemView.findViewById(R.id.comment_text);
            progress = itemView.findViewById(R.id.progress);
        }
    }

    List<Post> posts;

    private final LayoutInflater mInflater;
    private Context mContext = null;

    public FeedAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.feed_post_layout, parent, false);
        mContext = parent.getContext();
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.name.setText(current.getName());
            holder.when.setText(current.getWhenPosted());
            holder.text.setText(current.getText());
            Glide.with(mContext)
                    .load(FirebaseUtils.getStorageRef().child(current.getProfileImage()))
                    .into(holder.profile);
            Glide.with(mContext)
                    .load(FirebaseUtils.getStorageRef().child(current.getImgUrl()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mIntent = new Intent(v.getContext(), ImageClickedActivity.class);
                    mIntent.putExtra("UniqueKey", current);
                    v.getContext().startActivity(mIntent);
                }
            });
        }
    }

    public void setPosts(List<Post> s) {
        posts = s;
        Collections.sort(posts, (p1, p2) -> {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = simpleDateFormat.parse(p1.getWhenPosted());
                d2 = simpleDateFormat.parse(p2.getWhenPosted());
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }

            long Date = d1.getDate();
            long Date1 = d2.getDate();

            return d2.compareTo(d1);
        });
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        return 0;
    }

}
