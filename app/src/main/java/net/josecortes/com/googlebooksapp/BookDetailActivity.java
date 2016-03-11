package net.josecortes.com.googlebooksapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Activity to show the details of a book, used for smartphones (portrait)
 */
public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // If non-null the fragment state is already saved (e.g. screen rotation) and will be
        // automatically re-added.
        // Otherwise, the fragment should be recreated.

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(BookDetailFragment.ARG_BOOK,
                    getIntent().getSerializableExtra(BookDetailFragment.ARG_BOOK));
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // When back, the Activity is removed.
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
