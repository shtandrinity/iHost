package com.example.recviewfragment.CallbackInterfaces;

import com.example.recviewfragment.Model.ItemArtist;

import java.util.List;

public interface CallbackInterfaceArtistsList {
    void onSuccess (List<ItemArtist> list);
    void onFailure (String errorCode);
}
