package com.fidenz.dev.crmdemo.Actvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUpdateActvity extends AppCompatActivity  implements View.OnClickListener{


    String name,mobile,telphone,email,description,address,contactid;

    EditText etname,etmobileno,ettelphoneno,etemail,etdesciption,etaddress;

    DatabaseReference databaseReferencecntactupdate;
    DatabaseReference databaseReferencedeletecontacts;
    DatabaseReference  databaseReferencedeletenotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_update_actvity);


        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        telphone = getIntent().getStringExtra("telphoneno");
        email = getIntent().getStringExtra("email");
        description = getIntent().getStringExtra("Description");
        address = getIntent().getStringExtra("address");
        contactid =getIntent().getStringExtra("contactid");


        etname= findViewById(R.id.idnameupdate);
        etmobileno=findViewById(R.id.idmobilenoupdate);
        ettelphoneno=findViewById(R.id.idtelephoneupdate);
        etemail=findViewById(R.id.idemailupdate);
        etdesciption=findViewById(R.id.iddescriptionupdate);
        etaddress=findViewById(R.id.idaddressupdate);

        etname.setText(name);
        etmobileno.setText(mobile);
        ettelphoneno.setText(telphone);
        etemail.setText(email);
        etdesciption.setText(description);
        etaddress.setText(address);

        findViewById(R.id.btnupdate).setOnClickListener(this);
        findViewById(R.id.btndelete).setOnClickListener(this);












    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnupdate:
                updatecontact();
                break;

            case R.id.btndelete:

                deletecontact();
                break;


        }
    }

    private void deletecontact() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentuser =auth.getCurrentUser().getUid();

        databaseReferencedeletecontacts = FirebaseDatabase.getInstance().getReference().child("Contact").child(currentuser).child(contactid);
        databaseReferencedeletenotes =FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid);

        databaseReferencedeletecontacts.removeValue();
        databaseReferencedeletenotes.removeValue();

        Intent intent = new Intent(this,ContactList.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Contact Deleted Successfully!",Toast.LENGTH_LONG).show();






    }

    private void updatecontact() {


        String name=etname.getText().toString().trim();
        String mobile=etmobileno.getText().toString().trim();
        String telephone=ettelphoneno.getText().toString().trim();
        String email =etemail.getText().toString().trim();
        String description =etdesciption.getText().toString().trim();
        String address =etaddress.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentuser =auth.getCurrentUser().getUid();

        databaseReferencecntactupdate = FirebaseDatabase.getInstance().getReference().child("Contact").child(currentuser).child(contactid);

        Contact contact = new Contact(contactid,currentuser,name,mobile,telephone,email,description,address);

        databaseReferencecntactupdate.setValue(contact);

        Toast.makeText(getApplicationContext(),"Contact Updated Successfully!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,ContactList.class);
        startActivity(intent);


    }
}
