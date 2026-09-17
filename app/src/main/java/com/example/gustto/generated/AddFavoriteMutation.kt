
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



public interface AddFavoriteMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      AddFavoriteMutation.Data,
      AddFavoriteMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val favorite_upsert: FavoriteKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "AddFavorite"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun AddFavoriteMutation.ref(
  
    restaurantId: java.util.UUID,

  
  
): com.google.firebase.dataconnect.MutationRef<
    AddFavoriteMutation.Data,
    AddFavoriteMutation.Variables
  > =
  ref(
    
      AddFavoriteMutation.Variables(
        restaurantId=restaurantId,
  
      )
    
  )

public suspend fun AddFavoriteMutation.execute(

  
    
      restaurantId: java.util.UUID,

  

  ): com.google.firebase.dataconnect.MutationResult<
    AddFavoriteMutation.Data,
    AddFavoriteMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,
  
    
  ).execute()


