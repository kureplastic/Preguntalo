package com.example.preguntalo.login;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistroViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> mutableRegistroError;
    private MutableLiveData<String> mutablePasswordError;
    public RegistroViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableRegistroError = new MutableLiveData<>();
        mutablePasswordError = new MutableLiveData<>();
    }
    // TODO: Implement the ViewModel

    public MutableLiveData<String> getMutableRegistroError() {
        return mutableRegistroError;
    }
    public MutableLiveData<String> getMutablePasswordError() {
        return mutablePasswordError;
    }


}