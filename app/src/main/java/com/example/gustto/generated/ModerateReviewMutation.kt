
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



public interface ModerateReviewMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      ModerateReviewMutation.Data,
      ModerateReviewMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val userId: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val review_delete: ReviewKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "ModerateReview"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun ModerateReviewMutation.ref(
  
    restaurantId: java.util.UUID,userId: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    ModerateReviewMutation.Data,
    ModerateReviewMutation.Variables
  > =
  ref(
    
      ModerateReviewMutation.Variables(
        restaurantId=restaurantId,userId=userId,
  
      )
    
  )

public suspend fun ModerateReviewMutation.execute(

  
    
      restaurantId: java.util.UUID,userId: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    ModerateReviewMutation.Data,
    ModerateReviewMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,userId=userId,
  
    
  ).execute()


