package com.example.jsonresponse;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ListView listView = findViewById(R.id.listView);

        ArrayList<String> arrNames = new ArrayList<>();
        String url = "https://jsonplaceholder.typicode.com/users"; // GET API
        AndroidNetworking.initialize(this);
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RES", response.toString());

                        //parsing
                        try {
                            for (int i = 0 ; i < response.length(); i++) {
                                JSONObject objResult = response.getJSONObject(i);
                                String name = objResult.getString("name");
                                arrNames.add(name);
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrNames);
                                listView.setAdapter(arrayAdapter);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Log.e("ERROR", anError.toString());
                    }
                });

        //POST API
        AndroidNetworking.post("https://postman-echo.com/post")
                .addBodyParameter("headers")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Res:" , response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR: ", anError.toString());
                    }
                });
    }
}