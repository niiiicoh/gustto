
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


public interface MyProfileQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      AppConnector,
      MyProfileQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val user: User?,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class User(
  
    val id: String,
  
    val username: String,
  
    val email: String,
  
    val role: String,
  
    val createdAt: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.TimestampSerializer::class) com.google.firebase.Timestamp,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "MyProfile"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun MyProfileQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    MyProfileQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun MyProfileQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    MyProfileQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun MyProfileQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<MyProfileQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

