
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



public interface RemoveFavoriteMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      RemoveFavoriteMutation.Data,
      RemoveFavoriteMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val favorite_delete: FavoriteKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "RemoveFavorite"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun RemoveFavoriteMutation.ref(
  
    restaurantId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    RemoveFavoriteMutation.Data,
    RemoveFavoriteMutation.Variables
  > =
  ref(
    
      RemoveFavoriteMutation.Variables(
        restaurantId=restaurantId,
  
      )
    
  )

public suspend fun RemoveFavoriteMutation.execute(

  
    
      restaurantId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    RemoveFavoriteMutation.Data,
    RemoveFavoriteMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,
  
    
  ).execute()


