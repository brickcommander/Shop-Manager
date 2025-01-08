import android.content.Context
import com.brickcommander.shop.model.Profile
import com.google.gson.Gson

val key = "profileObject"

fun saveObjectToPreferences(context: Context, obj: Profile) {
    val sharedPreferences = context.getSharedPreferences("SHOPAppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val jsonString = gson.toJson(obj) // Convert object to JSON
    editor.putString(key, jsonString) // Save JSON string
    editor.commit() // Apply changes
}

fun getObjectFromPreferences(context: Context): Profile? {
    val sharedPreferences = context.getSharedPreferences("SHOPAppPreferences", Context.MODE_PRIVATE)
    val gson = Gson()
    val jsonString = sharedPreferences.getString(key, null)
    return if (jsonString != null) gson.fromJson(jsonString, Profile::class.java) else null // Deserialize JSON to object
}
