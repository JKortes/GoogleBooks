package net.josecortes.com.googlebooksapp.externalservices;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.josecortes.com.googlebooksapp.AppController;
import net.josecortes.com.googlebooksapp.json.JSONMapper;
import net.josecortes.com.googlebooksapp.model.Book;

import org.json.JSONObject;

import java.util.List;

/**
 * This layer will be in charge of abstracting the communication from UI to the external services
 * (Google API).
 * <p/>
 * In case other services are needed, they can be added here.
 */

public class GoogleServices {


    private static final String GOOGLE_API_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Method to perform searchs on Google API books
     *
     * @param query
     */

    public static void performSearch(String query, final OnGoogleServicesListener listener) {
        if (query != null && query.length() > 0) {
            query = sanitizeQuery(query);
            String url = GOOGLE_API_BOOKS_URL + query;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    List<Book> bookList = JSONMapper.listBooksJsonMapper(response);

                    if (bookList != null) {
                        // Everything was OK
                        if (listener != null) {
                            listener.onSearchPerformed(bookList, null);
                        }

                    } else {
                        // Error parsing JSON. For this case, a strategy should be defined, such as
                        // re-throwing the Exception to upper layers and let the UI show the error
                        // in a friendly way (localized)

                        if (listener != null) {
                            listener.onSearchPerformed(null, new Exception("Error parsing JSON"));
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (listener != null) {
                        listener.onSearchPerformed(null, error);
                    }
                }
            });


            AppController.getInstance().addToRequestQueue(request);

        }
    }


    public static String sanitizeQuery(String query) {
        if (query == null)
            return null;
        // We replace some random chars, but a real entity replacement should be performed.
        return query.replaceAll("%", "").replaceAll("&", "").replaceAll("/", "").trim();
    }


    public interface OnGoogleServicesListener {
        void onSearchPerformed(List<Book> bookList, Exception e);
    }

}
