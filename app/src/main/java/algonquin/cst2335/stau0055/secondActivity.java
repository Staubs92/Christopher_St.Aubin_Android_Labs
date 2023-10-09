package algonquin.cst2335.stau0055;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class secondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView textView = findViewById(R.id.textView3);
        textView.setText("Welcome Back: " + emailAddress);

        View callButton = findViewById(R.id.button);
        callButton.setOnClickListener(clk -> {

            EditText phoneNumberEditText = findViewById(R.id.editTextPhone);
            String phoneNumber = phoneNumberEditText.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(call);
        });

        View pictureButton = findViewById(R.id.button2);
        pictureButton.setOnClickListener(ck -> {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
             == PackageManager.PERMISSION_GRANTED) {
                 startActivity(cameraIntent);
             }

             else {
                requestPermissions( new String[] {android.Manifest.permission.CAMERA}, 8);

                ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {

                                Intent data = result.getData();
                                Bitmap thumbnail = data.getParcelableExtra("data");
                                ImageView profileImage = findViewById(R.id.imageView);
                                profileImage.setImageBitmap( thumbnail );
                            }
                        }

                    });
            cameraResult.launch(cameraIntent);
        };
    });


}}