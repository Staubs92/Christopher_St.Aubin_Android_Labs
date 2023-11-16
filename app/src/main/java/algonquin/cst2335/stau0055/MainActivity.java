package algonquin.cst2335.stau0055;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

    double latitude;
    double longitude;
    String description;
    String iconName;
    String name;
    double currentTemp;
    double minTemp;
    double maxTemp;
    int humidity;
    int visibility;
    String imageUrl;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());



      binding.getForecast.setOnClickListener( click -> {
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
                          JSONObject coord = response.getJSONObject("coord");
                          JSONArray weatherArray = response.getJSONArray("weather");
                          JSONObject position0 = weatherArray.getJSONObject(0);
                          String description = position0.getString("description");
                          String iconName = position0.getString("icon");
                          int vis = response.getInt("visibility");
                          String name = response.getString("name");
                          JSONObject mainObject = response.getJSONObject("main");
                          double current = mainObject.getDouble("temp");
                          double min = mainObject.getDouble("temp_min");
                          double max = mainObject.getDouble("temp_max");
                          int humidity = mainObject.getInt("humidity");

                          this.latitude = coord.getDouble("lat");
                          this.longitude = coord.getDouble("lon");
                          this.description = description;
                          this.iconName = iconName;
                          this.visibility = vis;
                          this.name = name;
                          this.currentTemp = current;
                          this.minTemp = min;
                          this.maxTemp = max;
                          this.humidity = humidity;


              } catch (JSONException e) {
                  throw new RuntimeException(e);
              }
                  },

                  (error) -> {});
          queue.add(request);
      });

      String pathname = getFilesDir() + "/" + iconName + ".png";
      File file = new File(pathname);
      if(file.exists()) {
          image = BitmapFactory.decodeFile(pathname);
      }
      else {
        ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/w/" + iconName + ".png", new Response.Listener>Bitmap<() {
            @Override
            public void onResponse(Bitmap bitmap) {
                try {
                    image = bitmap;
                    image.compress(bitmap.CompressFormat.PNG, 100, ActivityMain.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                } catch (Exception e) {
                }
            }
        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {});
      }

        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);

            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
}
