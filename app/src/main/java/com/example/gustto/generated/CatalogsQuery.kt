
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


public interface CatalogsQuery :
    com.google.firebase.dataconnect.generated.GeneratedQuery<
      AppConnector,
      CatalogsQuery.Data,
      Unit
    >
{
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val cities: List<CitiesItem>,
  
    val establishmentTypes: List<EstablishmentTypesItem>,
  
    val cuisines: List<CuisinesItem>,
  
  ) {
    
      
        @kotlinx.serialization.Serializable
  public data class CitiesItem(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class EstablishmentTypesItem(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
        @kotlinx.serialization.Serializable
  public data class CuisinesItem(
  
    val id: String,
  
    val name: String,
  
  ) {
    
    
  }
      
    
    
  }
  

  public companion object {
    public val operationName: String = "Catalogs"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Unit> =
      kotlinx.serialization.serializer()
  }
}

public fun CatalogsQuery.ref(
  
): com.google.firebase.dataconnect.QueryRef<
    CatalogsQuery.Data,
    Unit
  > =
  ref(
    
      Unit
    
  )

public suspend fun CatalogsQuery.execute(

  

  ): com.google.firebase.dataconnect.QueryResult<
    CatalogsQuery.Data,
    Unit
  > =
  ref(
    
  ).execute()


  public fun CatalogsQuery.flow(
    
    ): kotlinx.coroutines.flow.Flow<CatalogsQuery.Data> =
    ref(
        
      ).subscribe()
      .flow
      ._flow_map { querySubscriptionResult -> querySubscriptionResult.result.getOrNull() }
      ._flow_filterNotNull()
      ._flow_map { it.data }

