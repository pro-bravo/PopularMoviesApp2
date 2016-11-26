package com.moviedb.bravo.popularmoviesapp;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DetFragment extends Fragment {

    @BindView(R.id.textView) TextView title;
    @BindView(R.id.textView3) TextView relDate;
    @BindView(R.id.textView2) TextView moreview;
    @BindView(R.id.ratingBar) RatingBar rank;
    @BindView(R.id.imageView) ImageView background;
    @BindView(R.id.button) Button review;
    @BindView(R.id.recyclerView) RecyclerView recyclerView1;
    @BindView(R.id.floatingActionButton) FloatingActionButton fab;
    @BindView(R.id.imageButton) ImageButton imageButton;

    public static final String ARG_ITEM_ID = "item_id";
    private static mMovie model;
    private  String video_id;
    protected List<revModel> reviewData;
    private Adapterforreview reviewAdapter;
    public  LinearLayoutManager mLayoutManager;
   /* public static SharedPreferences settings;
    public static int index=0;*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

       //settings = getActivity().getSharedPreferences("favoritData", MODE_PRIVATE);
        reviewData=new ArrayList<>();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            model = (mMovie) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.detials_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView1.setHasFixedSize(true);
        reviewAdapter = new Adapterforreview(reviewData);
        recyclerView1.setAdapter(reviewAdapter);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestReviews(model.getID(),1);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReviews(model.getID(),0);
                watchYoutubeVideo(video_id);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                /*SharedPreferences.Editor myeditor = settings.edit();
                myeditor.putString("movieID"+index, model.getID());
                index++;
                myeditor.commit();*/

                new FavList(getActivity()).SaveMovieID(model.getID());
            }
        });

        title.setText(model.getOriginal_title());
        relDate.setText(model.getRelease_date());
        moreview.setText(model.getOverview());
        rank.setRating(Float.parseFloat(model.getVote_average())/2);

        Picasso.with(getActivity()).load(model.getBackdrop_path()).into(background);
        return view;
    }

    public void watchYoutubeVideo(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    public void requestReviews(String id,int i) {
        String Url ="";
        if(i==0) {
            Url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=7eca8c78f59ad3de8b7a19bfeab535fb";
        }
        else
        {
            Url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=7eca8c78f59ad3de8b7a19bfeab535fb";
        }
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url);
        if (entry != null)
        {
            try                                   // fetch the data from cache
            {
                String data = new String(entry.data, "UTF-8");
                if(i==0)
                {
                    video_id= Parser.getVideoID(data);
                }
                clearDataSet();
                Iterator iterator = Parser.parseDataToReview(data).iterator();

                while (iterator.hasNext())
                {
                    revModel rMovie = (revModel)iterator.next();
                    reviewData.add(rMovie);
                    reviewAdapter.notifyItemInserted(reviewData.size() - 1);
                }

            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

        }
        /////////////connection//////////
        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("response", response);
                clearDataSet();
                Iterator iterator = Parser.parseDataToReview(response).iterator();
                while (iterator.hasNext())
                {
                    revModel rMovie = (revModel)iterator.next();
                    reviewData.add(rMovie);
                    reviewAdapter.notifyItemInserted(reviewData.size() - 1);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", error.toString());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void clearDataSet()
    {
        if (reviewData != null){
            reviewData.clear();
            reviewAdapter.notifyDataSetChanged();
        }
    }

}
