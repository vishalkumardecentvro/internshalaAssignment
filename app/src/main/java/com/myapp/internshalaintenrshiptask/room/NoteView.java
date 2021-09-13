package com.myapp.internshalaintenrshiptask.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;

import java.util.List;

public class NoteView extends AndroidViewModel {
  private NoteRepository noteRepository;

  public NoteView(@NonNull Application application) {
    super(application);
    noteRepository = new NoteRepository(application);
  }

  public LiveData<List<NoteEntity>> getAllNotes(String accountId){
    return noteRepository.getAllNotes(accountId);
  }

  public void insertNote(NoteEntity noteEntity) {
    noteRepository.insertTask(noteEntity);
  }

  public void deleteNote(NoteEntity noteEntity){
    noteRepository.deleteNote(noteEntity);
  }

  public void updateNote(NoteEntity noteEntity){
    noteRepository.updateNote(noteEntity);
  }
}
