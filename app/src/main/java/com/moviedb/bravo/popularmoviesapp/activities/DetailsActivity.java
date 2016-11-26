package com.moviedb.bravo.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.moviedb.bravo.popularmoviesapp.DetFragment;
import com.moviedb.bravo.popularmoviesapp.R;



public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null)
        {
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetFragment.ARG_ITEM_ID,
                    getIntent().getSerializableExtra(DetFragment.ARG_ITEM_ID));
            DetFragment fragment = new DetFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }
}
