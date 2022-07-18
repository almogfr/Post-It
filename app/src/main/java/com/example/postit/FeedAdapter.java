package com.example.postit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.entities.Post;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {


    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView when;
        ImageView profile;
        ImageView img;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.feed_post_name);
            when = itemView.findViewById(R.id.feed_post_when);
            profile = itemView.findViewById(R.id.feed_post_profile_img);
            img = itemView.findViewById(R.id.feed_post_img);
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
            new ImageDownloader(current.getProfileImage(), holder.profile);
            new ImageDownloader(current.getImgUrl(), holder.img);
        }
    }

    public void setPosts(List<Post> s) {
        posts = s;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return 0;
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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.feed_post_layout, parent, false);
//
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.name = convertView.findViewById(R.id.feed_post_name);
//            viewHolder.when = convertView.findViewById(R.id.feed_post_when);
//            viewHolder.profile = convertView.findViewById(R.id.feed_post_profile_img);
//            viewHolder.img = convertView.findViewById(R.id.feed_post_img);
//
//            convertView.setTag(viewHolder);
//        }
//
//        Post p = posts.get(position);
//        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//        viewHolder.name.setText(p.getName());
//        viewHolder.when.setText(p.getWhenPosted());
////        viewHolder.profile.setImageResource(p.getProfileImage());
//        new ImageDownloader(p.getProfileImage(), viewHolder.profile).execute();
//        new ImageDownloader(p.getImgUrl(), viewHolder.img).execute();
////        viewHolder.img.setImageResource(p.getImgUrl());
//
//        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Clicked on image", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        return convertView;
//    }
}
