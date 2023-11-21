package algonquin.cst2335.stau0055;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import algonquin.cst2335.stau0055.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    protected String cityName;
    RequestQueue queue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        binding.getForecast.setOnClickListener(click -> {
            cityName = binding.editText.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONArray weather = response.getJSONArray("weather");
                            JSONObject data = response.getJSONObject("main");

                            binding.temp.setText("The current temperature is: " + data.getDouble("temp"));
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.maxTemp.setText("the maximum temperature is: " + data.getDouble("temp_max"));
                            binding.maxTemp.setVisibility(View.VISIBLE);
                            binding.minTemp.setText("The minimum temperature is: " + data.getDouble("temp_min"));
                            binding.minTemp.setVisibility(View.VISIBLE);
                            binding.humitidy.setText("The humidity is: " + data.getDouble("humidity") + "%");
                            binding.humitidy.setVisibility(View.VISIBLE);
                            binding.description.setText(weather.getJSONObject(0).getString("description"));
                            binding.description.setVisibility(View.VISIBLE);

                            String iconName = weather.getJSONObject(0).getString("icon");

                            String imgUrl = "http://openweathermap.org/img/w/" + iconName + ".png";

                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if (file.exists()) {
                                binding.icon.setImageBitmap(BitmapFactory.decodeFile(pathname));
                            } else {
                                ImageRequest imgReq = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        binding.icon.setImageBitmap(bitmap);
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    int i = 0;
                                });
                                queue.add(imgReq);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
int i = 0;
                    }
            );

            queue.add(request);
        });
    }
}


