package net.josecortes.com.googlebooksapp.json;

import net.josecortes.com.googlebooksapp.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Classes prepared to perform JSON mapping from the responses to App entities.
 * <p/>
 * In this case it is only used a simple mapper, in case of managing multiple entities it can be
 * easily splited into different clases (per entity) and abstracting common parsing logic.
 */

public class JSONMapper {

    public static final String JSON_ITEMS = "items";
    public static final String JSON_VOLUME_INFO = "volumeInfo";
    public static final String JSON_TITLE = "title";
    public static final String JSON_AUTHORS = "authors";
    public static final String JSON_IMAGE_LINKS = "imageLinks";
    public static final String JSON_SMALL_THUMB = "smallThumbnail";
    public static final String JSON_THUMB = "thumbnail";


    public static List<Book> listBooksJsonMapper(JSONObject response) {
        List<Book> bookList = new ArrayList<>();
        try {
            JSONArray array = response.optJSONArray(JSON_ITEMS);

            if (array == null)
                return bookList;
            for (int i = 0; i < array.length(); i++) {
                Book book = new Book();
                JSONObject item = array.getJSONObject(i);

                JSONObject volumeInfo = item.optJSONObject(JSON_VOLUME_INFO);
                if (volumeInfo != null) {
                    String title = volumeInfo.getString(JSON_TITLE);
                    book.setTitle(title);
                }

                if (volumeInfo != null) {
                    JSONArray authors = volumeInfo.optJSONArray(JSON_AUTHORS);
                    if (authors != null) {
                        String author = authors.getString(0);
                        book.setAuthor(author);
                    }

                    JSONObject imageLinks = volumeInfo.optJSONObject(JSON_IMAGE_LINKS);
                    if (imageLinks != null) {
                        String imageThumbLink = imageLinks.optString(JSON_SMALL_THUMB);
                        book.setImageThumbnailUrl(imageThumbLink);

                        String imageLink = imageLinks.optString(JSON_THUMB);
                        book.setImageUrl(imageLink);


                    }
                }

                bookList.add(book);
            }

            return bookList;
        } catch (JSONException e) {
            return null;
        }
    }
}
