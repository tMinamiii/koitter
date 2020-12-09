package koitter

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import io.github.cdimascio.dotenv.Dotenv
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun fetchBearerToken(): String? {
    val dotenv = Dotenv.load();
    val key = dotenv.get("API_TOKEN");
    val keySecret = dotenv.get("API_KEY_SECRET")
    val base64BearerToken = Base64.getEncoder().encodeToString("$key:$keySecret".toByteArray())
    val url = "https://api.twitter.com/oauth2/token"
    val resp = url.httpPost().header(
        mapOf(
            "Authorization" to "Basic $base64BearerToken",
            "Content-Type" to "application/x-www-form-urlencoded;charset=UTF-8"
        )
    ).body("grant_type=client_credentials").response()
    val result = String(resp.second.data)
    val gson = Gson()
    val type = object : TypeToken<HashMap<String, String>>(){}.type
    val resultJson: Map<String, String> = gson.fromJson(result, type)
    return resultJson["access_token"]
}

fun fetchTimeline(token: String, screenName: String, count: Int ) {
    val url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=$screenName&count=$count"

    println(token)
    val resp = url.httpGet().header(
        mapOf(
            "Authorization" to "Bearer $token",
            "Content-Type" to "application/json"
        )
    ).response()
    val result = String(resp.second.data)
    println(result)
    // val gson = Gson()
    // val type = object : TypeToken<List<HashMap<String, Object>>>() {}.type
    // return gson.fromJson(result, type)
}

fun main() {
    val screenName = "tMinamiii"
    val dotenv = Dotenv.load();
    val token = fetchBearerToken()
    if (token != null) {
        println(fetchTimeline(token=token, screenName=screenName, count=10))
    }
}
