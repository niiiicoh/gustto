package com.example.gustto.data.remote
import com.example.gustto.generated.*
import com.example.gustto.repository.Callback
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
/** Only coroutine/serialization adaptation. Business rules live in Java repositories. */
class SqlConnectBridge {
 private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
 private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
 fun execute(operation: String, variables: String, callback: Callback<String>) {
  scope.launch {
   try {
    val result = withTimeout(25000) {
     val c = AppConnector.instance
     when(operation) {
      "ListRestaurants" -> json.encodeToString(c.listRestaurants.execute().data)
      "AdminRestaurants" -> json.encodeToString(c.adminRestaurants.execute().data)
      "MyProfile" -> json.encodeToString(c.myProfile.execute().data)
      "MyFavorites" -> json.encodeToString(c.myFavorites.execute().data)
      "AdminReviews" -> json.encodeToString(c.adminReviews.execute().data)
      "Catalogs" -> json.encodeToString(c.catalogs.execute().data)
      "CreateProfile" -> json.encodeToString(c.createProfile.ref(json.decodeFromString<CreateProfileMutation.Variables>(variables)).execute().data)
      "AddFavorite" -> json.encodeToString(c.addFavorite.ref(json.decodeFromString<AddFavoriteMutation.Variables>(variables)).execute().data)
      "RemoveFavorite" -> json.encodeToString(c.removeFavorite.ref(json.decodeFromString<RemoveFavoriteMutation.Variables>(variables)).execute().data)
      "SaveReview" -> json.encodeToString(c.saveReview.ref(json.decodeFromString<SaveReviewMutation.Variables>(variables)).execute().data)
      "DeleteMyReview" -> json.encodeToString(c.deleteMyReview.ref(json.decodeFromString<DeleteMyReviewMutation.Variables>(variables)).execute().data)
      "ModerateReview" -> json.encodeToString(c.moderateReview.ref(json.decodeFromString<ModerateReviewMutation.Variables>(variables)).execute().data)
      "SetRestaurantStatus" -> json.encodeToString(c.setRestaurantStatus.ref(json.decodeFromString<SetRestaurantStatusMutation.Variables>(variables)).execute().data)
      "SaveRestaurant" -> json.encodeToString(c.saveRestaurant.ref(json.decodeFromString<SaveRestaurantMutation.Variables>(variables)).execute().data)
      "AssignType" -> json.encodeToString(c.assignType.ref(json.decodeFromString<AssignTypeMutation.Variables>(variables)).execute().data)
      "AssignCuisine" -> json.encodeToString(c.assignCuisine.ref(json.decodeFromString<AssignCuisineMutation.Variables>(variables)).execute().data)
      "SaveSchedule" -> json.encodeToString(c.saveSchedule.ref(json.decodeFromString<SaveScheduleMutation.Variables>(variables)).execute().data)
      else -> error("Operación desconocida")
     }
    }
    callback.success(result)
   } catch (e: Exception) {
    callback.failure("No se pudo completar la operación. Revisa la conexión y vuelve a intentar.")
   }
  }
 }
}
