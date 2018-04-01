package neoncore.com.servi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import neoncore.com.servi.activities.BaseActivity;
import neoncore.com.servi.activities.Register;
import neoncore.com.servi.fragment.CategoryFragment;

public class MainActivity extends BaseActivity{
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        *
        * check this to retain a fragments state across configuration*/
        // Begin the transaction
        if (savedInstanceState != null) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
           ft.replace(R.id.frag_container, new CategoryFragment());
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
           ft.commit();





    }

     @Override
     protected void onResume() {
         super.onResume();
         if (FirebaseAuth.getInstance().getCurrentUser() != null){
             //great user has signed up
             Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
         }else{
             Intent i = new Intent(this, Register.class);
             startActivity(i);
             finish();
         }

     }
             @Override
             protected int getNavigationDrawerID() {
                 return R.id.nav_home;
             }

             public boolean isUserLoggedin(){

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 return user != null;
             }






}


