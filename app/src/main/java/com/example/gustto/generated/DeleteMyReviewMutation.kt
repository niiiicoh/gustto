
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



public interface DeleteMyReviewMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      DeleteMyReviewMutation.Data,
      DeleteMyReviewMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val review_delete: ReviewKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "DeleteMyReview"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun DeleteMyReviewMutation.ref(
  
    restaurantId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    DeleteMyReviewMutation.Data,
    DeleteMyReviewMutation.Variables
  > =
  ref(
    
      DeleteMyReviewMutation.Variables(
        restaurantId=restaurantId,
  
      )
    
  )

public suspend fun DeleteMyReviewMutation.execute(

  
    
      restaurantId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    DeleteMyReviewMutation.Data,
    DeleteMyReviewMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,
  
    
  ).execute()


