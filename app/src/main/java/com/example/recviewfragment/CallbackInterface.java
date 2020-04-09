package com.example.recviewfragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public interface CallbackInterface {
    void onSuccess (List<ItemList> list);
    void onFailure (String errorCode);
}
