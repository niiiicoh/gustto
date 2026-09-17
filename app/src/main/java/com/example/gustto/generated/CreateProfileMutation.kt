
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



public interface CreateProfileMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      CreateProfileMutation.Data,
      CreateProfileMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val username: String,
  
  ) {
    
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val user_insert: UserKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateProfile"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateProfileMutation.ref(
  
    username: String,

  
  
): com.google.firebase.dataconnect.MutationRef<
    CreateProfileMutation.Data,
    CreateProfileMutation.Variables
  > =
  ref(
    
      CreateProfileMutation.Variables(
        username=username,
  
      )
    
  )

public suspend fun CreateProfileMutation.execute(

  
    
      username: String,

  

  ): com.google.firebase.dataconnect.MutationResult<
    CreateProfileMutation.Data,
    CreateProfileMutation.Variables
  > =
  ref(
    
      username=username,
  
    
  ).execute()


