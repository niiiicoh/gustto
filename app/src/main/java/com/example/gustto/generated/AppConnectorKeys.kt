
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.gustto.generated


  @kotlinx.serialization.Serializable
  public data class FavoriteKey(
  
    val userId: String,
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class RestaurantCuisineKey(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val cuisineId: String,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class RestaurantKey(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class RestaurantTypeKey(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val typeId: String,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class ReviewKey(
  
    val userId: String,
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class ScheduleKey(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val day: Int,
  
  ) {
    
    
  }

  @kotlinx.serialization.Serializable
  public data class UserKey(
  
    val id: String,
  
  ) {
    
    
  }

