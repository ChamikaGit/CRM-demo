package com.fidenz.dev.crmdemo.Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fidenz.dev.crmdemo.Models.Contact;
import com.fidenz.dev.crmdemo.Models.Notes;
import com.fidenz.dev.crmdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by fidenz on 4/13/18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    List<Notes> notesList;
    Context context;
    mGetpostion mGetpostion;

    public NoteAdapter(List<Notes> notesList, Context context, mGetpostion mGetpostion) {
        this.notesList = notesList;
        this.context = context;
        this.mGetpostion = mGetpostion;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {

        holder.notedescription.setText(notesList.get(position).getNotetext());
        holder.notedate.setText(notesList.get(position).getTime());

        //new add
        if (!notesList.get(position).getNoteimage().equals("note_image_null")) {
            Picasso.with(context).load(notesList.get(position).getNoteimage()).into(holder.noteim);
        }else {
            holder.noteim.setVisibility(View.GONE);
        }

        holder.noteedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetpostion.getpositionnoteedit(position);

            }
        });

        holder.noteaddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetpostion.getpositionnoteaddphoto(position);
            }
        });

        holder.notedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetpostion.getpositionnotedlete(position);
            }
        });

        holder.noteim.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mGetpostion.getnoteimage(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (notesList != null ? notesList.size() : 0);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {


        TextView notedescription;
        TextView notedate;
        ImageView noteedit;
        ImageView noteaddphoto;
        ImageView noteim;
        ImageView notedelete;


        public NoteViewHolder(View itemView) {
            super(itemView);

            notedescription = itemView.findViewById(R.id.tvDescriptin);
            notedate=itemView.findViewById(R.id.tvdate);
            noteedit =itemView.findViewById(R.id.imagnoteedit);
            noteaddphoto =itemView.findViewById(R.id.imagaddnotephoto);
            noteim =itemView.findViewById(R.id.noteim);
            notedelete =itemView.findViewById(R.id.imagaddnotedelete);
        }
    }

    public interface mGetpostion {

        void getpositionnoteedit(int Itemposition);
        void getpositionnoteaddphoto(int Itemposition);
        void getpositionnotedlete(int Itemposition);
        void getnoteimage(int Itemposition);


    }
}
