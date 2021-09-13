package com.myapp.internshalaintenrshiptask.room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.myapp.internshalaintenrshiptask.room.dao.NoteDao;
import com.myapp.internshalaintenrshiptask.room.entity.NoteEntity;

@androidx.room.Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
  public static Database databaseInstance;

  public static synchronized Database getDatabaseInstance(Context context) {
    if (databaseInstance == null) {
      databaseInstance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "notes")
              .fallbackToDestructiveMigrationFrom()
              .build();
    }
    return databaseInstance;
  }

  public abstract NoteDao noteDao();
}
