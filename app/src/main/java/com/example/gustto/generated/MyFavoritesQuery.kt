
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


import kotlinx.coroutines.flow.filterNotNull as _flow_filterNotNull
import kotlinx.coroutines.flow.map as _flow_map


public interface MyFavoritesQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      AppConnector,
      MyFavoritesQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val favorites: List<FavoritesItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class FavoritesItem(
  
    val restaurant: Restaurant,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Restaurant(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val name: String,
  
    val address: String,
  
    val city: City,
  
    val latitude: Double,
  
    val longitude: Double,
  
    val description: String?,
  
    val phone: String?,
  
    val website: String?,
  
    val imageUrl: String,
  
    val status: String,
  
    val restaurantTypes_on_restaurant: List<RestaurantTypesOnRestaurantItem>,
  
    val restaurantCuisines_on_restaurant: List<RestaurantCuisinesOnRestaurantItem>,
  
    val schedules_on_restaurant: List<SchedulesOnRestaurantItem>,
  
    val reviews_on_restaurant: List<ReviewsOnRestaurantItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class City(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class RestaurantTypesOnRestaurantItem(
  
    val type: Type,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Type(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class RestaurantCuisinesOnRestaurantItem(
  
    val cuisine: Cuisine,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class Cuisine(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class SchedulesOnRestaurantItem(
  
    val day: Int,
  
    val opens: String?,
  
    val closes: String?,
  
    val closed: Boolean,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class ReviewsOnRestaurantItem(
  
    val user: User,
  
    val rating: Int,
  
    val comment: String,
  
    val createdAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
    val updatedAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class User(
  
    val id: String,
  
    val username: String,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "MyFavorites"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun MyFavoritesQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    MyFavoritesQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun MyFavoritesQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    MyFavoritesQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun MyFavoritesQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<MyFavoritesQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

