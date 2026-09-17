
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



public interface SetRestaurantStatusMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      SetRestaurantStatusMutation.Data,
      SetRestaurantStatusMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val status: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val restaurant_update: RestaurantKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "SetRestaurantStatus"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun SetRestaurantStatusMutation.ref(
  
    id: java.util.UUID,status: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    SetRestaurantStatusMutation.Data,
    SetRestaurantStatusMutation.Variables
  > =
  ref(
    
      SetRestaurantStatusMutation.Variables(
        id=id,status=status,
  
      )
    
  )

public suspend fun SetRestaurantStatusMutation.execute(

  
    
      id: java.util.UUID,status: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    SetRestaurantStatusMutation.Data,
    SetRestaurantStatusMutation.Variables
  > =
  ref(
    
      id=id,status=status,
  
    
  ).execute()


