
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



public interface SaveScheduleMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      SaveScheduleMutation.Data,
      SaveScheduleMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val restaurantId: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val day: Int,
  
    val opens: String,
  
    val closes: String,
  
    val closed: Boolean,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val schedule_upsert: ScheduleKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "SaveSchedule"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun SaveScheduleMutation.ref(
  
    restaurantId: java.util.UUID,day: Int,opens: String,closes: String,closed: Boolean,

  
  
): com.google.firebase.dataconnect.MutationRef<
    SaveScheduleMutation.Data,
    SaveScheduleMutation.Variables
  > =
  ref(
    
      SaveScheduleMutation.Variables(
        restaurantId=restaurantId,day=day,opens=opens,closes=closes,closed=closed,
  
      )
    
  )

public suspend fun SaveScheduleMutation.execute(

  
    
      restaurantId: java.util.UUID,day: Int,opens: String,closes: String,closed: Boolean,

  

  ): com.google.firebase.dataconnect.MutationResult<
    SaveScheduleMutation.Data,
    SaveScheduleMutation.Variables
  > =
  ref(
    
      restaurantId=restaurantId,day=day,opens=opens,closes=closes,closed=closed,
  
    
  ).execute()


