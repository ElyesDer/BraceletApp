package com.wildchild.locationpickermodule.locationpickermodule.DBSynchronisation.Database.Interfaces;

public interface CompletionHandler<T> {
    void onSuccess(T response);
    void onFailure(Throwable e);
}
