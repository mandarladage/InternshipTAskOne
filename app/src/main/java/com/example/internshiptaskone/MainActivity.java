package com.example.internshiptaskone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import org.xmlpull.v1.XmlPullParser;

import java.io.File;

import dev.abhishekkumar.canvasview.CanvasView;

public class MainActivity extends AppCompatActivity {

    CanvasView canvas;
    Button button1, button2;
    Bitmap bitmap;
    EditText editText;
    AlertDialog.Builder builder;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvas = findViewById(R.id.canvasView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);


        editText = new EditText(MainActivity.this);
        builder = new AlertDialog.Builder(MainActivity.this);


        builder.setTitle("The extracted text is:");
        builder.setCancelable(false);




        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });




        canvas.setColorMarker(R.color.colorPaint);
        canvas.setStrokeWidth(12f);


    }




    public void Scan(View view) {
        bitmap = canvas.getBitmap();


        EditText input = new EditText(MainActivity.this);
       

        //1.create a FirebaseVisionImage object fro Bitmap object
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        //2.Get an instance of FirebaseVision
        FirebaseVision firebaseVision = FirebaseVision.getInstance();

        //3. Createan instance of FIrebaseVIsionTExtREcognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();

        //4.create task to process image
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(image);

        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String text = firebaseVisionText.getText();
                input.setText(text);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Processing Image Failure",Toast.LENGTH_LONG).show();
            }
        });






        builder.setView(input);
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void Reset(View view) {
        canvas.clearView();
    }


}