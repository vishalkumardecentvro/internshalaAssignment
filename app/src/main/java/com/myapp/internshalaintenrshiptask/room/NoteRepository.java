package com.myapp.internshalaintenrshiptask.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.myapp.internshalaintenrshiptask.room.dao.NoteDao;
import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;

import java.util.List;

public class NoteRepository {
  private NoteDao noteDao;

  public NoteRepository(Application application) {
    Database database = Database.getDatabaseInstance(application);
    this.noteDao = database.noteDao();
  }

  public LiveData<List<NoteEntity>> getAllNotes(String accountId){
    return noteDao.getAllNotes(accountId);
  }

  public void insertTask(NoteEntity noteEntity){
    new InsertNoteAsync(noteDao).execute(noteEntity);
  }

  private static class InsertNoteAsync extends AsyncTask<NoteEntity,Void,Void>{
    private NoteDao noteDao;

    public InsertNoteAsync(NoteDao noteDao) {
      this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
      noteDao.insertNote(noteEntities[0]);
      return null;
    }
  }

  public void deleteNote(NoteEntity noteEntity){
    new DeleteNoteAsync(noteDao).execute(noteEntity);
  }

  private static class DeleteNoteAsync extends AsyncTask<NoteEntity,Void,Void>{
    private NoteDao noteDao;

    public DeleteNoteAsync(NoteDao noteDao) {
      this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
      noteDao.deleteNote(noteEntities[0]);
      return null;
    }
  }

  public void updateNote(NoteEntity noteEntity){
    new UpdateNote(noteDao).execute(noteEntity);
  }

  private static class UpdateNote extends AsyncTask<NoteEntity,Void,Void>{
    private NoteDao noteDao;

    public UpdateNote(NoteDao noteDao) {
      this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
      noteDao.updateNote(noteEntities[0]);
      return null;
    }
  }

  public void deleteAllNotes(String accountId){
    new DeleteAllNotes(accountId,noteDao).execute();

  }

  private static class DeleteAllNotes extends AsyncTask<Void,Void,Void>{
    private String accountId;
    private NoteDao noteDao;

    public DeleteAllNotes(String accountId, NoteDao noteDao) {
      this.accountId = accountId;
      this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      noteDao.deleteAllNotes(accountId);
      return null;
    }
  }
}
