package com.fidenz.dev.crmdemo.Actvities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.Holders.ContactAdapter;
import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity implements ContactAdapter.mGetpostion {


    private Button btnlogout;

    private FirebaseAuth mAuth;

    private RecyclerView contactrecyclerView;

    private DatabaseReference databaseReferencecontact;

    private ProgressBar progressBar;

    private List<Contact> contactlist = new ArrayList<>();

    private ContactAdapter contactAdapter;

   // private EditText editTextserach;

    private SearchView searchView;

//    String userid = mAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactrecyclerView = findViewById(R.id.contactRecyler);

        progressBar = findViewById(R.id.progressbarcontactlist);
        //editTextserach = findViewById(R.id.editetextsearch);
        searchView = findViewById(R.id.editetextsearch);


        mAuth = FirebaseAuth.getInstance();
        databaseReferencecontact = FirebaseDatabase.getInstance().getReference("Contact").child(mAuth.getCurrentUser().getUid());
        databaseReferencecontact.keepSynced(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toString());
                return true;
            }
        });

//        editTextserach.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//
//            }
//        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(ContactList.this, ContactAdd.class);
                startActivity(intent);
            }
        });

        initView();
    }

    private void filter(String s) {

        List<Contact> contactfilterlist = new ArrayList<>();

        for (Contact contactitem : contactlist) {

            if (contactitem.getName().toLowerCase().contains(s.toLowerCase()) || contactitem.getEmail().toLowerCase().contains(s.toLowerCase())
                    || contactitem.getMobileNO().toString().contains(s.toString())) {

                contactfilterlist.add(contactitem);

            }

            contactAdapter.filterlist(contactfilterlist);


        }


    }

    public void initView() {


        contactrecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contactrecyclerView.setLayoutManager(layoutManager);


    }


    @Override
    protected void onStart() {
        super.onStart();


        databaseReferencecontact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);

                contactlist.clear();
                for (DataSnapshot contactsnapshot : dataSnapshot.getChildren()) {

                    Contact contact = contactsnapshot.getValue(Contact.class);

                    contactlist.add(contact);

                    contactAdapter = new ContactAdapter(contactlist, getApplicationContext(), ContactList.this);
                    contactrecyclerView.setAdapter(contactAdapter);
                    initView();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void getpositionedit(int Itemposition) {


//        Toast.makeText(getApplicationContext(), "position is:" + Itemposition, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ContactUpdateActvity.class);

        String nameupdate = contactlist.get(Itemposition).getName();
        String mobileupdate = contactlist.get(Itemposition).getMobileNO();
        String Tephonenoupdate = contactlist.get(Itemposition).getTelephoneno();
        String Emailupdate = contactlist.get(Itemposition).getEmail();
        String Descriptionupdate = contactlist.get(Itemposition).getDescription();
        String addressupdate = contactlist.get(Itemposition).getAddress();

        intent.putExtra("name", nameupdate);
        intent.putExtra("mobile", mobileupdate);
        intent.putExtra("telphoneno", Tephonenoupdate);
        intent.putExtra("email", Emailupdate);
        intent.putExtra("Description", Descriptionupdate);
        intent.putExtra("address", addressupdate);
        intent.putExtra("contactid", contactlist.get(Itemposition).getContactId());


        startActivity(intent);

    }

    @Override
    public void getpositioncomment(int Itemposition) {

//        Toast.makeText(getApplicationContext(), "position is:" + Itemposition, Toast.LENGTH_LONG).show();

        String ContactId = contactlist.get(Itemposition).getContactId();

        Intent intent = new Intent(this, NoteActvity.class);
        intent.putExtra("Contactid", ContactId);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(ContactList.this, MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
