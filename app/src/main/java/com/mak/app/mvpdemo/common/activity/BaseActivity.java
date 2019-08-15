package com.mak.app.mvpdemo.common.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mak.app.mvpdemo.R;
import com.mak.app.mvpdemo.customviews.AppProgressDialog;
import com.mak.app.mvpdemo.customviews.snackbar.AppSnackbar;

import butterknife.ButterKnife;

import static android.content.pm.PackageManager.GET_META_DATA;

/**
 * This is the base activity which will include the basic functionality of every sub activity. Every activity must
 * be inherited from this.
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getName();

    private AppProgressDialog mDialog;

    protected abstract boolean isFullScreen();

    public abstract void injectDependency();

    public abstract int getLayoutId();

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);
        injectDependency();

        resetTitles();

    }

    private void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showSnackBar(String message) {
        AppSnackbar.make(findViewById(android.R.id.content), message, AppSnackbar.LENGTH_LONG).show();
    }

    public void showToast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void showAlert(String message) {
        showAlert(message, getString(R.string.lbl_ok), null,
                (dialog, which) -> {

                });
    }

    public void showAlert(String message, String posBtnLabel, String negBtnLabel,
                          DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(posBtnLabel, onClickListener);

        if (!TextUtils.isEmpty(negBtnLabel)) {
            builder.setNegativeButton(negBtnLabel, onClickListener);
        }
        builder.show();
    }

    public void showProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = new AppProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    public void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

}
