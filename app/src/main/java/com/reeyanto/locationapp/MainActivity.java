package com.reeyanto.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.reeyanto.locationapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int locationRequestCode = 3000;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding.btnGetLocation.setOnClickListener(view -> {
            getCurrentLocation();
        });
    }

    private void getCurrentLocation() {
        // cek permission
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, locationRequestCode);
            }
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                binding.tvResult.setText(String.valueOf(location.getLatitude()));
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationRequestCode) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Akses lokasi ditolak!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}