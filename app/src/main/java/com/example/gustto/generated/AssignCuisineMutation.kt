
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



public interface AssignCuisineMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      AssignCuisineMutation.Data,
      AssignCuisineMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val cuisineId: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val restaurantCuisine_upsert: RestaurantCuisineKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "AssignCuisine"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun AssignCuisineMutation.ref(
  
    restaurantId: java.util.UUID,cuisineId: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    AssignCuisineMutation.Data,
    AssignCuisineMutation.Variables
  > =
  ref(
    
      AssignCuisineMutation.Variables(
        restaurantId=restaurantId,cuisineId=cuisineId,
  
      )
    
  )

public suspend fun AssignCuisineMutation.execute(

  
    
      restaurantId: java.util.UUID,cuisineId: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    AssignCuisineMutation.Data,
    AssignCuisineMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,cuisineId=cuisineId,
  
    
  ).execute()


