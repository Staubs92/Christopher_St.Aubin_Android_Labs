package algonquin.cst2335.stau0055;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main activity for the password complexity checker app.
 * @author chris
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the  center of the screen*/
    TextView tv = null;

    /** This holds the text for the password being entered*/
    EditText et = null;

    /** This holds content for the login button*/
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         tv = findViewById(R.id.textView);
         et = findViewById(R.id.editTextTextPassword);
         btn = findViewById(R.id.loginButton);

        // Set an onClickListener for the login button
        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();

            // Check if the password meets complexity requirements and update the TextView accordingly
            if (checkPasswordComplexity(password)) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });
    }
    /**
     * Check the complexity of the provided password.
     *
     * @param password The password to be checked.
     * @return true if the password meets the complexity requirements, false otherwise.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Must contain at least 1 uppercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Must contain at least 1 lowercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Must contain at least 1 number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Must contain at least 1 special character.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    /**
     * Check if a character is a special character.
     *
     * @param c The character to be checked.
     * @return true if the character is a special character, false otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}
