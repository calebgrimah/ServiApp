package neoncore.com.servi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import neoncore.com.servi.R;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_settings;
    }
}
