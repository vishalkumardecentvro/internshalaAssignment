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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.myapp.internshalaintenrshiptask.databinding.FragmentCreateNoteBinding;
import com.myapp.internshalaintenrshiptask.room.viewmodel.NoteView;
import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;
import com.myapp.internshalaintenrshiptask.utils.Utils;

public class CreateNoteFragment extends Fragment {
  private FragmentCreateNoteBinding binding;
  private Bundle bundle;
  private boolean editMode = false;
  private FragmentManager fragmentManager;
  private NoteView noteView;
  private SharedPreferences authSharedPref;

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

    bundle = getArguments();
    if (bundle != null && bundle.containsKey("content"))
      editMode = true;

    if (editMode)
      getActivity().setTitle("Update note");
    else
      getActivity().setTitle("Create note");
  }

  private void initialize() {
    authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    noteView = ViewModelProviders.of((FragmentActivity) getContext()).get(NoteView.class);

    if (editMode) {
      binding.tilNotes.getEditText().setText(bundle.getString("content"));
    }
  }

  private void listen() {
    binding.cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
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
    } else {
      saveNote();
      popBack();
    }
  }

  private void saveNote() {

    NoteEntity noteEntity = new NoteEntity();
    noteEntity.setNotes(binding.tilNotes.getEditText().getText().toString());
    noteEntity.setAccountId(authSharedPref.getString(Utils.ACCOUNT_ID, ""));
    noteEntity.setName(authSharedPref.getString(Utils.NAME, ""));

    noteView.insertNote(noteEntity);

  }

  private void processUpdateNote() {
    if (binding.tilNotes.getEditText().getText().toString().trim().isEmpty()) {
      Toast.makeText(getContext(), "Please enter note body!", Toast.LENGTH_SHORT).show();
      return;
    } else {
      updateNote();
      popBack();
    }
  }

  private void updateNote() {

    NoteEntity noteEntity = new NoteEntity();
    noteEntity.setNotes(binding.tilNotes.getEditText().getText().toString());
    noteEntity.setId(bundle.getInt("id"));
    noteEntity.setAccountId(authSharedPref.getString(Utils.ACCOUNT_ID, ""));
    noteEntity.setName(authSharedPref.getString(Utils.NAME, ""));

    noteView.updateNote(noteEntity);
  }

  private void popBack() {
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    fragmentManager.popBackStack();
  }
}