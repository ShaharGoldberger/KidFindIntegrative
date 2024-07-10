package com.example.choresandshop.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choresandshop.Adapters.NotificationsAdapter;
import com.example.choresandshop.Model.Notification;

import java.util.List;

public class NotificationsViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");

    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setTextNone() {
        this.mText.setValue("You have 0 notifications");
    }


    public void clearText() {
        this.mText.setValue("");

    }
}