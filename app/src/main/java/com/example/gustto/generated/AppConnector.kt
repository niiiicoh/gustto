
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

import com.google.firebase.dataconnect.getInstance as _fdcGetInstance
import kotlin.time.Duration.Companion.milliseconds as _milliseconds

public interface AppConnector : com.google.firebase.dataconnect.generated.GeneratedConnector<AppConnector> {
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect

  
    public val addFavorite: AddFavoriteMutation
  
    public val adminRestaurants: AdminRestaurantsQuery
  
    public val adminReviews: AdminReviewsQuery
  
    public val assignCuisine: AssignCuisineMutation
  
    public val assignType: AssignTypeMutation
  
    public val catalogs: CatalogsQuery
  
    public val createProfile: CreateProfileMutation
  
    public val deleteMyReview: DeleteMyReviewMutation
  
    public val listRestaurants: ListRestaurantsQuery
  
    public val moderateReview: ModerateReviewMutation
  
    public val myFavorites: MyFavoritesQuery
  
    public val myProfile: MyProfileQuery
  
    public val removeFavorite: RemoveFavoriteMutation
  
    public val saveRestaurant: SaveRestaurantMutation
  
    public val saveReview: SaveReviewMutation
  
    public val saveSchedule: SaveScheduleMutation
  
    public val setRestaurantStatus: SetRestaurantStatusMutation
  

  public companion object {
    @Suppress("MemberVisibilityCanBePrivate")
    public val config: com.google.firebase.dataconnect.ConnectorConfig = com.google.firebase.dataconnect.ConnectorConfig(
      connector = "app",
      location = "southamerica-west1",
      serviceId = "gustto",
    )

    public fun getInstance(
      dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
    ):AppConnector = synchronized(instances) {
      instances.getOrPut(dataConnect) {
        AppConnectorImpl(dataConnect)
      }
    }

    private val instances = java.util.WeakHashMap<com.google.firebase.dataconnect.FirebaseDataConnect, AppConnectorImpl>()

    
  }
}

public val AppConnector.Companion.instance:AppConnector
  get() = getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(
    config
  ))

public fun AppConnector.Companion.getInstance(
  settings: com.google.firebase.dataconnect.DataConnectSettings = com.google.firebase.dataconnect.DataConnectSettings()
):AppConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(config, settings))

public fun AppConnector.Companion.getInstance(
  app: com.google.firebase.FirebaseApp,
  settings: com.google.firebase.dataconnect.DataConnectSettings = com.google.firebase.dataconnect.DataConnectSettings()
):AppConnector =
  getInstance(com.google.firebase.dataconnect.FirebaseDataConnect._fdcGetInstance(app, config, settings))

