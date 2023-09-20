package algonquin.cst2335.stau0055;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import algonquin.cst2335.stau0055.databinding.ActivityMainBinding;
import algonquin.cst2335.stau0055.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( variableBinding.getRoot());

        variableBinding.textview.setText(model.editString);
        variableBinding.mybutton.setOnClickListener( vw ->
        {
            model.editString = variableBinding.myedittext.getText().toString();
            variableBinding.textview.setText("Your edit text has: " + model.editString);
        });

    }
}
