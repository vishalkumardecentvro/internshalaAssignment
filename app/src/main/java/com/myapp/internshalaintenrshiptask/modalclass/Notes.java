package com.myapp.internshalaintenrshiptask.modalclass;

import android.os.Parcelable;

public class Notes  {
  private String content,id,accountId,name;

  public Notes() {
    // firebase needs empty constructor
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
