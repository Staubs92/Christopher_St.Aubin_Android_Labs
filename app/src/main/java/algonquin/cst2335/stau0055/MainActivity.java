package algonquin.cst2335.stau0055;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        EditText emailText = findViewById(R.id.emailEditText);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String nameFromFile = prefs.getString("EmailAddress", "");
        emailText.setText(nameFromFile);

        View button = findViewById(R.id.loginButton);


        button.setOnClickListener(clk-> {

            SharedPreferences.Editor editor = prefs.edit();
            EditText emailEditText = findViewById(R.id.emailEditText);
            String emailAddress = emailEditText.getText().toString();

            Intent nextPage = new Intent( MainActivity.this, secondActivity.class);
            nextPage.putExtra("EmailAddress", emailAddress);
            startActivity( nextPage);

            editor.putString("EmailAddress", emailAddress);
            editor.apply();

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onStart() - The application is now visible on screen." );



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onResume() - The application is now responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onPause() - The application no longer responds to user input" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onStop() - The application is no longer visible." );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onDestroy() - Any memory used by the application is freed." );
    }
}