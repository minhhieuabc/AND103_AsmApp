package com.example.asm_and103.Service;

import com.example.asm_and103.Model.Product;
import com.example.asm_and103.Model.Respone;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiServices {
//    public static final String BASE_URL = "http://192.168.1.10/";
    public static final String BASE_URL = "http://192.168.1.4:3000/";
    @GET("/get-list-product")
    Call<Respone<ArrayList<Product>>> getListProduct();
    @Multipart
    @POST("/add-product")
    Call<Respone<Product>> addProduct(@Part("name")RequestBody name,
                                      @Part("quantity")RequestBody quantity,
                                      @Part("price")RequestBody price,
                                      @Part MultipartBody.Part image);

    @Multipart
    @PUT("/update-product/{id_product}")
    Call<Respone<Product>> updateProduct(@Path("id_product")String id_product,
                                          @Part("name") RequestBody name,
                                          @Part("quantity") RequestBody quantity,
                                          @Part("price") RequestBody price,
                                          @Part MultipartBody.Part image);

    @POST("/update-product-without-thumbnail/{id_product}")
    Call<Respone<Product>> updateProductWithoutThumbnail(@Path("id_product")String id,@Body Product product);
    @DELETE("/delete-product-byId/{id}")
    Call<Respone<Product>> deleteProduct(@Path("id")String id);
}
