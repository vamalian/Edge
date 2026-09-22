package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun askJarvix(prompt: String, history: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "JARVIX AI Core offline: GEMINI_API_KEY is not configured. Please add your Gemini API key in the AI Studio Secrets panel."
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val contentsArray = JSONArray()

            // System instruction
            val systemObj = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", "You are JARVIX, a premium futuristic AI assistant. Intelligent, calm, confident, friendly, helpful, and slightly futuristic. Keep responses concise and engaging.")))
            }

            // History
            for ((role, text) in history) {
                contentsArray.put(
                    JSONObject().put("parts", JSONArray().put(JSONObject().put("text", text)))
                )
            }

            // Current prompt
            contentsArray.put(
                JSONObject().put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            )

            val requestJson = JSONObject().apply {
                put("contents", contentsArray)
                put("systemInstruction", systemObj)
            }

            val body = requestJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext "Neural link error (${response.code}): ${response.message}"
                }
                val responseBody = response.body?.string() ?: return@withContext "Empty response from AI core."
                val jsonRoot = JSONObject(responseBody)
                val candidates = jsonRoot.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val part = parts.getJSONObject(0)
                        return@withContext part.optString("text", "No text generated.")
                    }
                }
                return@withContext "No candidate response received from JARVIX core."
            }
        } catch (e: Exception) {
            return@withContext "Network connection error: ${e.localizedMessage ?: "Unknown"}. Please check your connection."
        }
    }
}
