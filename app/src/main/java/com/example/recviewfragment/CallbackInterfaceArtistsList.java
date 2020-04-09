package com.example.recviewfragment;

import java.util.List;

public interface CallbackInterfaceArtistsList {
    void onSuccess (List<ItemArtist> list);
    void onFailure (String errorCode);
}
