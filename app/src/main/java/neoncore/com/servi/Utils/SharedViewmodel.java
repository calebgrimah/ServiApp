package neoncore.com.servi.Utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;

import neoncore.com.servi.beans.DateObject;
import neoncore.com.servi.beans.Interaction;
import neoncore.com.servi.beans.ServiceCategory;
import neoncore.com.servi.beans.TaskRequest;

/**
 * Created by Musa on 3/14/2018.
 */

public class SharedViewmodel extends ViewModel {

    private final MutableLiveData<ServiceCategory> selected = new MutableLiveData<ServiceCategory>();
    private final MutableLiveData<Interaction> taskReq = new MutableLiveData<>();
    private final MutableLiveData<String> dateStuff = new MutableLiveData<>();
    private final MutableLiveData<Date> dateObj = new MutableLiveData<>();





    public void select(ServiceCategory item) {
        selected.setValue(item);
    }

    public LiveData<ServiceCategory> getSelected() {
        return selected;
    }

    public void select(Interaction request){
        taskReq.setValue(request);
    }
    public void selectNuDate(Date nuDate){
        dateObj.setValue(nuDate);
    }


    public void setDate(String date){
        dateStuff.setValue(date);
    }
    public LiveData<String> getDate(){return dateStuff;}
    public LiveData<Date> getDateObj(){return dateObj;}

    public LiveData<Interaction> getSelectedRequest(){
        return taskReq;
    }
}
