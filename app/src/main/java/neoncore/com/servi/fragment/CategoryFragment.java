package neoncore.com.servi.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import neoncore.com.servi.MainActivity;
import neoncore.com.servi.R;
import neoncore.com.servi.Utils.SharedViewmodel;
import neoncore.com.servi.Utils.SlideAnimationUtil;
import neoncore.com.servi.beans.ServiceCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private FirestoreRecyclerAdapter adapter;
    private SharedViewmodel modelView;
    private TextView textView;
    private RecyclerView rv;
    


    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelView = ViewModelProviders.of(getActivity()).get(SharedViewmodel.class);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_cateegory, container, false);

        //recyclerView with categories
         rv = v.findViewById(R.id.rv_cart);
        textView  = v.findViewById(R.id.promtp_txtview);
        SlideAnimationUtil.slideInFromLeft(getContext(),v);

        //use firebaseUI to display the list of categories on the app.
//        if(isUserLoggedin()){

        Query query = FirebaseFirestore.getInstance()
                .collection("Categories")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ServiceCategory> options = new FirestoreRecyclerOptions.Builder<ServiceCategory>()
                .setQuery(query, ServiceCategory.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ServiceCategory, CategoryHolder>(options){
            int x ;
            @Override
            protected void onBindViewHolder(@NonNull CategoryHolder holder, int position, @NonNull final ServiceCategory model) {

                holder.categoryDesc.setText(model.getCategory_name());
                holder.categoryName.setText(model.getCategory_description());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        modelView.select(model);
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        //using add instead of replace;
                        transaction.replace(R.id.frag_container, new CategoryDetailFragment());
                        transaction.addToBackStack(null);
                        transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
                      //  transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_to_right);
                        transaction.commit();

                    }
                });




            }

            @Override
            public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_card_it, parent, false);
                return new CategoryHolder(view);
            }


            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Toast.makeText(getActivity(), "Error Loading data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if(getItemCount() == 0){
                    //show no item layout or loading bar
                    rv.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                }else {
                    //hide loading bar
                    textView.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);


                }




            }

        };

        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
//        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));




        // }




        return v;
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryName,categoryDesc;
        View mView;
        public CategoryHolder(View itemView) {
            super(itemView);
            mView = itemView;


            categoryName = itemView.findViewById(R.id.cat_dd);
            categoryDesc= itemView.findViewById(R.id.cat_nn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(getActivity(), "Clicked Number : " + v.getId(), Toast.LENGTH_LONG).show();
            //start the new fragment here
            //use view model to send data to the other fragment
//            model.select();



        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //stop adapter
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void setEnterTransition(@Nullable Object transition) {
        super.setEnterTransition(transition);
        SlideAnimationUtil.slideInFromLeft(getContext(),getView());
    }

    @Override
    public void setExitTransition(@Nullable Object transition) {
        super.setExitTransition(transition);
        SlideAnimationUtil.slideOutToLeft(getContext(),getView());
    }
}
