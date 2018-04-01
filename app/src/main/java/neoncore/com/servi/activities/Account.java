package neoncore.com.servi.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import neoncore.com.servi.R;
import neoncore.com.servi.beans.SelectedImage;
import pub.devrel.easypermissions.EasyPermissions;

public class Account extends BaseActivity {
    private static final int READ_REQUEST_CODE = 1337;
    private List<SelectedImage> filenames;
    private List<String> donList;

//    FirebaseStorage storage;
//    StorageReference storageReference;

    private static final String TAG = Account.class.getSimpleName();




    private FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);




        // Kick off the process of building the LocationCallback, LocationRequest, and

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFileSearch();
            }
        });


    }




    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_account;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                // Author chose the "Save" action, mark the current item
                // as a favorite...
                //uploadImage();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void performFileSearch() {



            // BEGIN_INCLUDE (use_open_document_intent)
            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Filter to only show results that can be "opened", such as a file (as opposed to a list
            // of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers, it would be
            // "*/*".
            intent.setType("image/*");
            // String[] extrMIME = {"image/*","video/*"};
            // intent.putExtra(Intent.EXTRA_MIME_TYPES,extrMIME);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(intent, READ_REQUEST_CODE);
            // END_INCLUDE (use_open_document_intent)

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent resultData) {
        // Log.i(TAG, "Received an \"Activity Result\"");
        // BEGIN_INCLUDE (parse_open_document_response)
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"


//            if (resultData != null) {
//                uri = resultData.getData();
//                Log.i(TAG, "Uri: " + uri.toString());
//              //  Toast.makeText(this, "Get Upload File" + uri , Toast.LENGTH_LONG).show();
//                uriArrayList.add(uri);
//
//                setImagetoImageView(uri);
//
//
//            }
            if(resultData.getClipData() != null){
                Toast.makeText(this, "Gotten Files Success", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < resultData.getClipData().getItemCount(); i++){
                    Uri fileUri = resultData.getClipData().getItemAt(i).getUri();
//                    SelectedImage image = getUrisMetadata(fileUri);
//                    filenames.add(image);
                    //add images to firebase db


                }
                //we have selected multiple images
                //i have list of uri's here



            }else if(resultData.getData() != null){
                //user has selected jus a single image
                //just one uri
                // uploadSingleImage(resultData.getData(),getUrisMetadata(resultData.getData()));

            }
            // END_INCLUDE (parse_open_document_response)
        }
    }

    public void dumpImageMetaData(Uri uri) {
        // BEGIN_INCLUDE (dump_metadata)

        // The query, since it only applies to a single document, will only return one row.
        // no need to filter, sort, or select fields, since we want all fields for one
        // document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is provider-specific, and
                // might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an int can't be
                // null in java, the behavior is implementation-specific, which is just a fancy
                // term for "unpredictable".  So as a rule, check if it's null before assigning
                // to an int.  This will happen often:  The storage API allows for remote
                // files, whose size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString will do the
                    // conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // END_INCLUDE (dump_metadata)
    }

    public SelectedImage getUrisMetadata(Uri uri) {
        // BEGIN_INCLUDE (dump_metadata)

        // The query, since it only applies to a single document, will only return one row.
        // no need to filter, sort, or select fields, since we want all fields for one
        // document.
        SelectedImage r = new SelectedImage();
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is provider-specific, and
                // might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                r.setFileName(displayName);
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an int can't be
                // null in java, the behavior is implementation-specific, which is just a fancy
                // term for "unpredictable".  So as a rule, check if it's null before assigning
                // to an int.  This will happen often:  The storage API allows for remote
                // files, whose size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString will do the
                    // conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                Log.i(TAG, "Size: " + size);
                r.setSize(size);
                r.setUri(uri.toString());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // END_INCLUDE (dump_metadata)
        return  r;
    }









}
