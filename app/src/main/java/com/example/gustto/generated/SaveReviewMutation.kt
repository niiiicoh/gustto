
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



public interface SaveReviewMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      SaveReviewMutation.Data,
      SaveReviewMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val rating: Int,
  
    val comment: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val review_upsert: ReviewKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "SaveReview"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun SaveReviewMutation.ref(
  
    restaurantId: java.util.UUID,rating: Int,comment: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    SaveReviewMutation.Data,
    SaveReviewMutation.Variables
  > =
  ref(
    
      SaveReviewMutation.Variables(
        restaurantId=restaurantId,rating=rating,comment=comment,
  
      )
    
  )

public suspend fun SaveReviewMutation.execute(

  
    
      restaurantId: java.util.UUID,rating: Int,comment: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    SaveReviewMutation.Data,
    SaveReviewMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,rating=rating,comment=comment,
  
    
  ).execute()


