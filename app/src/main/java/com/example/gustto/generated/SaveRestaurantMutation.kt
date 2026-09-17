
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



public interface SaveRestaurantMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      AppConnector,
      SaveRestaurantMutation.Data,
      SaveRestaurantMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val name: String,
  
    val address: String,
  
    val cityId: String,
  
    val latitude: Double,
  
    val longitude: Double,
  
    val description: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val phone: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val website: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val imageUrl: String,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var name: String
        public var address: String
        public var cityId: String
        public var latitude: Double
        public var longitude: Double
        public var description: String?
        public var phone: String?
        public var website: String?
        public var imageUrl: String
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,name: String,address: String,cityId: String,latitude: Double,longitude: Double,imageUrl: String,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var name= name
            var address= address
            var cityId= cityId
            var latitude= latitude
            var longitude= longitude
            var description: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var phone: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var website: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var imageUrl= imageUrl
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var name: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { name = value_ }
              
            override var address: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { address = value_ }
              
            override var cityId: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { cityId = value_ }
              
            override var latitude: Double
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { latitude = value_ }
              
            override var longitude: Double
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { longitude = value_ }
              
            override var description: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { description = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var phone: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { phone = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var website: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { website = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var imageUrl: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { imageUrl = value_ }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,name=name,address=address,cityId=cityId,latitude=latitude,longitude=longitude,description=description,phone=phone,website=website,imageUrl=imageUrl,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val restaurant_upsert: RestaurantKey,
  
    val restaurantType_deleteMany: Int,
  
    val restaurantCuisine_deleteMany: Int,
  
    val schedule_deleteMany: Int,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "SaveRestaurant"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun SaveRestaurantMutation.ref(
  
    id: java.util.UUID,name: String,address: String,cityId: String,latitude: Double,longitude: Double,imageUrl: String,

  
    block_: SaveRestaurantMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    SaveRestaurantMutation.Data,
    SaveRestaurantMutation.Variables
  > =
  ref(
    
      SaveRestaurantMutation.Variables.build(
        id=id,name=name,address=address,cityId=cityId,latitude=latitude,longitude=longitude,imageUrl=imageUrl,
  
    block_
      )
    
  )

public suspend fun SaveRestaurantMutation.execute(

  
    
      id: java.util.UUID,name: String,address: String,cityId: String,latitude: Double,longitude: Double,imageUrl: String,

  
    block_: SaveRestaurantMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    SaveRestaurantMutation.Data,
    SaveRestaurantMutation.Variables
  > =
  ref(
    
      id=id,name=name,address=address,cityId=cityId,latitude=latitude,longitude=longitude,imageUrl=imageUrl,
  
    block_
    
  ).execute()


