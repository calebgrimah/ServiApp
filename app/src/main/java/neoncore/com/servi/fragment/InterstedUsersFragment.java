package neoncore.com.servi.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import neoncore.com.servi.R;
import neoncore.com.servi.Utils.SharedViewmodel;
import neoncore.com.servi.beans.Interaction;
import neoncore.com.servi.beans.TaskRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterstedUsersFragment extends Fragment {
    //listen for changes in this layout for new users
    private FirebaseUser user;
    private RecyclerView convoRecycler;
    private FirestoreRecyclerAdapter convoAdapter;
    private SharedViewmodel sharedViewmodel;

    public InterstedUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        sharedViewmodel = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_intersted_users, container, false);
        getUsers();

        return v;
    }

    public void getUsers(){

        //query the interaction collection to see the peoplr that have messaged u
        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getUid())
                .collection("Interactions")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Interaction> options = new FirestoreRecyclerOptions.Builder<Interaction>()
                .setQuery(query, Interaction.class)
                .build();
        convoAdapter = new FirestoreRecyclerAdapter <Interaction,ConvoViewholder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ConvoViewholder holder, int position, @NonNull final Interaction model) {

                holder.bind(model);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedViewmodel.select(model);
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        //using add instead of replace;
                        transaction.replace(R.id.convo_frag_container, new ConvoChatDetailFragment());
                        transaction.addToBackStack(null);
                        transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                        //  transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_to_right);
                        transaction.commit();

                    }
                });

            }

            @Override
            public ConvoViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.conversations_user_item, parent, false);
                return new ConvoViewholder(view);
            }
        };



    }

    public class ConvoViewholder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView usersName;
        View mView;
        public ConvoViewholder(View itemView) {
            super(itemView);
            mView = itemView;
            userImage = itemView.findViewById(R.id.interested_users_image);
            usersName = itemView.findViewById(R.id.interested_users_name);

        }

        void bind( Interaction interaction){

            usersName.setText(interaction.getAuthor().getName());
            //use glide to show the image

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        convoAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(convoAdapter != null){
            convoAdapter.stopListening();
        }
    }
}















