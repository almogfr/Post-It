package com.example.postit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.entities.Post;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter <FeedAdapter.PostViewHolder> {

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView when;
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
            progress = itemView.findViewById(R.id.progress);
        }
    }

    List<Post> posts;

    private final LayoutInflater mInflater;

    public FeedAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.feed_post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.name.setText(current.getName());
            holder.when.setText(current.getWhenPosted());
            new ImageDownloader(current.getProfileImage(), holder.profile).execute();
            new ImageDownloader(current.getImgUrl(), holder.img, holder.progress).execute();
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

    public List<Post> getPosts() {

        return posts;
    }

}
