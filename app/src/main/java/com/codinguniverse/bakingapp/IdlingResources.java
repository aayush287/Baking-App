package com.codinguniverse.bakingapp;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class IdlingResources implements androidx.test.espresso.IdlingResource {
    @Nullable
    private volatile ResourceCallback callback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        String className = this.getClass().getName();
        return className;
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIsIdleNow(boolean isIdle){
        mIsIdleNow.set(isIdle);
        if (isIdle && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
