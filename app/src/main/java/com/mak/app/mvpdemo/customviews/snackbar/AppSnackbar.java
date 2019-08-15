package com.mak.app.mvpdemo.customviews.snackbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.mak.app.mvpdemo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class is to declare Custom SnackBar
 * <p>
 * Copyright (c) 2018 <ClientName>. All rights reserved.
 * Created by mak on 20/9/18.
 */

public final class AppSnackbar {

    private static final String LOG_TAG = AppSnackbar.class.getSimpleName();

    /**
     * Callback class for {@link AppSnackbar} instances.
     *
     * @see AppSnackbar#setCallback(Callback)
     */
    public static abstract class Callback {
        /**
         * Indicates that the AppSnackbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = 0;
        /**
         * Indicates that the AppSnackbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = 1;
        /**
         * Indicates that the AppSnackbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = 2;
        /**
         * Indicates that the AppSnackbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = 3;
        /**
         * Indicates that the AppSnackbar was dismissed from a new AppSnackbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;

        /**
         * @hide
         */
        @IntDef({DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT,
                DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }

        /**
         * Called when the given {@link AppSnackbar} has been dismissed, either through a time-out,
         * having been manually dismissed, or an action being clicked.
         *
         * @param snackbar The snackbar which has been dismissed.
         * @param event    The event which caused the dismissal. One of either:
         *                 {@link #DISMISS_EVENT_SWIPE}, {@link #DISMISS_EVENT_ACTION},
         *                 {@link #DISMISS_EVENT_TIMEOUT}, {@link #DISMISS_EVENT_MANUAL} or
         *                 {@link #DISMISS_EVENT_CONSECUTIVE}.
         * @see AppSnackbar#dismiss()
         */
        public void onDismissed(AppSnackbar snackbar, @DismissEvent int event) {
            // empty
        }

        /**
         * Called when the given {@link AppSnackbar} is visible.
         *
         * @param snackbar The snackbar which is now visible.
         * @see AppSnackbar#show()
         */
        public void onShown(AppSnackbar snackbar) {
            // empty
        }
    }

    /**
     * @hide
     */
    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * Show the AppSnackbar indefinitely. This means that the AppSnackbar will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another AppSnackbar is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = -2;

