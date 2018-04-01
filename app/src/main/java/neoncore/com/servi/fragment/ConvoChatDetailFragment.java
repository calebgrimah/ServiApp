package neoncore.com.servi.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

import neoncore.com.servi.R;
import neoncore.com.servi.Utils.SharedViewmodel;
import neoncore.com.servi.beans.Interaction;
import neoncore.com.servi.beans.Message;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConvoChatDetailFragment extends Fragment {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final String TAG = "ConvoChatClass";
    private SharedViewmodel viewmodel;
    private Interaction mInteraction;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter recyclerAdapter;
    private FirebaseFirestore db;
    private FirebaseUser user;


    public ConvoChatDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_convo_chat_detail, container, false);
//        mInteraction.getAuthor();


        viewmodel.getSelectedRequest().observe(this, new Observer<Interaction>() {
            @Override
            public void onChanged(@Nullable Interaction interaction) {
                if (interaction != null) {
                    mInteraction = interaction;
                    final boolean messageSent = false;

                    final Message message = new Message(mInteraction.getSenderID(),"Message",mInteraction.getAuthor());
                    db.collection("Users").document(user.getUid())
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(final DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                                    if(documentSnapshot.exists()){
                                        documentSnapshot.getDocumentReference(user.getUid()).collection("Messages")
                                                .add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        Toast.makeText(getActivity(), "MessageSent", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }
                                }
                            });
                    db.collection("Users").document(mInteraction.getSenderID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                            if(documentSnapshot.exists()){
                                //receiver exixts
                                documentSnapshot.getDocumentReference(mInteraction.getSenderID()).collection("Messages")
                                        .add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(getActivity(), "Receiver has gotten it", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }
                    });
                }
            }
        });

        //observve my own message end only
        CollectionReference messagesCollection = db.collection("Users").document(user.getUid()).collection("Messages");
        Query query = messagesCollection.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Message> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query,Message.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<Message, RecyclerView.ViewHolder>(firestoreRecyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Message model) {
                switch (holder.getItemViewType()) {
                    case VIEW_TYPE_MESSAGE_SENT:
                        ((MeMessageHolder) holder).bind(model);
                        break;
                    case VIEW_TYPE_MESSAGE_RECEIVED:
                        ((OtherMessageHolder) holder).bind(model);


                }

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view;
                if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_chat_me, parent, false);
                    return new MeMessageHolder(view);
                } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item_other, parent, false);
                    return new OtherMessageHolder(view);
                }

                return null;            }

            @Override
            public int getItemViewType(int position) {

                Message message = getItem(position);
                //smooth scroll to current message position.
                if(user.getUid().equals(message.getAuthor().getUserID())){
                    return VIEW_TYPE_MESSAGE_SENT;
                }
                else{ return VIEW_TYPE_MESSAGE_RECEIVED;}
                //return super.getItemViewType(position);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.d(TAG, e.getLocalizedMessage());
            }


        };

        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true));


        return v;
    }
//
    public class MeMessageHolder extends RecyclerView.ViewHolder{
        TextView messageBody, messageTime;
        SimpleDateFormat simpleDateFormat ;

        public MeMessageHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body_other);
            messageTime = itemView.findViewById(R.id.text_message_time);
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        void bind (Message message){

            messageBody.setText(message.getText());
            messageTime.setText(simpleDateFormat.format(message.getTimestamp()));

        }
    }

    public class OtherMessageHolder extends RecyclerView.ViewHolder {
        TextView messageBody, messageTime, messageName;
        ImageView otherMessageImage;
        SimpleDateFormat simpleDateFormat;

        public OtherMessageHolder(View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.text_message_body_other);
            messageTime = itemView.findViewById(R.id.text_message_time_other);
            messageName = itemView.findViewById(R.id.text_message_name);
            otherMessageImage = itemView.findViewById(R.id.image_message_profile);
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        }

        void bind(Message message) {
            if (message != null) {

                messageName.setText(message.getAuthor().getName());
                messageBody.setText(message.getText());
                messageTime.setText(simpleDateFormat.format(message.getTimestamp()));
//                Glide.with(getApplicationContext())
//                        .load(message.getAuthor().getPhotoUrl())
//                        .into(otherMessageImage);

            }

        }


//
//}
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
    }
}