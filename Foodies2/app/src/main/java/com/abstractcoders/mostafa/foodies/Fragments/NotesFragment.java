package com.abstractcoders.mostafa.foodies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abstractcoders.mostafa.foodies.Handlers.DatabaseHelper;
import com.abstractcoders.mostafa.foodies.Model.CurrentUserSingleton;
import com.abstractcoders.mostafa.foodies.Model.Note;
import com.abstractcoders.mostafa.foodies.Adapters.MyItemRecyclerViewAdapter;
import com.abstractcoders.mostafa.foodies.NoteActivity;
import com.abstractcoders.mostafa.foodies.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;
    private OnListFragmentInteractionListener mListener;
    private FloatingActionButton addNotesFab;
    private RecyclerView recyclerView;
    MyItemRecyclerViewAdapter recyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotesFragment newInstance(int columnCount) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        addNotesFab = (FloatingActionButton) view.findViewById(R.id.addNoteFab);
        addNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(inflater.getContext(), NoteActivity.class);
                i.putExtra("SelectedNote",new Note(Integer.toString(CurrentUserSingleton.getInstance().getUserName()),"","","","",""));
                startActivity(i);
            }
        });

        DatabaseHelper db = new DatabaseHelper(getContext());
        recyclerViewAdapter = new MyItemRecyclerViewAdapter(db.retrieveNotes(CurrentUserSingleton.getInstance().getUserName()), mListener);
        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            recyclerView.setAdapter(recyclerViewAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        recyclerViewAdapter.notifyDataSetChanged();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Note item);
    }




}
