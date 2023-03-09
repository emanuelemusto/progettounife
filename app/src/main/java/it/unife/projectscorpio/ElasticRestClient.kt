package it.unife.projectscorpio

import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class ElasticRestClient {
    // called when request is retried// called when response HTTP status is "4XX" (eg. 401, 403, 404)// If the response is JSONObject instead of expected JSONArray
    // instead of 'get' use twitter/tweet/1
    val httpRequest: Unit
        get() {
            try {
                Companion["get", null, object : JsonHttpResponseHandler() {
                    // instead of 'get' use twitter/tweet/1
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<Header>,
                        response: JSONObject
                    ) {
                        // If the response is JSONObject instead of expected JSONArray
                        Log.i(CLASS_NAME, "onSuccess: $response")
                    }

                    override fun onSuccess(
                        statusCode: Int,
                        headers: Array<Header>,
                        response: JSONArray
                    ) {
                        Log.i(CLASS_NAME, "onSuccess: $response")
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<Header>,
                        responseString: String,
                        throwable: Throwable
                    ) {
                        super.onFailure(statusCode, headers, responseString, throwable)
                        Log.e(CLASS_NAME, "onFailure")
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }

                    override fun onRetry(retryNo: Int) {
                        Log.i(CLASS_NAME, "onRetry $retryNo")
                        // called when request is retried
                    }
                }]
            } catch (e: Exception) {
                Log.e(CLASS_NAME, e.localizedMessage)
            }
        }

    companion object {
        private const val BASE_URL = "http://localhost:9200/"
        private val CLASS_NAME = ElasticRestClient::class.java.simpleName
        private val client = AsyncHttpClient()
        operator fun get(
            url: String,
            params: RequestParams?,
            responseHandler: AsyncHttpResponseHandler?
        ) {
            client[getAbsoluteUrl(url), params, responseHandler]
        }

        fun post(url: String, params: RequestParams?, responseHandler: AsyncHttpResponseHandler?) {
            client.post(getAbsoluteUrl(url), params, responseHandler)
        }

        private fun getAbsoluteUrl(relativeUrl: String): String {
            return BASE_URL + relativeUrl
        }
    }
}