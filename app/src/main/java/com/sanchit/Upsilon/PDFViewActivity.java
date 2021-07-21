package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class PDFViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getStringExtra("docUrl");
        getSupportActionBar().setTitle(url);
        PDFView pdfView1 = findViewById(R.id.pdf_view);
        ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Loading file");
        loadingBar.setMessage("Please wait while the file gets loaded...");
        loadingBar.show();
//                    PDFView pdfView1 = new PDFView(context.getApplicationContext(), null);
//                    PopupWindow window = new PopupWindow(pdfView1);

        //load file
//                    builder.addContentView(pdfView1, new RelativeLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT));
//                    builder.show();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    InputStream input = null;
                    try {
                        input = new URL(url).openStream();
                        pdfView1.fromStream(input).onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                loadingBar.dismiss();

                            }
                        }).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);

    }
}