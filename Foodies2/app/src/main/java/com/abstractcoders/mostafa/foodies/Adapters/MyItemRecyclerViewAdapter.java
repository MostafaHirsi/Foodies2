package com.abstractcoders.mostafa.foodies.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.Note;
import com.abstractcoders.mostafa.foodies.R;
import com.abstractcoders.mostafa.foodies.Fragments.NotesFragment;


import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Note> mValues;
    private final NotesFragment.OnListFragmentInteractionListener mListener;
    private Context context;


    public MyItemRecyclerViewAdapter(List<Note> items, NotesFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_note, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Note currentNote = holder.mItem;
        if(currentNote.getPlaceName().length() < 15)
        {
            holder.mIdView.setText(currentNote.getPlaceName());
        }else {
            holder.mIdView.setText(currentNote.getPlaceName().substring(0,14) + "...");
        }
        holder.mContentView.setText(currentNote.getNoteTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = (View) v.getParent().getParent();
                Animation anim = AnimationUtils.loadAnimation(
                        v.getContext(), android.R.anim.slide_out_right
                );
                anim.setDuration(500);
                item.startAnimation(anim);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(item.getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                        item.setVisibility(View.GONE);
                        notifyDataSetChanged();
                    }
                }, anim.getDuration());

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.deleteNote(currentNote.getNoteID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageButton mDeleteBtn;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.title_text);
            mDeleteBtn = (ImageButton) view.findViewById(R.id.delete_note_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        
    }
}
