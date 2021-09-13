package com.myapp.internshalaintenrshiptask.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {

  @Insert
  void insertNote(NoteEntity noteEntity);

  @Delete
  void deleteNote(NoteEntity noteEntity);

  @Update
  void updateNote(NoteEntity noteEntity);

  @Query("delete from Notes where accountId = :accountId  ")
  void deleteAllNotes(String accountId);

  @Query("select * from Notes where accountId = :accountId order by id asc")
  LiveData<List<NoteEntity>> getAllNotes(String accountId);
}
