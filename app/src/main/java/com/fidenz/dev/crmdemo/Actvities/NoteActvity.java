package com.fidenz.dev.crmdemo.Actvities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.Holders.ContactAdapter;
import com.fidenz.dev.crmdemo.Holders.NoteAdapter;
import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.Models.Notes;
import com.fidenz.dev.crmdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;

public class NoteActvity extends AppCompatActivity implements NoteAdapter.mGetpostion {


    String contactid;
    DatabaseReference databaseReferencenote;
    DatabaseReference databaseReferencenotetextupdate;
    DatabaseReference databaseReferencenoteimageupdate;
    FirebaseAuth mauth;
    EditText editTextnote;
    String useerid;

    RecyclerView recyclerViewnote;
    NoteAdapter noteAdapter;

    Context context;
    private static final int GalleryRequestCode = 1115;
    private Uri resultUri;
    //ImageView imageViewnote;
    private StorageReference mstorageref, mdeleteref;

    private ProgressBar progressBarnote;
    private List<Notes> notesList = new ArrayList<>();

    private int positionimage;

    private ProgressDialog mprogress;
    DatabaseReference databaseReferencedeletenotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_actvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactid = getIntent().getStringExtra("Contactid");

        context = getApplicationContext();

        mprogress = new ProgressDialog(this);

        mauth = FirebaseAuth.getInstance();
        mstorageref = FirebaseStorage.getInstance().getReference();

        recyclerViewnote = findViewById(R.id.recylernote);
        progressBarnote = findViewById(R.id.progressbarnotelist);
        progressBarnote.setVisibility(View.VISIBLE);


        useerid = mauth.getCurrentUser().getUid();

        databaseReferencenote = FirebaseDatabase.getInstance().getReference("Notes");
        databaseReferencenote.keepSynced(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialognoteadd();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void dialognoteadd() {

        final Dialog dialog = new Dialog(NoteActvity.this);
        dialog.setContentView(R.layout.dialog_mcustom);
        Button dialogButtonyes = dialog.findViewById(R.id.btndilogyes);
        Button dilogButtonNo = dialog.findViewById(R.id.btndilogno);
        editTextnote = dialog.findViewById(R.id.etNote);
        //imageViewnote = dialog.findViewById(R.id.notephoto);


        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                noteadd();
            }
        });

        dilogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "No clicked..!!", Toast.LENGTH_SHORT).show();


            }
        });

