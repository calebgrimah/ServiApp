package neoncore.com.servi.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import neoncore.com.servi.R;
import neoncore.com.servi.fragment.InterstedUsersFragment;

public class Conversations extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        //first frag to show list of users and then can be clicked to show other frag

        if(savedInstanceState != null){
            return;
        }
        //add this frag to the layout.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.convo_frag_container,new InterstedUsersFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_convo;
    }
}
