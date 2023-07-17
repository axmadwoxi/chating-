package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText message;
    ImageView send;
    List<Message_model> list;
    MessageAdapter adapter;
    public static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        message = findViewById(R.id.MASSEG);
        send = findViewById(R.id.imageView6);
        list = new ArrayList<>();
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        LayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queston = message.getText().toString();
                if (queston.isEmpty()) {
                    Toast.makeText(MainActivity.this, "wrihte something", Toast.LENGTH_SHORT).show();
                } else {
                    addTochat(queston, Message_model.SENT_BY_ME);
                    message.setText("");
                    callAPI(queston);
                }
            }
        });
    }

    private void callAPI(String queston) {
        list.add(new Message_model("Typinge..", Message_model.SENT_BY_Bot));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", queston);
            jsonObject.put("max_tokens", 6000);
            jsonObject.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(response.body().toString());
                        JSONArray jsonArray=jsonObject1.getJSONArray("choces");
                        String ressult=jsonArray.getJSONObject(0).getString("text");
                        addResponse(ressult.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
                else {
                    addResponse("failed to load" + response.body().toString());
                }
            }
        });
    }

    private void addResponse(String s) {

        list.remove(list.size() - 1);
        addTochat(s, Message_model.SENT_BY_Bot);
    }

    private void addTochat(String queston, String sentByMe) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.add(new Message_model(queston, sentByMe));
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

}