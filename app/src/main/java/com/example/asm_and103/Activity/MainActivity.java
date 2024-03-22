package com.example.asm_and103.Activity;

import static com.example.asm_and103.Adapter.RecycleViewProductAdapter.convertLocalhostToIpAddress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.asm_and103.Adapter.RecycleViewProductAdapter;
import com.example.asm_and103.Model.Product;
import com.example.asm_and103.Model.Respone;
import com.example.asm_and103.R;
import com.example.asm_and103.Service.ApiServices;
import com.example.asm_and103.Service.HttpRequest;
import com.example.asm_and103.Service.Item_Product_Handle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecycleViewProductAdapter recycleViewProductAdapter;
    private RecyclerView rcvProduct;
    private HttpRequest httpRequest;
    private FloatingActionButton fltAdd;

    private Button btnAdd;
    private EditText edtAddName, edtAddQuantity, edtAddPrice;
    private ImageButton imgBtnAdd;

    private Dialog dialogAddProduct;

    private File file;
    private boolean typeHandle = true;

    Product productUpdate = new Product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvProduct = findViewById(R.id.rcvProduct);
        fltAdd = findViewById(R.id.fltAdd);
        httpRequest = new HttpRequest();
        httpRequest.callAPI().getListProduct().enqueue(getProductAPI);

        fltAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogAddCategory();
                typeHandle = true;
            }
        });
    }

    private void GetData(ArrayList<Product> list){
        recycleViewProductAdapter = new RecycleViewProductAdapter(this, list, new Item_Product_Handle() {
            @Override
            public void Delete(String id) {
                httpRequest.callAPI().deleteProduct(id).enqueue(delProduct);
            }

            @Override
            public void Update(Product product) {
                typeHandle = false;
                productUpdate = product;
                OpenDialogAddCategory();
            }
        });
        rcvProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcvProduct.setAdapter(recycleViewProductAdapter);
    }

    Callback<Respone<ArrayList<Product>>> getProductAPI = new Callback<Respone<ArrayList<Product>>>() {
        @Override
        public void onResponse(Call<Respone<ArrayList<Product>>> call, Response<Respone<ArrayList<Product>>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    ArrayList<Product> listPro = response.body().getData();
                    GetData(listPro);
                }
            }
        }

        @Override
        public void onFailure(Call<Respone<ArrayList<Product>>> call, Throwable t) {
            Log.e("String", t.getMessage());
        }
    };

    Callback<Respone<Product>> delProduct = new Callback<Respone<Product>>() {
        @Override
        public void onResponse(Call<Respone<Product>> call, Response<Respone<Product>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                   httpRequest.callAPI().getListProduct().enqueue(getProductAPI);
                    Toast.makeText(MainActivity.this, "Xóa thành công" , Toast.LENGTH_SHORT).show();
                }
            }
        }


        @Override
        public void onFailure(Call<Respone<Product>> call, Throwable t) {

        }
    };

    private void ChooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            file = CreateFileFormUri(imageUri, "product");
            Glide.with(MainActivity.this)
                    .load(file)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.fruit_apple)
                    .placeholder(R.drawable.fruit_apple)
                    .into(imgBtnAdd);
        }
    }
    private File CreateFileFormUri (Uri path, String name) {
        File _file = new File(MainActivity.this.getCacheDir(),name + ".png");
        try {
            InputStream in = MainActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf)) > 0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
            return _file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void OpenDialogAddCategory() {
        final View dialogView = View.inflate(this, R.layout.dialog_add_product, null);
        dialogAddProduct = new Dialog(this);

        dialogAddProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddProduct.setContentView(dialogView);

        Window window = dialogAddProduct.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAddProduct.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogAddProduct.getWindow().setAttributes(layoutParams);

        edtAddName = dialogView.findViewById(R.id.edt_AddName);
        edtAddQuantity = dialogView.findViewById(R.id.edt_AddQuantity);
        edtAddPrice = dialogView.findViewById(R.id.edt_AddPrice);
        imgBtnAdd = dialogView.findViewById(R.id.imgBtnAdd);
        btnAdd = dialogView.findViewById(R.id.btnAddPro);
        if(typeHandle == false){
            edtAddName.setText(productUpdate.getName());
            edtAddQuantity.setText(String.valueOf(productUpdate.getQuantity()));
            edtAddPrice.setText(String.valueOf(productUpdate.getPrice()));
            Glide.with(this).load(convertLocalhostToIpAddress(productUpdate.getImage())).error(R.drawable.fruit_apple).placeholder(R.drawable.fruit_apple).into(imgBtnAdd);

        }

        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtAddName.getText().toString().trim();
                String quantity = edtAddQuantity.getText().toString().trim();
                String price = edtAddPrice.getText().toString().trim();
                if (typeHandle){
                    if (file == null){
                        Toast.makeText(MainActivity.this, "Vui lòng chọn anh", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"),name);
                    RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"),quantity);
                    RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"),price);

                    MultipartBody.Part multipartBody;
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
                    multipartBody = MultipartBody.Part.createFormData("image",file.getName(),requestFile);
                    httpRequest.callAPI().addProduct(
                            _name,
                            _quantity,
                            _price,
                            multipartBody
                    ).enqueue(addProduct);
                } else {
                    productUpdate.setName(name);
                    productUpdate.setQuantity(Integer.parseInt(quantity));
                    productUpdate.setPrice(Integer.parseInt(price));

                    RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                    RequestBody _quantity = RequestBody.create(MediaType.parse("multipart/form-data"), quantity);
                    RequestBody _price = RequestBody.create(MediaType.parse("multipart/form-data"), price);

                    MultipartBody.Part multipartBody = null;
                    if (file != null) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        httpRequest.callAPI().updateProduct(
                                productUpdate.getId(),
                                _name,
                                _quantity,
                                _price,
                                multipartBody
                                ).enqueue(updateProduct);
                    } else {
                        httpRequest.callAPI().updateProductWithoutThumbnail(productUpdate.getId(), productUpdate).enqueue(updateProduct);
                    }
                }


            }
        });
        dialogAddProduct.show();
    }

    Callback<Respone<Product>> addProduct = new Callback<Respone<Product>>() {
        @Override
        public void onResponse(Call<Respone<Product>> call, Response<Respone<Product>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    httpRequest.callAPI().getListProduct().enqueue(getProductAPI);
                    Toast.makeText(MainActivity.this, "Thêm thành công" , Toast.LENGTH_SHORT).show();
                    file = null;
                    dialogAddProduct.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Respone<Product>> call, Throwable t) {

        }
    };

    Callback<Respone<Product>> updateProduct = new Callback<Respone<Product>>() {
        @Override
        public void onResponse(Call<Respone<Product>> call, Response<Respone<Product>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    httpRequest.callAPI().getListProduct().enqueue(getProductAPI);
                    Toast.makeText(MainActivity.this, "Cập nhật thành công" , Toast.LENGTH_SHORT).show();
                    file = null;
                    dialogAddProduct.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Respone<Product>> call, Throwable t) {

        }
    };
}
