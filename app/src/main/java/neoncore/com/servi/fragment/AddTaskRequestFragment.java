package neoncore.com.servi.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import neoncore.com.servi.R;
import neoncore.com.servi.Utils.SharedViewmodel;
import neoncore.com.servi.Utils.SlideAnimationUtil;
import neoncore.com.servi.activities.Register;
import neoncore.com.servi.beans.ServiceCategory;
import neoncore.com.servi.beans.TaskRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskRequestFragment extends DialogFragment implements View.OnClickListener {
    private Button cancelBtn,postBtn,selectDate;
    private TextView categoryTv,dueDateTv;
    private EditText postTitle,postDescription;
    public static final String TAG = "AddTaskFrag";
    private SharedViewmodel viewmodel;
    private ServiceCategory category;
    private FirebaseFirestore db;
    private Date date;
    private String dueDate;
    private DueDatePicker dueDatePicker;
    private float distFromUser;
    private Location mLocation;


    public AddTaskRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);
        db = FirebaseFirestore.getInstance();
        dueDatePicker = new DueDatePicker();
        viewmodel.getDate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s != null){

                    dueDate = s;
                }
            }
        });
        viewmodel.getDateObj().observe(this, new Observer<Date>() {
            @Override
            public void onChanged(@Nullable Date date) {
                if(date!= null){

                    dueDateTv.setText(date.toString());


                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_task_request, container, false);

        SlideAnimationUtil.slideInFromRight(getContext(),v);
        selectDate = v.findViewById(R.id.select_date);
        dueDateTv = v.findViewById(R.id.due_date_text_view);



        cancelBtn = v.findViewById(R.id.post_task_cancel_btn);
                postBtn = v.findViewById(R.id.post_task_post_btn);
                categoryTv = v.findViewById(R.id.categoryEdt);
                postTitle = v.findViewById(R.id.post_task_title);
                postDescription = v.findViewById(R.id.post_task_description);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                postBtn.setOnClickListener(this);

        viewmodel.getSelected().observe(getActivity(), new Observer<ServiceCategory>() {
            @Override
            public void onChanged(@Nullable ServiceCategory serviceCategory) {

                if(serviceCategory != null)
                    category = serviceCategory;

                Log.d("CATEGORY OBJ", category.getCategory_description());
                Log.d("CATEGORY OBJ", category.getCategory_name());

            }
        });
        selectDate.setOnClickListener(this);


        return v;
    }

    public void addTask(){

        //get text and upload to fb
        String taskTitle, taskDesc;
        taskTitle = postTitle.getText().toString();
        taskDesc = postDescription.getText().toString();
        //if textView is not empty




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(getActivity(), "You have to Sign In", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(TextUtils.isEmpty(taskTitle) && TextUtils.isEmpty(taskDesc))){
            Context context = getActivity();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String lat = sharedPref.getString(getString(R.string.latitude_value),"");
           // long lat = sharedPref.getLong(getString(R.string.latitude_value),0);
            String longi = sharedPref.getString(getString(R.string.longitude_value),"");
            Log.d(TAG, lat + " ");
            Log.d(TAG, longi  + " ");



            Toast.makeText(getActivity(), lat + "latshared:longshared" + longi, Toast.LENGTH_SHORT).show();



            final TaskRequest request = new TaskRequest(category.getCategory_id(),taskTitle,user.getUid(),
                    UUID.randomUUID().toString(),taskDesc,false,dueDate + "days Left ",new GeoPoint(Double.valueOf(lat),Double.valueOf(longi)));
            //add the request to the tasks db
            final float[] distance = new float[2];
            if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid())){


                // distFromUser = location.distanceTo(location);
                Location.distanceBetween(mLocation.getLatitude(),mLocation.getLongitude(),
                        request.getGeoPoint().getLatitude(),request.getGeoPoint().getLongitude(),distance);
                //Log.d("DISTFROMUSERINSI",distFromUser + "");
                Log.d("DISTFROMUSERINADDTAsK",distance[0] + "");
                distFromUser = distance[0];

                //textViewDist.setText(distFromUser + " from user");

            }
            viewmodel.selectNuDate(null);
            dueDateTv.setText("");
            db.collection(getString(R.string.TASK_DB_NAME)).add(request)
                   .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
                           Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                           clearEdittext();
                           Map<String, Object> userDistanceFrom =
                                   new HashMap<>();
                           userDistanceFrom.put("DistanceFrom",distFromUser);

                           db.collection("Tasks").document(request.getTaskMessageID()).update(userDistanceFrom).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                   Toast.makeText(getActivity(), "Successfully Uploaded the location data", Toast.LENGTH_SHORT).show();

                               }
                           });

                       }
                   })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });



        }

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.select_date){


            dueDatePicker.show(getFragmentManager(),DueDatePicker.DUE_PICKER);

        }else if(v.getId() == R.id.post_task_post_btn) {

            if(!(dueDate == null)){
                addTask();
            }else {
                Toast.makeText(getActivity(), "Set the due Date", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void clearEdittext(){
        postTitle.setText("");
        postDescription.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        SmartLocation.with(getActivity()).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                mLocation = location;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        SmartLocation.with(getActivity()).location().stop();
    }
}
