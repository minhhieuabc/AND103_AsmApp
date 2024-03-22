package com.example.asm_and103.Adapter;



import static com.example.asm_and103.Service.ApiServices.BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.asm_and103.Model.Product;

import com.example.asm_and103.R;
import com.example.asm_and103.Service.Item_Product_Handle;

import java.util.ArrayList;

public class RecycleViewProductAdapter extends RecyclerView.Adapter<RecycleViewProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> listProduct;
    private Item_Product_Handle item_product_handle;

    public RecycleViewProductAdapter(Context context, ArrayList<Product> listProduct, Item_Product_Handle item_product_handle) {
        this.context = context;
        this.listProduct = listProduct;
        this.item_product_handle = item_product_handle;
    }

    @NonNull
    @Override
    public RecycleViewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewProductAdapter.ViewHolder holder, int position) {
        Product product = listProduct.get(position);
        Glide.with(context).load(convertLocalhostToIpAddress(product.getImage())).error(R.drawable.fruit_apple).placeholder(R.drawable.fruit_apple).into(holder.imgProduct);
        holder.txtNamePro.setText("Name: " + product.getName());
        holder.txtQuanityPro.setText("Quantity: " + product.getQuantity());
        holder.txtPricePro.setText("Price: " + product.getPrice());

        holder.imgBtnDelPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_product_handle.Delete(product.getId());
            }
        });

        holder.imgBtnUpPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_product_handle.Update(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct != null ? listProduct.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView txtNamePro, txtQuanityPro, txtPricePro;
        ImageButton imgBtnUpPro, imgBtnDelPro;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgBtnUpPro = itemView.findViewById(R.id.imgBtnUpPro);
            imgBtnDelPro = itemView.findViewById(R.id.imgBtnDelPro);
            txtNamePro = itemView.findViewById(R.id.txtNameProduct);
            txtQuanityPro = itemView.findViewById(R.id.txtQuantityProduct);
            txtPricePro = itemView.findViewById(R.id.txtPriceProduct);
        }
    }

    public static final String convertLocalhostToIpAddress(String url) {
        int index = url.indexOf("3000/");
        String newUrl = "";
        if (index != -1) {
            newUrl = BASE_URL + url.substring(index + 5);
        } else {
            newUrl = url;
        }
        return newUrl;
    }
}