private class AppConnectorImpl(
  override val dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect
) : AppConnector {
  
    override val addFavorite by lazy(LazyThreadSafetyMode.PUBLICATION) {
      AddFavoriteMutationImpl(this)
    }
  
    override val adminRestaurants by lazy(LazyThreadSafetyMode.PUBLICATION) {
      AdminRestaurantsQueryImpl(this)
    }
  
    override val adminReviews by lazy(LazyThreadSafetyMode.PUBLICATION) {
      AdminReviewsQueryImpl(this)
    }
  
    override val assignCuisine by lazy(LazyThreadSafetyMode.PUBLICATION) {
      AssignCuisineMutationImpl(this)
    }
  
    override val assignType by lazy(LazyThreadSafetyMode.PUBLICATION) {
      AssignTypeMutationImpl(this)
    }
  
    override val catalogs by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CatalogsQueryImpl(this)
    }
  
    override val createProfile by lazy(LazyThreadSafetyMode.PUBLICATION) {
      CreateProfileMutationImpl(this)
    }
  
    override val deleteMyReview by lazy(LazyThreadSafetyMode.PUBLICATION) {
      DeleteMyReviewMutationImpl(this)
    }
  
    override val listRestaurants by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ListRestaurantsQueryImpl(this)
    }
  
    override val moderateReview by lazy(LazyThreadSafetyMode.PUBLICATION) {
      ModerateReviewMutationImpl(this)
    }
  
    override val myFavorites by lazy(LazyThreadSafetyMode.PUBLICATION) {
      MyFavoritesQueryImpl(this)
    }
  
    override val myProfile by lazy(LazyThreadSafetyMode.PUBLICATION) {
      MyProfileQueryImpl(this)
    }
  
    override val removeFavorite by lazy(LazyThreadSafetyMode.PUBLICATION) {
      RemoveFavoriteMutationImpl(this)
    }
  
    override val saveRestaurant by lazy(LazyThreadSafetyMode.PUBLICATION) {
      SaveRestaurantMutationImpl(this)
    }
  
    override val saveReview by lazy(LazyThreadSafetyMode.PUBLICATION) {
      SaveReviewMutationImpl(this)
    }
  
    override val saveSchedule by lazy(LazyThreadSafetyMode.PUBLICATION) {
      SaveScheduleMutationImpl(this)
    }
  
    override val setRestaurantStatus by lazy(LazyThreadSafetyMode.PUBLICATION) {
      SetRestaurantStatusMutationImpl(this)
    }
  

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun operations(): List<com.google.firebase.dataconnect.generated.GeneratedOperation<AppConnector, *, *>> =
    queries() + mutations()

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun mutations(): List<com.google.firebase.dataconnect.generated.GeneratedMutation<AppConnector, *, *>> =
    listOf(
      addFavorite,
        assignCuisine,
        assignType,
        createProfile,
        deleteMyReview,
        moderateReview,
        removeFavorite,
        saveRestaurant,
        saveReview,
        saveSchedule,
        setRestaurantStatus,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun queries(): List<com.google.firebase.dataconnect.generated.GeneratedQuery<AppConnector, *, *>> =
    listOf(
      adminRestaurants,
        adminReviews,
        catalogs,
        listRestaurants,
        myFavorites,
        myProfile,
        
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(dataConnect: com.google.firebase.dataconnect.FirebaseDataConnect) =
    AppConnectorImpl(dataConnect)

  override fun equals(other: Any?): Boolean =
    other is AppConnectorImpl &&
    other.dataConnect == dataConnect

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "AppConnectorImpl",
      dataConnect,
    )

  override fun toString(): String =
    "AppConnectorImpl(dataConnect=$dataConnect)"
}



private open class AppConnectorGeneratedQueryImpl<Data, Variables>(
  override val connector: AppConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedQuery<AppConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: AppConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    AppConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    AppConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    AppConnectorGeneratedQueryImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is AppConnectorGeneratedQueryImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "AppConnectorGeneratedQueryImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "AppConnectorGeneratedQueryImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}

