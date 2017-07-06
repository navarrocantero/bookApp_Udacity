package com.android.driftineo.bookfinal;

import com.android.driftineo.bookfinal.model.Book;

import java.util.ArrayList;

/**
 * Created by driftineo on 5/7/17.
 */

interface AsyncResponse {
    void processFinish(ArrayList<Book> bookArrayList);
}
