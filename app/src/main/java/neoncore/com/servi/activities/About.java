package neoncore.com.servi.activities;

import android.os.Bundle;
import android.widget.Toolbar;

import neoncore.com.servi.R;

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_about;
    }

}