private open class AppConnectorGeneratedMutationImpl<Data, Variables>(
  override val connector: AppConnector,
  override val operationName: String,
  override val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
  override val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
) : com.google.firebase.dataconnect.generated.GeneratedMutation<AppConnector, Data, Variables> {

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun copy(
    connector: AppConnector,
    operationName: String,
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data>,
    variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables>,
  ) =
    AppConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewVariables> withVariablesSerializer(
    variablesSerializer: kotlinx.serialization.SerializationStrategy<NewVariables>
  ) =
    AppConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  @com.google.firebase.dataconnect.ExperimentalFirebaseDataConnect
  override fun <NewData> withDataDeserializer(
    dataDeserializer: kotlinx.serialization.DeserializationStrategy<NewData>
  ) =
    AppConnectorGeneratedMutationImpl(
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun equals(other: Any?): Boolean =
    other is AppConnectorGeneratedMutationImpl<*,*> &&
    other.connector == connector &&
    other.operationName == operationName &&
    other.dataDeserializer == dataDeserializer &&
    other.variablesSerializer == variablesSerializer

  override fun hashCode(): Int =
    java.util.Objects.hash(
      "AppConnectorGeneratedMutationImpl",
      connector, operationName, dataDeserializer, variablesSerializer
    )

  override fun toString(): String =
    "AppConnectorGeneratedMutationImpl(" +
    "operationName=$operationName, " +
    "dataDeserializer=$dataDeserializer, " +
    "variablesSerializer=$variablesSerializer, " +
    "connector=$connector)"
}



private class AddFavoriteMutationImpl(
  connector: AppConnector
):
  AddFavoriteMutation,
  AppConnectorGeneratedMutationImpl<
      AddFavoriteMutation.Data,
      AddFavoriteMutation.Variables
  >(
    connector,
    AddFavoriteMutation.Companion.operationName,
    AddFavoriteMutation.Companion.dataDeserializer,
    AddFavoriteMutation.Companion.variablesSerializer,
  )


private class AdminRestaurantsQueryImpl(
  connector: AppConnector
):
  AdminRestaurantsQuery,
  AppConnectorGeneratedQueryImpl<
      AdminRestaurantsQuery.Data,
      Unit
  >(
    connector,
    AdminRestaurantsQuery.Companion.operationName,
    AdminRestaurantsQuery.Companion.dataDeserializer,
    AdminRestaurantsQuery.Companion.variablesSerializer,
  )


private class AdminReviewsQueryImpl(
  connector: AppConnector
):
  AdminReviewsQuery,
  AppConnectorGeneratedQueryImpl<
      AdminReviewsQuery.Data,
      Unit
  >(
    connector,
    AdminReviewsQuery.Companion.operationName,
    AdminReviewsQuery.Companion.dataDeserializer,
    AdminReviewsQuery.Companion.variablesSerializer,
  )


private class AssignCuisineMutationImpl(
  connector: AppConnector
):
  AssignCuisineMutation,
  AppConnectorGeneratedMutationImpl<
      AssignCuisineMutation.Data,
      AssignCuisineMutation.Variables
  >(
    connector,
    AssignCuisineMutation.Companion.operationName,
    AssignCuisineMutation.Companion.dataDeserializer,
    AssignCuisineMutation.Companion.variablesSerializer,
  )


private class AssignTypeMutationImpl(
  connector: AppConnector
):
  AssignTypeMutation,
  AppConnectorGeneratedMutationImpl<
      AssignTypeMutation.Data,
      AssignTypeMutation.Variables
  >(
    connector,
    AssignTypeMutation.Companion.operationName,
    AssignTypeMutation.Companion.dataDeserializer,
    AssignTypeMutation.Companion.variablesSerializer,
  )


private class CatalogsQueryImpl(
  connector: AppConnector
):
  CatalogsQuery,
  AppConnectorGeneratedQueryImpl<
      CatalogsQuery.Data,
      Unit
  >(
    connector,
    CatalogsQuery.Companion.operationName,
    CatalogsQuery.Companion.dataDeserializer,
    CatalogsQuery.Companion.variablesSerializer,
  )


private class CreateProfileMutationImpl(
  connector: AppConnector
):
  CreateProfileMutation,
  AppConnectorGeneratedMutationImpl<
      CreateProfileMutation.Data,
      CreateProfileMutation.Variables
  >(
    connector,
    CreateProfileMutation.Companion.operationName,
    CreateProfileMutation.Companion.dataDeserializer,
    CreateProfileMutation.Companion.variablesSerializer,
  )


private class DeleteMyReviewMutationImpl(
  connector: AppConnector
):
  DeleteMyReviewMutation,
  AppConnectorGeneratedMutationImpl<
      DeleteMyReviewMutation.Data,
      DeleteMyReviewMutation.Variables
  >(
    connector,
    DeleteMyReviewMutation.Companion.operationName,
    DeleteMyReviewMutation.Companion.dataDeserializer,
    DeleteMyReviewMutation.Companion.variablesSerializer,
  )


private class ListRestaurantsQueryImpl(
  connector: AppConnector
):
  ListRestaurantsQuery,
  AppConnectorGeneratedQueryImpl<
      ListRestaurantsQuery.Data,
      Unit
  >(
    connector,
    ListRestaurantsQuery.Companion.operationName,
    ListRestaurantsQuery.Companion.dataDeserializer,
    ListRestaurantsQuery.Companion.variablesSerializer,
  )


private class ModerateReviewMutationImpl(
  connector: AppConnector
):
  ModerateReviewMutation,
  AppConnectorGeneratedMutationImpl<
      ModerateReviewMutation.Data,
      ModerateReviewMutation.Variables
  >(
    connector,
    ModerateReviewMutation.Companion.operationName,
    ModerateReviewMutation.Companion.dataDeserializer,
    ModerateReviewMutation.Companion.variablesSerializer,
  )


private class MyFavoritesQueryImpl(
  connector: AppConnector
):
  MyFavoritesQuery,
  AppConnectorGeneratedQueryImpl<
      MyFavoritesQuery.Data,
      Unit
  >(
    connector,
    MyFavoritesQuery.Companion.operationName,
    MyFavoritesQuery.Companion.dataDeserializer,
    MyFavoritesQuery.Companion.variablesSerializer,
  )


private class MyProfileQueryImpl(
  connector: AppConnector
):
  MyProfileQuery,
  AppConnectorGeneratedQueryImpl<
      MyProfileQuery.Data,
      Unit
  >(
    connector,
    MyProfileQuery.Companion.operationName,
    MyProfileQuery.Companion.dataDeserializer,
    MyProfileQuery.Companion.variablesSerializer,
  )


private class RemoveFavoriteMutationImpl(
  connector: AppConnector
):
  RemoveFavoriteMutation,
  AppConnectorGeneratedMutationImpl<
      RemoveFavoriteMutation.Data,
      RemoveFavoriteMutation.Variables
  >(
    connector,
    RemoveFavoriteMutation.Companion.operationName,
    RemoveFavoriteMutation.Companion.dataDeserializer,
    RemoveFavoriteMutation.Companion.variablesSerializer,
  )


private class SaveRestaurantMutationImpl(
  connector: AppConnector
):
  SaveRestaurantMutation,
  AppConnectorGeneratedMutationImpl<
      SaveRestaurantMutation.Data,
      SaveRestaurantMutation.Variables
  >(
    connector,
    SaveRestaurantMutation.Companion.operationName,
    SaveRestaurantMutation.Companion.dataDeserializer,
    SaveRestaurantMutation.Companion.variablesSerializer,
  )


private class SaveReviewMutationImpl(
  connector: AppConnector
):
  SaveReviewMutation,
  AppConnectorGeneratedMutationImpl<
      SaveReviewMutation.Data,
      SaveReviewMutation.Variables
  >(
    connector,
    SaveReviewMutation.Companion.operationName,
    SaveReviewMutation.Companion.dataDeserializer,
    SaveReviewMutation.Companion.variablesSerializer,
  )


private class SaveScheduleMutationImpl(
  connector: AppConnector
):
  SaveScheduleMutation,
  AppConnectorGeneratedMutationImpl<
      SaveScheduleMutation.Data,
      SaveScheduleMutation.Variables
  >(
    connector,
    SaveScheduleMutation.Companion.operationName,
    SaveScheduleMutation.Companion.dataDeserializer,
    SaveScheduleMutation.Companion.variablesSerializer,
  )


private class SetRestaurantStatusMutationImpl(
  connector: AppConnector
):
  SetRestaurantStatusMutation,
  AppConnectorGeneratedMutationImpl<
      SetRestaurantStatusMutation.Data,
      SetRestaurantStatusMutation.Variables
  >(
    connector,
    SetRestaurantStatusMutation.Companion.operationName,
    SetRestaurantStatusMutation.Companion.dataDeserializer,
    SetRestaurantStatusMutation.Companion.variablesSerializer,
  )


