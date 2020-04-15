package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces;

import androidx.annotation.Nullable;

public interface CompletionHandler<T> {
    void onSuccess(T response ); //, @Nullable String message
    void onFailure(Throwable e);
}
