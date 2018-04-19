package com.fidenz.dev.crmdemo.Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.R;

import java.util.List;

/**
 * Created by fidenz on 4/12/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {


    List<Contact> contactList;
    Context context;
    mGetpostion mGetpostion;


    public ContactAdapter(List<Contact> contactList, Context context, ContactAdapter.mGetpostion mGetpostion) {
        this.contactList = contactList;
        this.context = context;
        this.mGetpostion = mGetpostion;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contactlist, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {


        holder.contactname.setText(contactList.get(position).getName());
        holder.contactphoneno.setText(contactList.get(position).getMobileNO());
        holder.contactemail.setText(contactList.get(position).getEmail());


        holder.imgcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetpostion.getpositioncomment(position);
            }
        });

        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetpostion.getpositionedit(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (contactList != null ? contactList.size() : 0);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactname;
        TextView contactphoneno;
        TextView contactemail;
        ImageView imgedit,imgcomment;

        public ContactViewHolder(View itemView) {
            super(itemView);


            contactname = itemView.findViewById(R.id.tvcontactname);
            contactphoneno =itemView.findViewById(R.id.tvphoneno);
            contactemail =itemView.findViewById(R.id.tvemail);
            imgedit =itemView.findViewById(R.id.imgedit);
            imgcomment =itemView.findViewById(R.id.imgcomment);
        }


    }



    public interface mGetpostion {

        void getpositionedit(int Itemposition);
        void getpositioncomment(int Itemposition);


    }


    public  void  filterlist(List<Contact> contactfiltedList){
        contactList =contactfiltedList;
        notifyDataSetChanged();
    }



}
