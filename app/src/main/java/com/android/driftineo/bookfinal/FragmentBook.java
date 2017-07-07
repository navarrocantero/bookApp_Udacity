package com.android.driftineo.bookfinal;

/**
 * Created by driftineo on 25/6/17.
 */


import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.driftineo.bookfinal.model.Book;

import java.util.ArrayList;

public class FragmentBook extends Fragment {

    private ListView listView;
    private ArrayList<Book> books;
    private BookAdapter adapter;
    private TextView mesaggeTextView;
    public static final String BOOKS = "books";


    public static final String GOOGLE_URL =
            "https://www.googleapis.com/books/v1/volumes?maxResults=25&q=";

    public FragmentBook() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        listView = (ListView) getActivity().findViewById(R.id.FragmentListado);
        Button button = (Button) getActivity().findViewById(R.id.search);
        books = new ArrayList<Book>();
        adapter = new BookAdapter(this, books);

        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textViewSearchValue = (TextView) getActivity().findViewById(R.id.searchValue);
                mesaggeTextView = (TextView) getActivity().findViewById(R.id.messageTextView);
                String paramToSearch = textViewSearchValue.getText().toString().trim();
                String blankString = "";
                String finalStringToSearch = GOOGLE_URL + paramToSearch;

                if (isNetDisponible()) {
                    BookAsyncTask searchBookATask = new BookAsyncTask(new AsyncResponse() {
                        @Override
                        public void processFinish(ArrayList<Book> bookArrayList) {
                            if (bookArrayList != null) {
                                adapter.clear();
                                adapter.addAll(bookArrayList);
                                adapter.notifyDataSetChanged();
                                Util.newMesagge(mesaggeTextView, getString(R.string.found) + "  " + bookArrayList.size() + "  " + getString(R.string.items));
                            } else {
                                Util.newMesagge(mesaggeTextView, getString(R.string.no_results));
                            }
                        }
                    });

                    if (paramToSearch.equals(blankString)) {
                        Util.newMesagge(mesaggeTextView, getString(R.string.error) + "  " + getString(R.string.no_parameters));
                        searchBookATask.cancel(true);
                    } else {
                        Util.newMesagge(mesaggeTextView, getString(R.string.searching) + "  " + paramToSearch);
                        searchBookATask.execute(finalStringToSearch);
                    }
                } else {
                    Util.newMesagge(mesaggeTextView, getString(R.string.error) + "  " + getString(R.string.no_internet));
                }
                adapter.clear();
            }


        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BOOKS, books);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(BOOKS);
            adapter.clear();
            adapter.addAll(books);
            listView.setAdapter(adapter);
        }
    }

    public class BookAdapter extends ArrayAdapter<Book> {

        ArrayList<Book> books;

        BookAdapter(Fragment context, ArrayList<Book> books) {
            super(context.getActivity(), R.layout.listitem_book, books);
            this.books = books;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Book book = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_book, parent, false);
            }

            TextView bookName = (TextView) convertView.findViewById(R.id.title);
            TextView bookAuthor = (TextView) convertView.findViewById(R.id.author);


            bookName.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());

            return convertView;
        }


    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
}


