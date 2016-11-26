package com.moviedb.bravo.popularmoviesapp;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ListItemsFragment extends Fragment  {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private Menu mu;
    protected Adapterforitem itemAdap;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<mMovie> dataSet;
    protected List<mMovie> favoriteDataset;

    public ListItemsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dataSet = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_item, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        itemAdap = new Adapterforitem(getActivity(),dataSet);
        mRecyclerView.setAdapter(itemAdap);

        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        //noinspection ResourceAsColor
        mSwipeRefreshLayout.setColorScheme(
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorAccent, R.color.colorPrimaryDark);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (!mSwipeRefreshLayout.isRefreshing())
        {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        initiateRefresh(0);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // handel swipe refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh(0);

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
        this.mu =  menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.top_rated:
                initiateRefresh(1);
                return true;

            case R.id.favorite:
                showFavorite();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showFavorite()
    {
        favoriteDataset = new ArrayList<>();

        for (String id : new FavList(getActivity()).findAllFavoriteMovies()) {
            for (int j = 0; j < dataSet.size(); j++) {
                if (dataSet.get(j).getID().equalsIgnoreCase(id))
                    favoriteDataset.add(dataSet.get(j));
            }

        }


        clearDataSet();
        dataSet.addAll(0, favoriteDataset);
        itemAdap.notifyDataSetChanged();
    }

    public  void initiateRefresh(int i)
    {
       String Url;
        if(i!=0)
            Url="http://api.themoviedb.org/3/movie/top_rated?api_key=7eca8c78f59ad3de8b7a19bfeab535fb";
        else
            Url="http://api.themoviedb.org/3/movie/popular?api_key=7eca8c78f59ad3de8b7a19bfeab535fb";
        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                clearDataSet();
                Iterator iterator = Parser.parseStringToJson(data).iterator();
                while (iterator.hasNext()){
                    mMovie movie = (mMovie)iterator.next();
                    dataSet.add(movie);
                    itemAdap.notifyItemInserted(dataSet.size() - 1);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("response", response);
                clearDataSet();
                Iterator iterator = Parser.parseStringToJson(response).iterator();
                while (iterator.hasNext()){
                    mMovie movie = (mMovie)iterator.next();
                    dataSet.add(movie);
                    itemAdap.notifyItemInserted(dataSet.size() - 1);
                }
                onRefreshComplete();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Stop the refreshing indicator
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d("response", error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void clearDataSet()
    {
        if (dataSet != null){
            dataSet.clear();
            itemAdap.notifyDataSetChanged();
        }
    }
    private void onRefreshComplete()
    {
        mSwipeRefreshLayout.setRefreshing(false);

    }

}
