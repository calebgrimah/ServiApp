package neoncore.com.servi.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import neoncore.com.servi.R;
import neoncore.com.servi.beans.ServiceCategory;

public class AddCategory extends BaseActivity {

    private TextView catName, catDesc;
    private Button btnSubmit;
    private FirebaseFirestore db;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        //jus a handy activity that would add a category for me


        catName = findViewById(R.id.category_edit);
        catDesc = findViewById(R.id.category_desc_edit_add);
        btnSubmit =findViewById(R.id.category_submit_btn);


        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                {

                    //upload the stuff to the DB
                    if(!(TextUtils.isEmpty(catName.getText()) && TextUtils.isEmpty(catDesc.getText()))){
                        ServiceCategory sv = new ServiceCategory(UUID.randomUUID().toString(),catName.getText().toString(),catDesc.getText().toString());
                        CollectionReference colRef = db.collection(getString(R.string.CATEGORY_NAME_CONSTANT));
                        colRef.add(sv)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(AddCategory.this, "Upload Success", Toast.LENGTH_SHORT).show();
                                        catName.setText("");
                                        catDesc.setText("");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        Toast.makeText(AddCategory.this, "Edittext is Empty", Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Toast.makeText(AddCategory.this, "User Not Logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_add_category;
    }
}
