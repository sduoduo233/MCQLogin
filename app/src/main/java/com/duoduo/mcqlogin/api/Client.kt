package com.duoduo.mcqlogin.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.StringReader
import java.lang.reflect.Type

class CallbackBodyConvertor(val gson: Gson, val adapter: TypeAdapter<*>): Converter<ResponseBody, Any> {
    override fun convert(value: ResponseBody): Any {
        var content = value.string()
        content = content.substring(2, content.length - 1)
        val reader = gson.newJsonReader(StringReader(content))
        return adapter.read(reader)
    }
}

object ApiClient {
    private const val baseUrl = "http://172.31.3.200/"

    fun get(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(object: Converter.Factory() {

                private val gson = GsonBuilder().create()

                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<out Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *> {
                    return CallbackBodyConvertor(gson, gson.getAdapter(TypeToken.get(type)))
                }

            })
            .build()
    }
}