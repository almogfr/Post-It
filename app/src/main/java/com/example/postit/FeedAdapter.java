package com.example.postit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FeedAdapter extends BaseAdapter {
    List<Post> posts;

    private class ViewHolder {
        TextView name;
        TextView when;
        ImageView profile;
        ImageView img;
    }

    public FeedAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feed_post_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.feed_post_name);
            viewHolder.when = convertView.findViewById(R.id.feed_post_when);
            viewHolder.profile = convertView.findViewById(R.id.feed_post_profile_img);
            viewHolder.img = convertView.findViewById(R.id.feed_post_img);

            convertView.setTag(viewHolder);
        }

        Post p = posts.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(p.getName());
        viewHolder.when.setText(p.getWhenPosted());
//        viewHolder.profile.setImageResource(p.getProfileImage());
        new ImageDownloader(p.getProfileImage(), viewHolder.profile).execute();
        new ImageDownloader(p.getImgUrl(), viewHolder.img).execute();
//        viewHolder.img.setImageResource(p.getImgUrl());

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked on image", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}
