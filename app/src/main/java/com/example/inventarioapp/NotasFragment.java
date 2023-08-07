package com.example.inventarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class NotasFragment extends Fragment {

    private EditText editTextNote;
    private Button buttonSave;
    private ListView listViewNotes;
    private List<String> noteList;
    private ArrayAdapter<String> noteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notas, container, false);

        editTextNote = view.findViewById(R.id.editTextNote);
        buttonSave = view.findViewById(R.id.buttonSave);
        listViewNotes = view.findViewById(R.id.listViewNotes);

        noteList = new ArrayList<>();
        noteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, noteList);
        listViewNotes.setAdapter(noteAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString();
                noteList.add(note);
                noteAdapter.notifyDataSetChanged();
                editTextNote.setText("");
            }
        });

        listViewNotes.setOnItemClickListener((parent, view1, position, id) -> {
            noteList.remove(position);
            noteAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
