package com.example.android.notesapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.notesapp.R;
import com.github.barteksc.pdfviewer.PDFView;

public class display_pdf extends AppCompatActivity {
    int PDF_SELECTION_CODE = 99;
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show();
        Intent browseStorage = new Intent(Intent.ACTION_GET_CONTENT);
        browseStorage.setType("application/pdf");
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedPdfFromStorage = data.getData();
            showPdfFromUri(selectedPdfFromStorage);
        }
    }
    private void showPdfFromUri(Uri uri) {
        pdfView.fromUri(uri)
                .defaultPage(0)
                .spacing(10)
                .load();
    }
}