//    imageViewnote.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            Toast.makeText(getApplicationContext(),"imageview clicked",Toast.LENGTH_LONG).show();
//            Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            GalleryIntent.setType("image/*");
//            startActivityForResult(GalleryIntent,GalleryRequestCode);
//        }
//    });


        dialog.show();


    }

    public void dialognoteedit(int position) {

        final String notetext = notesList.get(position).getNotetext();
        final String noteid = notesList.get(position).getNoteid();
//        final String notetime = notesList.get(position).getTime();
        final String noteimage = notesList.get(position).getNoteimage();

        final Dialog dialog = new Dialog(NoteActvity.this);
        dialog.setContentView(R.layout.dialog_editnote);
        Button dialogButtonyes = dialog.findViewById(R.id.btndilogyes);
        Button dilogButtonNo = dialog.findViewById(R.id.btndilogno);
        editTextnote = dialog.findViewById(R.id.etNote);

        editTextnote.setText(notetext);


        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd | hh:mm:ss");
        dateFormat.setLenient(false);
        Date today = new Date();
        final String date = dateFormat.format(today);


        dialogButtonyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String currentuser = auth.getCurrentUser().getUid();

                databaseReferencenotetextupdate = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid).child(noteid);
                databaseReferencenoteimageupdate = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid).child(noteid);

                Notes notes = new Notes(noteid, editTextnote.getText().toString().trim(), "Updated at: " + date, noteimage);

                databaseReferencenotetextupdate.setValue(notes);

                Toast.makeText(getApplicationContext(), "Update Successfully!", Toast.LENGTH_LONG).show();

                dialog.dismiss();


            }
        });

        dilogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "No clicked..!!", Toast.LENGTH_SHORT).show();


            }
        });

        dialog.show();


    }

    public void initView() {


        recyclerViewnote.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewnote.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference databaseReferencenote = FirebaseDatabase.getInstance().getReference().child("Notes").child(useerid).child(contactid);


        databaseReferencenote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressBarnote.setVisibility(View.GONE);


                notesList.clear();
                for (DataSnapshot notessnapshot : dataSnapshot.getChildren()) {

                    Notes notes = notessnapshot.getValue(Notes.class);

                    notesList.add(notes);

                    noteAdapter = new NoteAdapter(notesList, getApplicationContext(), NoteActvity.this);
                    recyclerViewnote.setAdapter(noteAdapter);
                    initView();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void noteadd() {


        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd | hh:mm:ss");
        dateFormat.setLenient(false);
        Date today = new Date();
        String date = dateFormat.format(today);

        String a = "note_image_null";


        String notetext = editTextnote.getText().toString().trim();

        if (!TextUtils.isEmpty(notetext)) {

            String id = databaseReferencenote.push().getKey();

            Notes notes = new Notes(id, notetext, date, a);


            //databaseReferencenote.child(useerid).child(contactid).setValue(notes);
            databaseReferencenote.child(useerid).child(contactid).child(id).setValue(notes);


            Toast.makeText(getApplicationContext(), " Note Saved..!!", Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void getpositionnoteedit(int Itemposition) {


        dialognoteedit(Itemposition);
//        Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_LONG).show();

    }

    @Override
    public void getpositionnoteaddphoto(int Itemposition) {

        addphoto(Itemposition);
//        Toast.makeText(getApplicationContext(), "add photo clicked", Toast.LENGTH_LONG).show();

    }

    @Override
    public void getpositionnotedlete(int Itemposition) {

        Toast.makeText(getApplicationContext(), "add delete clicked", Toast.LENGTH_LONG).show();

        notedelete(Itemposition);

    }

    @Override
    public void getnoteimage(int Itemposition) {




        View view = getLayoutInflater().inflate(R.layout.note_imagefull,null);
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(view);

        Picasso.with(context).load(notesList.get(Itemposition).getNoteimage()).into((ImageView) dialog.findViewById(R.id.idimagelarge));

        dialog.show();

        RelativeLayout relativeLayout = view.findViewById(R.id.imagelarge);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }

    private void notedelete(final int itemposition) {


        final Dialog dialog = new Dialog(NoteActvity.this);
        dialog.setContentView(R.layout.dialog_delete);
        Button dialogButtondeleteimage = dialog.findViewById(R.id.btndeleteimage);
        Button dilogButtondletenote = dialog.findViewById(R.id.btndeletenote);
        ImageView imageViewclose = dialog.findViewById(R.id.idclosedialog);
        editTextnote = dialog.findViewById(R.id.etNote);


        dialogButtondeleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                noteimagedelete(itemposition);

            }
        });

        dilogButtondletenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notedeleteitem(itemposition);
                dialog.dismiss();


            }
        });

        imageViewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }

    private void noteimagedelete(int itemposition) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentuser = auth.getCurrentUser().getUid();


        String noteid = notesList.get(itemposition).getNoteid();
        String notetext = notesList.get(itemposition).getNotetext();
        String notetime = notesList.get(itemposition).getTime();
        String notesaimage = notesList.get(itemposition).getNoteimage();

        mdeleteref = FirebaseStorage.getInstance().getReferenceFromUrl(notesaimage);

        mdeleteref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.e("Delete", "onSuccess: deleted file");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Delete", "onSuccess: not deleted file");

            }
        });


//        databaseReferencedeletenotes =FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid).child(noteid).child("noteimage");
//        databaseReferencedeletenotes.removeValue();

        Notes notes = new Notes(noteid, notetext, notetime, "note_image_null");
        databaseReferencenotetextupdate = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid).child(noteid);
        databaseReferencenotetextupdate.setValue(notes);


    }

    private void notedeleteitem(int position) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentuser = auth.getCurrentUser().getUid();

        String noteid = notesList.get(position).getNoteid();

        databaseReferencedeletenotes = FirebaseDatabase.getInstance().getReference().child("Notes").child(currentuser).child(contactid).child(noteid);
        databaseReferencedeletenotes.removeValue();


    }

    private void addphoto(int itemposition) {

        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, GalleryRequestCode);

        positionimage = itemposition;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryRequestCode && resultCode == RESULT_OK) {


            Uri imageuri = data.getData();
            CropImage.activity(imageuri).setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON).setActivityTitle("CRM APP")
                    .setBackgroundColor(Color.BLACK)
                    .setAutoZoomEnabled(true)
                    .setGuidelinesColor(Color.RED)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                //imageViewnote.setImageURI(resultUri);

                imageadd();


                Toast.makeText(getApplicationContext(), "crop clicked", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }
    }

    private void imageadd() {


        mprogress.setMessage("Uploading Image....");
        mprogress.show();
        mprogress.setCanceledOnTouchOutside(false);
        mprogress.setCancelable(false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd | hh:mm:ss");
        dateFormat.setLenient(false);
        Date today = new Date();
        final String date = dateFormat.format(today);

        final String id = notesList.get(positionimage).getNoteid();
        final String notetext = notesList.get(positionimage).getNotetext();


        StorageReference filepath = mstorageref.child("NoteImage").child(resultUri.getLastPathSegment());


        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Uri downloaduri = taskSnapshot.getDownloadUrl();
                String imageurlmew = downloaduri.toString();
                Notes notes = new Notes(id, notetext, date, imageurlmew);
                databaseReferencenote.child(useerid).child(contactid).child(id).setValue(notes);
                mprogress.dismiss();
                Toast.makeText(getApplicationContext(), " Image added Successfully..!!", Toast.LENGTH_SHORT).show();


            }
        });

    }


}
