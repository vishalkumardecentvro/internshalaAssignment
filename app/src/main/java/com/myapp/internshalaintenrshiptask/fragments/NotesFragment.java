package com.myapp.internshalaintenrshiptask.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.myapp.internshalaintenrshiptask.R;
import com.myapp.internshalaintenrshiptask.activity.MainActivity;
import com.myapp.internshalaintenrshiptask.adapter.NotesAdapter;
import com.myapp.internshalaintenrshiptask.databinding.FragmentNotesBinding;
import com.myapp.internshalaintenrshiptask.room.NoteView;
import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;
import com.myapp.internshalaintenrshiptask.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {
  private FragmentNotesBinding binding;
  private NotesAdapter notesAdapter;
  private CreateNoteFragment createNoteFragment;
  private GoogleSignInClient googleSignInClient;
  private GoogleSignInOptions gso;
  private NoteView noteView;
  private List<NoteEntity> noteListEntity;
  private SharedPreferences authSharedPref;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    getActivity().getMenuInflater().inflate(R.menu.log_out_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.logOut:
        logout();
        break;
      case R.id.deleteAll:
        processDeleteAllNotes();
        break;
    }
    return super.onOptionsItemSelected(item);
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

    noteView = ViewModelProviders.of((FragmentActivity) getContext()).get(NoteView.class);

    SharedPreferences authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    String name = authSharedPref.getString(Utils.NAME, "");
    binding.tvName.setText(name);

    createNoteFragment = new CreateNoteFragment();

    notesAdapter = new NotesAdapter();

    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
  }

  private void initialize() {
    authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
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

        NoteEntity note = new NoteEntity();
        if (!noteListEntity.isEmpty()) {

          note = noteListEntity.get(position);

          Bundle notesBundle = new Bundle();
          notesBundle.putString("content", note.getNotes());
          notesBundle.putInt("id", note.getId());

          navigateToCreateNotesFragment(createNoteFragment, notesBundle);
        }

      }

      @Override
      public void deleteNote(int position) {
        NoteEntity note = new NoteEntity();

        if (!noteListEntity.isEmpty()) {
          note = noteListEntity.get(position);
          noteView.deleteNote(note);
        }
      }
    });
  }

  private void load() {
    String accountId = authSharedPref.getString(Utils.ACCOUNT_ID, "");

    noteView.getAllNotes(accountId).observe(getViewLifecycleOwner(), new Observer<List<NoteEntity>>() {
      @Override
      public void onChanged(List<NoteEntity> noteEntities) {
        if (noteEntities.size() != 0)
          binding.tvEmptyList.setVisibility(View.GONE);
        else
          binding.tvEmptyList.setVisibility(View.VISIBLE);

        if (!(noteEntities.isEmpty())) {
          noteListEntity = noteEntities;
          notesAdapter.setNotesList(noteEntities);
        }
        else
          notesAdapter.setNotesList(new ArrayList<>());
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

  private void logout() {
    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        SharedPreferences authSharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = authSharedPref.edit();
        editor.putString(Utils.STATUS, Utils.SIGNED_OUT).apply();

        startActivity(new Intent(getContext(), MainActivity.class));
      }
    });
  }

  private void processDeleteAllNotes(){
    new MaterialAlertDialogBuilder(getContext())
            .setTitle("Delete all notes?")
            .setMessage("Once notes deleted cannot be undone!")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
              }
            }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        deleteAllNotes();
      }
    }).show();
  }

  private void deleteAllNotes(){
    noteView.deleteAllNotes(authSharedPref.getString(Utils.ACCOUNT_ID, ""));
    load();
  }

}