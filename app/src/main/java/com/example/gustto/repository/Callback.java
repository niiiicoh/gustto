package com.example.gustto.repository;

public interface Callback<T> {
  void success(T value);

  void failure(String message);
}
