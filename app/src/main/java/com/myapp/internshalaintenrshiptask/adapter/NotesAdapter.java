package com.myapp.internshalaintenrshiptask.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.internshalaintenrshiptask.databinding.RvNotesBinding;
import com.myapp.internshalaintenrshiptask.modalclass.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
  public onNoteClick noteClickListener;
  private List<Notes> notesList = new ArrayList<>();

  public void setOnNoteClick(onNoteClick noteClick) {
    noteClickListener = noteClick;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(RvNotesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), noteClickListener);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.populate(position);
  }

  @Override
  public int getItemCount() {
    return notesList.size();
  }

  public void setNotesList(List<Notes> notesList) {
    this.notesList = notesList;
    notifyDataSetChanged();
  }

  public interface onNoteClick {
    void editNote(int position);

    void deleteNote(int position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private RvNotesBinding binding;

    public ViewHolder(@NonNull RvNotesBinding binding, final onNoteClick listener) {
      super(binding.getRoot());
      this.binding = binding;

      binding.ivDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.deleteNote(position);
            }
          }
        }
      });

      binding.ivEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.editNote(position);
            }
          }
        }
      });
    }

    private void populate(int position) {
      binding.tvNoteContent.setText(notesList.get(position).getContent());
    }
  }

}
