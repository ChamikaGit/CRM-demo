package com.fidenz.dev.crmdemo.Actvities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactAdd extends AppCompatActivity implements View.OnClickListener {


    EditText contactId, contactname, contactmobile, contacttelephone, contactemail, contactdescription, contactaddress;
    Button btnsave;

    DatabaseReference databaseReferencecontact;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        // contactId = findViewById(R.id.idcontact);
        contactname = findViewById(R.id.idname);
        contactmobile = findViewById(R.id.idmobileno);
        contacttelephone = findViewById(R.id.idtelephone);
        contactemail = findViewById(R.id.idemail);
        contactdescription = findViewById(R.id.iddescription);
        contactaddress = findViewById(R.id.idaddress);

        mauth = FirebaseAuth.getInstance();

        databaseReferencecontact = FirebaseDatabase.getInstance().getReference("Contact");

        findViewById(R.id.btnsave).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnsave:

                addcontact();

                break;


        }
    }

    private void addcontact() {


        //String ctContactID= contactId.getText().toString().trim();
        String ctcontactname = contactname.getText().toString().trim();
        String ctcontactmobile = contactmobile.getText().toString();
        String ctcontacttelephone = contacttelephone.getText().toString();
        String ctcontactemail = contactemail.getText().toString().trim();
        String ctcctontactdescription = contactdescription.getText().toString().trim();
        String ctcontactaddress = contactaddress.getText().toString().trim();


        if (TextUtils.isEmpty(ctcontactname)) {
            contactname.setError("Contact name Required !");
            contactname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ctcontactmobile)) {
            contactmobile.setError("MobileNo Required !");
            contactmobile.requestFocus();
            return;
        }

        if (ctcontactmobile.length() <10 || ctcontactmobile.length()>10) {
            contactmobile.setError("Invalid MobileNo !");
            contactmobile.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(ctcontacttelephone)) {
            contactmobile.setError("TelephoneNo Required!");
            contactmobile.requestFocus();
            return;
        }

        if (ctcontacttelephone.length() <10 || ctcontacttelephone.length()>10) {
            contactmobile.setError("Invalid TelephoneNo !");
            contactmobile.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(ctcontactemail)) {
            contactemail.setError("Email Required !");
            contactemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(ctcontactemail).matches()) {
            contactemail.setError("Please enter a valid email");
            contactemail.requestFocus();
            return;
        }

        String userid = mauth.getCurrentUser().getUid();
        String id = databaseReferencecontact.push().getKey();


//        if (!TextUtils.isEmpty(ctcontactname) && !TextUtils.isEmpty(ctcontactmobile) && !TextUtils.isEmpty(ctcontacttelephone)
//
//                && !TextUtils.isEmpty(ctcctontactdescription) && !TextUtils.isEmpty(ctcontactaddress)  ){


        Contact contact = new Contact(id, userid, ctcontactname, ctcontactmobile, ctcontacttelephone, ctcontactemail, ctcctontactdescription, ctcontactaddress);
        databaseReferencecontact.child(userid).child(id).setValue(contact);

        Intent intent = new Intent(ContactAdd.this, ContactList.class);
        startActivity(intent);
        finish();

        Toast.makeText(getApplicationContext(), ctcontactname+" Add Successfully", Toast.LENGTH_LONG).show();


//        }else {
//
//
//            Toast.makeText(getApplicationContext(),"Some fields are empty",Toast.LENGTH_LONG).show();
//        }


    }
}
