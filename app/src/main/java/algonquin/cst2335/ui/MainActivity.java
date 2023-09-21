package algonquin.cst2335.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.data.MainViewModel;
import algonquin.cst2335.stau0055.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
     MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( variableBinding.getRoot());

        variableBinding.myCheckBox.setOnCheckedChangeListener(
                (btn, onOrOff) -> model.onOrOff.postValue( onOrOff)
        );

        variableBinding.mySwitch.setOnCheckedChangeListener(
                (btn, onOrOff) -> model.onOrOff.postValue( onOrOff)
        );

        variableBinding.myRadioButton.setOnCheckedChangeListener(
                (btn, onOrOff) -> model.onOrOff.postValue( onOrOff)
        );

        variableBinding.myimagebutton.setOnClickListener( clk -> {
            int width = variableBinding.myimagebutton.getWidth();
            int height = variableBinding.myimagebutton.getHeight();

            String message = "the width = " + width + " and height = " + height;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

                model.onOrOff.observe(this, newValue -> {
                    variableBinding.myRadioButton.setChecked(newValue);
                    variableBinding.mySwitch.setChecked(newValue);
                    variableBinding.myCheckBox.setChecked(newValue);

                    String message = "The value is now: " + newValue;
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                });

        variableBinding.textview.setText( model.editString.getValue());
        variableBinding.mybutton.setOnClickListener( vw ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
           ;
        });

        model.editString.observe(this, newString ->
        {    variableBinding.textview.setText("Your edit text has: " + newString) ; }
        );
    }
}
