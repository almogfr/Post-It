package com.example.postit.webservices;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.postit.MyApplication;
import com.example.postit.R;
import com.example.postit.data.PostDao;
import com.example.postit.entities.Post;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetPostsTask extends AsyncTask<Void, Void, Void>
{
    private MutableLiveData<List<Post>> postListData;
    private PostDao dao;

    public GetPostsTask(MutableLiveData<List<Post>> postListData, PostDao dao) {
        this.postListData = postListData;
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... urls)
    {


        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(MyApplication.context.getString(R.string.PostsUrl)+"Post?key="+R.string.api_key);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                result.append(line);
            }

            dao.clear();
            String temp = result.toString();
            Gson gson = new Gson();
//            JsonObject t = gson.fromJson(temp, JsonObject .class);
            JSONObject t = new JSONObject(temp);
//            JSONArray posts = t.getJSONArray("documents");
//            JSONArray posts = new JSONArray(result.toString());
            ArrayList<Post> posts = parse(temp);

            for (int i = 0; i < posts.size(); i++) {
//                JSONObject js = posts.getJSONObject(i).getJSONObject("fields");
//                Post post = new Gson().fromJson(String.valueOf(js), Post.class);
//                post.setImgUrl("R.drawable.ic_baseline_account_circle_24");
                dao.insert(posts.get(i));
            }

            postListData.postValue(dao.get());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return null;
    }

    public ArrayList<Post> parse(String json)
    {
        ArrayList<Post> list = new ArrayList<Post>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray doc = object.getJSONArray("documents");
            for (int j = 0; j < doc.length(); j++) {
                Post post = new Post(null, null, null, null, null, null, 0);
                JSONObject jObject = doc.getJSONObject(j);
                JSONObject fields = jObject.getJSONObject("fields");
                if (fields.has("name") && fields.getJSONObject("name").has("stringValue")) {
                    post.setName(fields.getJSONObject("name").getString("stringValue"));
                }
                if (fields.has("profileImage") && fields.getJSONObject("profileImage").has("stringValue")) {
                    post.setProfileImage(fields.getJSONObject("profileImage").getString("stringValue"));
                }
                if (fields.has("whenPosted") && fields.getJSONObject("whenPosted").has("stringValue")) {
                    post.setWhenPosted(fields.getJSONObject("whenPosted").getString("stringValue"));
                }
                if (fields.has("imgUrl") && fields.getJSONObject("imgUrl").has("stringValue")) {
                    post.setImgUrl(fields.getJSONObject("imgUrl").getString("stringValue"));
                }
                if (fields.has("likes") && fields.getJSONObject("likes").has("integerValue")) {
                    post.setLikes(fields.getJSONObject("likes").getInt("integerValue"));
                }
                if (fields.has("id") && fields.getJSONObject("id").has("stringValue")) {
                    post.setId(fields.getJSONObject("id").getString("stringValue"));
                }
                list.add(post);
            }

        }
        catch(JSONException e)
        {
            //something
        }
        return list;
    }
}