package net.josecortes.com.googlebooksapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.josecortes.com.googlebooksapp.externalservices.GoogleServices;
import net.josecortes.com.googlebooksapp.lazylist.ImageLoader;
import net.josecortes.com.googlebooksapp.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity will work using the pattern Master-Detail. In case of smartphones, it will inject
 * a simple portrait layout and perform standard navigations (Activity->Fragment).
 * <p/>
 * In case of tablet (minimum 900dp with) it will inject another layout, providing two fragments,
 * one for the List and another for the Detail of the selected item.
 * <p/>
 * Strategy based on the recommendations in: http://developer.android.com/guide/practices/tablets-and-handsets.html
 */
public class BookListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    EditText mEditQuery;

    Button mButton;

    // To avoid unnecessary requeries
    String mPreviousTextSearch = null;
    SimpleItemRecyclerViewAdapter mListBooksAdapter;
    ImageLoader imageLoader;

    boolean isPerformingSearch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        imageLoader = new ImageLoader(this);

        // Even it can be achieved with simple ListView, we use the more efficient approach with
        // RecyclerView.

        View recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;

        if (findViewById(R.id.book_detail_container) != null) {
            // In case of landscape, the book_list.xml for w900dp will be injected and the view
            // will exist
            mTwoPane = true;
        }

        mEditQuery = (EditText) findViewById(R.id.edit_query);
        mButton = (Button) findViewById(R.id.btn_search);

        mListBooksAdapter = new SimpleItemRecyclerViewAdapter(new ArrayList<Book>());
        setupRecyclerView((RecyclerView) recyclerView);

        // In this case, the strategy for search will be on demand after clicking the button.
        // It can be performed as the user writes, adding a textWatcher to the editText. In that
        // case, it will be necessary to cancel unnecessary requests to improve the performance.

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = mEditQuery.getText().toString().trim();
                if ("".equals(text)) {
                    Snackbar.make(v, getString(R.string.empty_search), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    if (!text.equals(mPreviousTextSearch)) {
                        mPreviousTextSearch = text;
                        performSearch(text);

                    }
                }
            }
        });
    }


    private void performSearch(String text) {
        isPerformingSearch = true;
        GoogleServices.performSearch(text, new GoogleServices.OnGoogleServicesListener() {
            @Override
            public void onSearchPerformed(List<Book> bookList, Exception e) {
                if (e == null) {
                    if (bookList != null) {
                        // Everything OK, lets update the UI

                        mListBooksAdapter.setBooks(bookList);
                        mListBooksAdapter.notifyDataSetChanged();

                        if (bookList.isEmpty()) {
                            Snackbar.make(findViewById(R.id.btn_search), getString(R.string.no_items_found),
                                    Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {

                        // Something wrong, no error and no data...shouldn't happen

                        Snackbar.make(findViewById(R.id.btn_search), getString(R.string.unknown_error),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.btn_search), e.getMessage(),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                isPerformingSearch = false;
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mListBooksAdapter);
    }

    /**
     * Adapter for the List
     */

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Book> mBooks;

        public void setBooks(List<Book> books) {
            mBooks = books;
        }


        public SimpleItemRecyclerViewAdapter(List<Book> items) {
            mBooks = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_book, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Book book = mBooks.get(position);
            holder.mItem = book;
            holder.mTitle.setText(book.getTitle());
            holder.mAutor.setText(book.getAuthor());
            imageLoader.DisplayImage(book.getImageThumbnailUrl(), holder.mCover);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Depending on the navigation structure, the
                    // next step for navigation will be to open a new Activity (smartphone)
                    // or load a fragment in the other placeholder.

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(BookDetailFragment.ARG_BOOK, holder.mItem);
                        BookDetailFragment fragment = new BookDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BookDetailActivity.class);
                        intent.putExtra(BookDetailFragment.ARG_BOOK, holder.mItem);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBooks.size();
        }

        /**
         * Standard holder
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitle;
            public final TextView mAutor;
            public final ImageView mCover;

            public Book mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitle = (TextView) view.findViewById(R.id.tvTitle);
                mAutor = (TextView) view.findViewById(R.id.tvAuthor);
                mCover = (ImageView) view.findViewById(R.id.ivCover);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mAutor.getText() + "'";
            }
        }
    }


}
