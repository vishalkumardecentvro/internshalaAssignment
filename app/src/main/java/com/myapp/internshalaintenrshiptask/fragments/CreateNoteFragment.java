package com.myapp.internshalaintenrshiptask.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.internshalaintenrshiptask.databinding.FragmentCreateNoteBinding;

import java.util.HashMap;

public class CreateNoteFragment extends Fragment {
  private FragmentCreateNoteBinding binding;
  private FirebaseFirestore firestore;
  private Bundle bundle;
  private boolean editMode = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentCreateNoteBinding.inflate(inflater, container, false);
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

  private void instantiate() {
    getActivity().setTitle("Create notes");

    bundle = getArguments();
    if (bundle != null && bundle.containsKey("content"))
      editMode = true;
  }

  private void initialize() {
    firestore = FirebaseFirestore.getInstance();
    if (editMode) {
      binding.tilNotes.getEditText().setText(bundle.getString("content"));
    }

  }

  private void listen() {
    binding.cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

      }
    });

    binding.saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (editMode)
          processUpdateNote();
        else
          processSaveNote();
      }
    });

  }

  private void load() {


  }

  private void processSaveNote() {
    if (binding.tilNotes.getEditText().getText().toString().trim().isEmpty()) {
      Toast.makeText(getContext(), "Please enter note body", Toast.LENGTH_SHORT).show();
      return;
    } else
      saveNote();
  }

  private void saveNote() {
    SharedPreferences authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    String name = authSharedPref.getString("name","");
    String accountId = authSharedPref.getString("accountId","");

    HashMap<String, String> noteHash = new HashMap();
    noteHash.put("content", binding.tilNotes.getEditText().getText().toString());
    noteHash.put("accountId", accountId);
    noteHash.put("name",name);

    firestore.collection("notes").add(noteHash).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
      @Override
      public void onSuccess(DocumentReference documentReference) {
        Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();

      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(getContext(), "Failed to save note!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void processUpdateNote(){
    if (binding.tilNotes.getEditText().getText().toString().trim().isEmpty()) {
      Toast.makeText(getContext(), "Please enter note body!", Toast.LENGTH_SHORT).show();
      return;
    } else
      updateNote();
  }

  private void updateNote(){
    HashMap<String, Object> noteHash = new HashMap();
    noteHash.put("content", binding.tilNotes.getEditText().getText().toString());

    firestore.collection("notes").document(bundle.getString("id")).update(noteHash).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(Void unused) {
        Toast.makeText(getContext(), "Note updated", Toast.LENGTH_SHORT).show();
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(getContext(), "Failed to update note!", Toast.LENGTH_SHORT).show();
      }
    });
  }
}