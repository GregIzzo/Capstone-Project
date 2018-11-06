package com.example.android.tagsalenow;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TagSaleNowRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new TagSaleNowRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
