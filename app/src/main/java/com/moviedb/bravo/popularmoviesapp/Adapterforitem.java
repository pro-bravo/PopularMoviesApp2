package com.moviedb.bravo.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moviedb.bravo.popularmoviesapp.activities.DetailsActivity;
import com.moviedb.bravo.popularmoviesapp.activities.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;





public class Adapterforitem extends RecyclerView.Adapter<Adapterforitem.ViewHolder> {

    private List<mMovie> DataSet;
    private static Context context1;

    public Adapterforitem(Context cont, List<mMovie> dataSet)
    {
        context1=cont;
        DataSet = dataSet;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        if (DataSet.get(position) != null)
        {
            Log.d("", "Element " + position + " set.");
            holder.getTitle().setText(DataSet.get(position).getOriginal_title());
            holder.getText().setText(DataSet.get(position).getOverview());



            if (DataSet.get(position).getPoster() != null)
            {

                Picasso.with(context1).load(DataSet.get(position).getPoster()).into(holder.getPoster());
            }

        }
    }




            public  class ViewHolder extends RecyclerView.ViewHolder
            {
                @BindView(R.id.image)ImageView poster;
                @BindView(R.id.title)TextView title;
                @BindView(R.id.text)TextView text;

                public ViewHolder(View v)
                {
                    super(v);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Element " + getPosition() + " clicked.");
                            if (MainActivity.mTwoPane)
                            {
                                Bundle arguments = new Bundle();
                                arguments.putSerializable(DetFragment.ARG_ITEM_ID, DataSet.get(getPosition()));

                                DetFragment fragment = new DetFragment();
                                fragment.setArguments(arguments);
                                ((FragmentActivity)context1).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.detail_container, fragment)
                                        .commit();
                            }

                            else
                            {
                                Context context2 = v.getContext();
                                Intent intent = new Intent(context2, DetailsActivity.class);
                                intent.putExtra(DetFragment.ARG_ITEM_ID,  DataSet.get(getPosition()));
                                context2.startActivity(intent);
                            }
                        }
                    });
                    ButterKnife.bind(this,v);

                }

                public ImageView getPoster() {
                    return poster;
                }

                public TextView getTitle() {
                    return title;
                }

                public void setTitle(TextView title) {
                    this.title = title;
                }

                public TextView getText() {
                    return text;
                }

                public void setText(TextView text) {
                    this.text = text;
                }
            }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return  new ViewHolder(v);
    }






    @Override
    public int getItemCount() {
                return DataSet.size();
            }
}
