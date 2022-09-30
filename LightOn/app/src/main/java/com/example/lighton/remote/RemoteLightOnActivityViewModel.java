package com.example.lighton.remote;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemoteLightOnActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> state = new MutableLiveData<>();

    public void init(){
        state.postValue(false);
    }

    public MutableLiveData<Boolean> getState(){
        return state;
    }

    public void set(boolean b){
        state.postValue(b);
    }

}
