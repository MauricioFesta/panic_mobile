package com.stock.panic.ui.camera;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.stock.panic.R;
import com.stock.panic.data.model.CameraSql;
import com.stock.panic.utils.TokenRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class BarcodeScanActivity extends AppCompatActivity {

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static BarcodeScanner scanner;
    private static EditText numberCode;
    private static Button btnEstoque;
    private SqLite sql = null;
    private CameraSql cameraSql;
    private TokenRequest tokenRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_bars_scan);

        Button btn = findViewById(R.id.button2);
        btnEstoque = findViewById(R.id.button3);
        numberCode = findViewById(R.id.editTextNumber);

        btnEstoque.setVisibility(View.INVISIBLE);

        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                    Barcode.FORMAT_CODE_128)
                        .build();
        scanner = BarcodeScanning.getClient(options);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasCameraPermission()) {
                    startCamera();
                } else {
                    requestPermission();
                }
            }
        });

        btnEstoque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendNumberProduct sendestoque = new SendNumberProduct(numberCode.getText().toString(), getApplicationContext());
                sendestoque.send();

            }
        });

        numberCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    btnEstoque.setVisibility(View.VISIBLE);
                }else{
                    btnEstoque.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private Executor executor = Executors.newSingleThreadExecutor();
    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        PreviewView previewView = findViewById(R.id.preview_view);

        Preview preview = new Preview.Builder()
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ViewPort viewPort = previewView.getViewPort();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        imageAnalysis.setAnalyzer(executor,analyzer());

        ImageCapture imageCapture = new ImageCapture.Builder().build();

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll();

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture,imageAnalysis
            );

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error to open back camera", Toast.LENGTH_LONG).show();
        }


    }

    public ImageAnalysis.Analyzer analyzer(){

        return new ImageAnalysis.Analyzer() {

            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {

                @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();

                InputImage image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                Task<List<Barcode>> result = scanner.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                            @Override
                            public void onSuccess(List<Barcode> barcodes) {

                                if(!barcodes.isEmpty()){

                                    try {
                                        TimeUnit.SECONDS.sleep(1);
                                        btnEstoque.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "Codígo lido: " + barcodes.get(0).getRawValue(), Toast.LENGTH_LONG).show();
                                        clearBarcodeInput();
                                        numberCode.append(barcodes.get(0).getRawValue());


                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                imageProxy.close();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Erro on read barcode...", Toast.LENGTH_LONG).show();
                                imageProxy.close();
                            }
                        });
            }

        };

    }

    public void clearBarcodeInput(){

        numberCode.getText().clear();

    }


}