    /**
     * Show the AppSnackbar for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = -1;

    /**
     * Show the AppSnackbar for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 0;

    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;

    private static final Handler sHandler;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((AppSnackbar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((AppSnackbar) message.obj).hideView(message.arg1);
                        return true;
                }
                return false;
            }
        });
    }

    private final ViewGroup mParent;
    private Context mContext;
    private SnackBarLayout mView;
    private int mDuration;
    private Callback mCallback;

    private AppSnackbar(ViewGroup parent) {
        mParent = parent;
        try {
            mContext = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            mView = (SnackBarLayout) inflater.inflate(R.layout.layout_snackbar, mParent, false);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

    }


    @NonNull
    public static AppSnackbar make(@NonNull View view, @NonNull CharSequence text,
                                   @Duration int duration) {
        AppSnackbar snackbar = new AppSnackbar(findSuitableParent(view));
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }


    @NonNull
    public static AppSnackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }

    /**
     * size should be around 200....
     */
    public AppSnackbar addIcon(int resource_id, int size) {
        final TextView tv = mView.getMessageView();

        tv.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) (mContext.getResources().getDrawable(resource_id))).getBitmap(), size, size, true)), null, null, null);

        return this;
    }

    /**
     * Set the action to be displayed in this {@link AppSnackbar}.
     *
     * @param resId    String resource to display
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public AppSnackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(mContext.getText(resId), listener);
    }

    /**
     * Set the action to be displayed in this {@link AppSnackbar}.
     *
     * @param text     FEEDS_TEXT to display
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public AppSnackbar setAction(CharSequence text, final View.OnClickListener listener) {
        final TextView tv = mView.getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(view -> {
                listener.onClick(view);
                // Now dismiss the AppSnackbar
                dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
            });
        }
        return this;
    }

    /**
     * Sets the text color of the action specified in
     */
    @NonNull
    public AppSnackbar setActionTextColor(ColorStateList colors) {
        final TextView tv = mView.getActionView();
        tv.setTextColor(colors);
        return this;
    }

    /**
     * Sets the text color of the action specified in
     */
    @NonNull
    public AppSnackbar setActionTextColor(@ColorInt int color) {
        final TextView tv = mView.getActionView();
        tv.setTextColor(color);
        return this;
    }

    /**
     * Update the text in this {@link AppSnackbar}.
     *
     * @param message The new text for the Toast.
     */
    @NonNull
    public AppSnackbar setText(@NonNull CharSequence message) {
        final TextView tv = mView.getMessageView();
        tv.setMaxLines(3);
        tv.setText(message);
        return this;
    }

    /**
     * Update the text in this {@link AppSnackbar}.
     *
     * @param resId The new text for the Toast.
     */
    @NonNull
    public AppSnackbar setText(@StringRes int resId) {
        return setText(mContext.getText(resId));
    }

    /**
     * Set how long to show the view for.
     *
     * @param duration either be one of the predefined lengths:
     *                 {@link #LENGTH_SHORT}, {@link #LENGTH_LONG}, or a custom duration
     *                 in milliseconds.
     */
    @NonNull
    public AppSnackbar setDuration(@Duration int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    @Duration
    public int getDuration() {
        return mDuration;
    }

    /**
     * Returns the {@link AppSnackbar}'s view.
     */
    @NonNull
    public View getView() {
        return mView;
    }

    /**
     * Show the {@link AppSnackbar}.
     */
    public void show() {
        SnackbarManager.getInstance().show(mDuration, mManagerCallback);
    }

    /**
     * Dismiss the {@link AppSnackbar}.
     */
    public void dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackbarManager.getInstance().dismiss(mManagerCallback, event);
    }

    /**
     * Set a callback to be called when this the visibility of this {@link AppSnackbar} changes.
     */
    @NonNull
    public AppSnackbar setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    /**
     * Return whether this {@link AppSnackbar} is currently being shown.
     */
    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(mManagerCallback);
    }

    /**
     * Returns whether this {@link AppSnackbar} is currently being shown, or is queued to be
     * shown next.
     */
    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(mManagerCallback);
    }

    private final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
        @Override
        public void show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, AppSnackbar.this));
        }

        @Override
        public void dismiss(int event) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, AppSnackbar.this));
        }
    };

    final void showView() {
        if (mView.getParent() == null) {
            final ViewGroup.LayoutParams lp = mView.getLayoutParams();

            if (lp instanceof CoordinatorLayout.LayoutParams) {
                // If our LayoutParams are from a CoordinatorLayout, we'll setup our Behavior

                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(View view) {
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE);
                    }

                    @Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case SwipeDismissBehavior.STATE_DRAGGING:
                            case SwipeDismissBehavior.STATE_SETTLING:
                                // If the view is being dragged or settling, cancel the timeout
                                SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                                break;
                            case SwipeDismissBehavior.STATE_IDLE:
                                // If the view has been released and is idle, restore the timeout
                                SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                                break;
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams) lp).setBehavior(behavior);
            }
            mParent.addView(mView);
        }

        mView.setOnAttachStateChangeListener(new SnackBarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {
                    // If we haven't already been dismissed then this event is coming from a
                    // non-user initiated action. Hence we need to make sure that we callback
                    // and keep our state up to date. We need to post the call since removeView()
                    // will call through to onDetachedFromWindow and thus overflow.
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });

        if (ViewCompat.isLaidOut(mView)) {
            // If the view is already laid out, animate it now
            animateViewIn();
        } else {
            // Otherwise, add one of our layout change listeners and animate it in when laid out
            mView.setOnLayoutChangeListener(new SnackBarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    mView.setOnLayoutChangeListener(null);
                }
            });
        }
    }

    private void animateViewIn() {

        ViewCompat.setTranslationX(mView, -mView.getWidth());
        ViewCompat.animate(mView)
                .translationX(0f)
                .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        mView.animateChildrenIn(ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                                ANIMATION_FADE_DURATION);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (mCallback != null) {
                            mCallback.onShown(AppSnackbar.this);
                        }
                        SnackbarManager.getInstance().onShown(mManagerCallback);
                    }
                }).start();

    }

    private void animateViewOut(final int event) {
        ViewCompat.animate(mView)
                .translationX(mView.getWidth())
                .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        mView.animateChildrenOut(0, ANIMATION_FADE_DURATION);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        onViewHidden(event);
                    }
                }).start();

    }

    final void hideView(int event) {
        if (mView.getVisibility() != View.VISIBLE || isBeingDragged()) {
            onViewHidden(event);
        } else {
            animateViewOut(event);
        }
    }

    private void onViewHidden(int event) {
        // First tell the SnackbarManager that it has been dismissed
        SnackbarManager.getInstance().onDismissed(mManagerCallback);
        // Now call the dismiss listener (if available)
        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }
        // Lastly, remove the view from the parent (if attached)
        final ViewParent parent = mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mView);
        }
    }

    /**
     * @return if the view is being being dragged or settled by {@link SwipeDismissBehavior}.
     */
    private boolean isBeingDragged() {
        final ViewGroup.LayoutParams lp = mView.getLayoutParams();

        if (lp instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.LayoutParams cllp = (CoordinatorLayout.LayoutParams) lp;
            final CoordinatorLayout.Behavior behavior = cllp.getBehavior();

            if (behavior instanceof SwipeDismissBehavior) {
                return ((SwipeDismissBehavior) behavior).getDragState()
                        != SwipeDismissBehavior.STATE_IDLE;
            }
        }
        return false;
    }

    final class Behavior extends SwipeDismissBehavior<SnackBarLayout> {
        @Override
        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackBarLayout;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackBarLayout child,
                                             MotionEvent event) {
            // We want to make sure that we disable any AppSnackbar timeouts if the user is
            // currently touching the AppSnackbar. We restore the timeout when complete
            if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                        break;
                }
            }

            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}
