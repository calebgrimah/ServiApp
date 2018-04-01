package neoncore.com.servi.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Date;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import neoncore.com.servi.R;
import neoncore.com.servi.Utils.SharedViewmodel;
import neoncore.com.servi.Utils.SlideAnimationUtil;
import neoncore.com.servi.beans.Author;
import neoncore.com.servi.beans.Interaction;
import neoncore.com.servi.beans.ServiceCategory;
import neoncore.com.servi.beans.TaskRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetailFragment extends Fragment {


    //contain list of requests made by users also

    private TextView desc,name;
    private SharedViewmodel sharedViewmodel;
    private FloatingActionButton floatingActionButton;
    private AddTaskRequestFragment addTaskRequestFragment;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter recyclerAdapter;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Float distFromUser;
    private Location mLocation;
    private Date mDate;


    public CategoryDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_category_detail, container, false);
        SlideAnimationUtil.slideInFromRight(getContext(),v);
        recyclerView = v.findViewById(R.id.category_detail_rv);
        floatingActionButton = v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTaskRequestFragment.show(getFragmentManager(),AddTaskRequestFragment.TAG);


            }
        });
        sharedViewmodel.getSelected().observe(getActivity(), new Observer<ServiceCategory>() {
            @Override
            public void onChanged(@Nullable ServiceCategory serviceCategory) {
                if(serviceCategory != null){

                    recViewTinz(serviceCategory);

                }
            }
        });
        sharedViewmodel.getDateObj().observe(getActivity(), new Observer<Date>() {
            @Override
            public void onChanged(@Nullable Date date) {
               if(date != null)
                mDate = date;

            }
        });


//        desc = v.findViewById(R.id.cart_desc);
//        name = v.findViewById(R.id.cart_name);
//        sharedViewmodel.getSelected().observe(getActivity(), new Observer<ServiceCategory>() {
//            @Override
//            public void onChanged(@Nullable ServiceCategory serviceCategory) {
//
//
//                if(serviceCategory != null){
//
//                    desc.setText(serviceCategory.getCategory_description());
//                    name.setText(serviceCategory.getCategory_name());
//
//                }
//
//
//            }
//        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SlideAnimationUtil.slideInFromLeft(getContext(),getView());


        sharedViewmodel = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);
        addTaskRequestFragment = new AddTaskRequestFragment();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
        SmartLocation.with(getActivity()).location()
                .oneFix().config(LocationParams.BEST_EFFORT)
                .start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                mLocation  = location;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(R.id.action_settings == item.getItemId() ){

            //open a dialog fragment

        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void setEnterTransition(@Nullable Object transition) {
//        super.setEnterTransition(transition);
//    }
//
//    @Override
//    public void setExitTransition(@Nullable Object transition) {
//        super.setExitTransition(transition);
//        SlideAnimationUtil.slideOutToRight(getContext(),getView());
//    }


    public void  recViewTinz(ServiceCategory category){
        Query query = FirebaseFirestore.getInstance()
                .collection("Tasks")
                .whereEqualTo("categoryID",category.getCategory_id())
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TaskRequest> options = new FirestoreRecyclerOptions.Builder<TaskRequest>()
                .setQuery(query, TaskRequest.class)
                .build();

        recyclerAdapter = new FirestoreRecyclerAdapter<TaskRequest,TaskRequestViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull TaskRequestViewHolder holder, int position, @NonNull TaskRequest model) {

                holder.bind(model);


            }

            @Override
            public TaskRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_detail_item, parent, false);
                return new TaskRequestViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount() == 0){
                    Toast.makeText(getActivity(), "No items Yet", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Toast.makeText(getActivity(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));



    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(recyclerAdapter != null){
            recyclerAdapter.stopListening();
        }
        SmartLocation.with(getActivity()).location().stop();
    }

    public class TaskRequestViewHolder extends RecyclerView.ViewHolder{

        TextView serviceTxt,serviceDesc,taskCompleteTv,taskDate,textViewDist,dateAddedTv;
        ImageButton indicateInterest;

        public TaskRequestViewHolder(View itemView) {
            super(itemView);
            serviceTxt = itemView.findViewById(R.id.task_request_title_text);
            serviceDesc = itemView.findViewById(R.id.task_request_desc);
            taskCompleteTv = itemView.findViewById(R.id.taskcomplete_indicator);
            indicateInterest = itemView.findViewById(R.id.indicate_interest_btn);
            taskDate = itemView.findViewById(R.id.due_date_text_view_ca);
            textViewDist = itemView.findViewById(R.id.textView_dist_frm_user);
            dateAddedTv = itemView.findViewById(R.id.date_added_txt_view);
        }


        void bind(final TaskRequest request){
            serviceTxt.setText(request.getTaskMessage());
            serviceDesc.setText(request.getTaskDescription());
            if(request.isTaskComplete()){
                taskCompleteTv.setText("Complete");
                taskCompleteTv.setBackgroundColor(Color.GREEN);

            }
            if (mDate != null)
            dateAddedTv.setText(mDate.toString());
            taskDate.setText(request.getDueDate());

        final float[] distance = new float[2];


            if((request.getGeoPoint().getLatitude() != 0) && (request.getGeoPoint().getLongitude() != 0)){
               if(mLocation != null){

                   // distFromUser = location.distanceTo(location);
                   Location.distanceBetween(mLocation.getLatitude(),mLocation.getLongitude(),
                           request.getGeoPoint().getLatitude(),request.getGeoPoint().getLongitude(),distance);
                   //Log.d("DISTFROMUSERINSI",distFromUser + "");
                   Log.d("DISTFROMUSERINSIwww",distance[0] + "");
                   distFromUser = distance[0];

                   textViewDist.setText(distFromUser + " from user");
               }


            }else{
                Toast.makeText(getActivity(), "No Location Info", Toast.LENGTH_SHORT).show();
                textViewDist.setText("NO location Info");
            }


            indicateInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add id to the current logged in  users collection of users
                    if(!(user.getUid().equals(request.getSenderID()))){

                        Toast.makeText(getActivity(), "Users arent Equal, add to the logged in user coll", Toast.LENGTH_SHORT).show();
                        //add the user user id to his users collection;
                        //get ref to users coll
                        db.collection("Users").document(request.getSenderID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                if(documentSnapshot.exists()){

                                    //user exists add to the users interactions collection
                                    final DocumentReference reference = documentSnapshot.getReference();
                                    //add a nu Author obj
                                    Author author = new Author(user.getDisplayName(),user.getUid(),user.getPhotoUrl().toString(),user.getEmail(),user.getPhoneNumber(),null,null);
                                    final Interaction interaction = new Interaction(request.getTaskMessageID(),request.getSenderID(),author);
                                    reference.collection("Interactions").document(interaction.getTaskID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                            if(documentSnapshot.exists()){
                                                indicateInterest.setImageResource(R.drawable.ic_chat_pink_24dp);
                                                Toast.makeText(getActivity(), "U have messaged user already, WAAAIT", Toast.LENGTH_SHORT).show();

                                            }else {
                                                reference.collection("Interactions").add(interaction).
                                                        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getActivity(), "Successfully Messaged Uploader", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });




                                }
                            }
                        });



                    }else {
                        Toast.makeText(getActivity(), "They are the same users, stop tappin", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }



}