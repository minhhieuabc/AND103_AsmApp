package com.example.asm_and103.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.asm_and103.R;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tạo intent để chuyển đến màn hình tiếp theo
                Intent intent = new Intent(SplashScreen.this, GetStartedScreen.class);
                startActivity(intent);

                // Đóng activity hiện tại
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}