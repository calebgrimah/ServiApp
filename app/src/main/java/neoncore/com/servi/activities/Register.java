package neoncore.com.servi.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import neoncore.com.servi.MainActivity;
import neoncore.com.servi.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Register extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {

    private static final String[] LOCATION_AND_CAMERA =
            {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE};
    private static final int RC_LOCATION_AND_CAMERA_PERM = 124;

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = Register.class.getSimpleName();
    private Button button;


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // not signed in
            //showSnackbar(R.string.sign_in_message);
            Toast.makeText(this, getString(R.string.sign_in_message), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // FirebaseApp.initializeApp(getApplicationContext());
       // FirebaseApp.getInstance();
        button = findViewById(R.id.reg_btn);
        button.setEnabled(false);
        locationAndCameraTask();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()
                                ))
                                .build(),
                        RC_SIGN_IN);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT)
                    .show();
            button.setEnabled(true);
        }
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Intent i = new Intent(this,MainActivity.class);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                //start mainActivity and end this one
                startActivity(i);
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
//
//    private static Intent createIntent(Register registerActivity, IdpResponse response) {
//
//        Intent i = new Intent(registerActivity,MainActivity.class);
//        i.putExtra("IDP_TOKEN_EXTRA", response.getIdpToken());
//        return i;
//    }
    public void showSnackbar(int res){

        Snackbar.make(findViewById(R.id.reg_activity),getString(res),Snackbar.LENGTH_SHORT).show();

    }

    //perm
    @AfterPermissionGranted(RC_LOCATION_AND_CAMERA_PERM)
    public void locationAndCameraTask() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            Toast.makeText(this, "TODO: Location and Camera things permission granted", Toast.LENGTH_LONG).show();
            SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {
                @Override
                public void onLocationUpdated(Location location) {
                    Toast.makeText(Register.this, "Gotten Location", Toast.LENGTH_LONG).show();
                    Toast.makeText(Register.this, "Gotten " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();


                    SharedPreferences sharedPref = Register.this.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    boolean issaved = sharedPref.getBoolean(getString(R.string.boolean_check),false);
                    if (!issaved){

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.latitude_value),String.valueOf(location.getLatitude()));
                        editor.putString(getString(R.string.longitude_value),String.valueOf(location.getLongitude()));
//                        editor.putLong(getString(R.string.longitude_value),Double.doubleToLongBits(location.getLongitude()));
                        editor.putBoolean(getString(R.string.boolean_check),true);
                        editor.apply();
                    }else
                        Toast.makeText(Register.this, "Already Saved", Toast.LENGTH_SHORT).show();


                }
            });
            button.setEnabled(true);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "This app requires Camera and Location Permission",
                    RC_LOCATION_AND_CAMERA_PERM,
                    LOCATION_AND_CAMERA);
        }
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CAMERA);

    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        button.setEnabled(true);

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);

    }



    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);


    }

    @Override
    protected void onStop() {
        super.onStop();
        SmartLocation.with(this).location().stop();
    }

}
