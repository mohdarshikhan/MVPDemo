package com.mak.app.mvpdemo.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mak.app.mvpdemo.R;


/**
 * This class is to declare Custom Progress Dialog.
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by Mohammad Arshi Khan on 16-02-2018.
 */

public class AppProgressDialog extends Dialog {

    private static final String LOG_TAG = AppProgressDialog.class.getSimpleName();

    private Context mContext;

    public AppProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.layout_custom_progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
}

