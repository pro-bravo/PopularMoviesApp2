package com.moviedb.bravo.popularmoviesapp;



import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




public class Adapterforreview extends RecyclerView.Adapter<Adapterforreview.ViewHolder> {

    private List<revModel> DataSet;


    public Adapterforreview(List<revModel> dataSet)
    {
        DataSet = dataSet;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.auther)TextView auther;
        @BindView(R.id.link)TextView link;
        @BindView(R.id.review)TextView review;

        public ViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this,v);
        }

        public TextView getAuther() {
            return auther;
        }

        public TextView getLink() {
            return link;
        }

        public TextView getReview() {
            return review;
        }

    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        if (DataSet.get(position) != null) {
            Log.d("", "Element " + position + " set.");
            holder.getAuther().setText(DataSet.get(position).getAuther());
            holder.getLink().setText(Html.fromHtml("<a href=\"" + DataSet.get(position).getUrl() + "\">"
                    + DataSet.get(position).getUrl() + "</a> "));
            holder.getLink().setMovementMethod(LinkMovementMethod.getInstance());
            holder.getReview().setText(DataSet.get(position).getContent());

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return  new ViewHolder(v);
    }



    @Override
    public int getItemCount() {
        return DataSet.size();
    }
}
