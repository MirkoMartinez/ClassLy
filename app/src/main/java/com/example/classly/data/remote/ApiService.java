package com.example.classly.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interfaz Retrofit para el consumo de servicios web y APIs REST.
 * Define los endpoints HTTP (GET, POST, etc.) requeridos para la conectividad con el mundo exterior.
 */
public interface ApiService {
    @GET("posts/1")
    Call<Object> getSampleData();
}
