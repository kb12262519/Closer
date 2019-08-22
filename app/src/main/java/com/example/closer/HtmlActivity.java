package com.example.closer;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class HtmlActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        WebView wv = findViewById(R.id.web_view);
        wv.loadUrl("file:///android_asset/www/liar.html");
    }
}
