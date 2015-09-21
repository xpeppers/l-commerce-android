package com.xpeppers.servicelib.utils;

/**
 * @author Emilio Frusciante - FEj (efrusciante AT wish-op DOT com)
 * @since 07/04/15
 */
public interface CallBack<T> {
    public void onSuccess(T result);
    public void onFailure(Throwable caught);
}

