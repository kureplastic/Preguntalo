package com.example.preguntalo.menu;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MenuActivityViewModel extends AndroidViewModel {

    private Context context;
    public MenuActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
}
