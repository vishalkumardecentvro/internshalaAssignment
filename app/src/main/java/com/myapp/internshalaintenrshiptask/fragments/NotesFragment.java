package com.myapp.internshalaintenrshiptask.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.myapp.internshalaintenrshiptask.R;
import com.myapp.internshalaintenrshiptask.adapter.NotesAdapter;
import com.myapp.internshalaintenrshiptask.databinding.FragmentNotesBinding;
import com.myapp.internshalaintenrshiptask.modalclass.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
  private FragmentNotesBinding binding;
  private FirebaseFirestore firestore;
  private List<Notes> noteList;
  private NotesAdapter notesAdapter;
  private CreateNoteFragment createNoteFragment;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentNotesBinding.inflate(inflater, container, false);
    View view = binding.getRoot();
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    instantiate();
    initialize();
    listen();
    load();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  private void instantiate() {
    getActivity().setTitle("Notes");

    firestore = FirebaseFirestore.getInstance();

    createNoteFragment = new CreateNoteFragment();

    noteList = new ArrayList<>();
    notesAdapter = new NotesAdapter();
  }

  private void initialize() {
    binding.rvNotes.setAdapter(notesAdapter);

  }

  private void listen() {
    binding.createNoteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToCreateNotesFragment(createNoteFragment, null);
      }
    });

    notesAdapter.setOnNoteClick(new NotesAdapter.onNoteClick() {
      @Override
      public void editNote(int position) {
        Notes notes = new Notes();
        notes = noteList.get(position);

        Bundle notesBundle = new Bundle();
        notesBundle.putString("content", notes.getContent());
        notesBundle.putString("id", notes.getId());

        navigateToCreateNotesFragment(createNoteFragment, notesBundle);
      }

      @Override
      public void deleteNote(int position) {
        Notes notes = new Notes();
        notes = noteList.get(position);

        firestore.collection("notes").document(notes.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void unused) {
            Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            load();
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Failed to delete note", Toast.LENGTH_SHORT).show();
          }
        });
      }
    });
  }

  private void load() {
    SharedPreferences authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    String accountId = authSharedPref.getString("accountId","");

    firestore.collection("notes").whereEqualTo("accountId",accountId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        noteList = new ArrayList<>();

        for (QueryDocumentSnapshot document : task.getResult()) {
          Notes notes = document.toObject(Notes.class);

          Notes notesObject = new Notes();
          notesObject.setContent(notes.getContent());
          notesObject.setId(document.getId());

          noteList.add(notesObject);
        }
        notesAdapter.setNotesList(noteList);
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(getContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void navigateToCreateNotesFragment(CreateNoteFragment createNoteFragment, @Nullable Bundle bundle) {

    if (bundle != null)
      createNoteFragment.setArguments(bundle);

    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.nav_host_fragment, createNoteFragment);
    fragmentTransaction.addToBackStack(null);

    fragmentTransaction.commit();
  }
}