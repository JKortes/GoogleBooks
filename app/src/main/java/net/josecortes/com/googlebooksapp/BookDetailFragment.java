package net.josecortes.com.googlebooksapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.josecortes.com.googlebooksapp.lazylist.ImageLoader;
import net.josecortes.com.googlebooksapp.model.Book;


/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {

    /**
     * Argument for the fragment
     */
    public static final String ARG_BOOK = "book";

    /**
     * The item to show the details
     */
    private Book mBook;

    /**
     * Loader to help asynchronous loading of image. Internally provides a cache (LRU strategy) based
     * in the URL of the image to avoid recalls.
     */
    ImageLoader imageLoader;


    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader(getActivity());

        if (getArguments().containsKey(ARG_BOOK)) {
            mBook = (Book) getArguments().getSerializable(ARG_BOOK);

            assert (mBook != null);


            Activity activity = this.getActivity();

            // In case the parent activity provides the item "toolbar_layout" the Fragment
            // will be in charge of setting the title.

            // It creates soft coupling, the other option will be to delegate the setting
            // of the title to the Activities, but that case will slightly increase the complexity
            // and code duplication.

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity
                    .findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mBook.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        TextView txtBookDetail = (TextView) rootView.findViewById(R.id.book_detail);
        ImageView imgBookImage = (ImageView) rootView.findViewById(R.id.ivCover);

        assert (mBook != null);
        assert (mBook.getTitle() != null);

        String image = mBook.getImageUrl();
        String imageThumb = mBook.getImageThumbnailUrl();

        txtBookDetail.setText(mBook.getTitle());
        if (image != null) {
            imageLoader.DisplayImage(image, imgBookImage);
        } else {
            if (imageThumb != null) {
                imageLoader.DisplayImage(imageThumb, imgBookImage);
            }
        }


        return rootView;
    }
}
