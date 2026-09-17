
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



public interface AssignTypeMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      AssignTypeMutation.Data,
      AssignTypeMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val typeId: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val restaurantType_upsert: RestaurantTypeKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "AssignType"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun AssignTypeMutation.ref(
  
    restaurantId: java.util.UUID,typeId: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    AssignTypeMutation.Data,
    AssignTypeMutation.Variables
  > =
  ref(
    
      AssignTypeMutation.Variables(
        restaurantId=restaurantId,typeId=typeId,
  
      )
    
  )

public suspend fun AssignTypeMutation.execute(

  
    
      restaurantId: java.util.UUID,typeId: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    AssignTypeMutation.Data,
    AssignTypeMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,typeId=typeId,
  
    
  ).execute()


