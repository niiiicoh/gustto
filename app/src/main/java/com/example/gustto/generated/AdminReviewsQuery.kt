
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


public interface AdminReviewsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      AppConnector,
      AdminReviewsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val reviews: List<ReviewsItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class ReviewsItem(
  
    val user: User,
  
    val restaurant: Restaurant,
  
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
      
        @kotlinx.serialization.Serializable
  public data class Restaurant(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val name: String,
  
  ) {
    
    
  }
      
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "AdminReviews"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun AdminReviewsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    AdminReviewsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun AdminReviewsQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    AdminReviewsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun AdminReviewsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<AdminReviewsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

