package com.myapp.internshalaintenrshiptask.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.internshalaintenrshiptask.R;
import com.myapp.internshalaintenrshiptask.databinding.FragmentNotesBinding;

public class NotesFragment extends Fragment {
  private FragmentNotesBinding binding;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentNotesBinding.inflate(inflater,container,false);
    View view = binding.getRoot();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}