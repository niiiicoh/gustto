package com.example.gustto.model;

public class UserProfile {
  public String id, username, email, role;

  public boolean isAdmin() {
    return "ADMIN".equals(role);
  }
}
