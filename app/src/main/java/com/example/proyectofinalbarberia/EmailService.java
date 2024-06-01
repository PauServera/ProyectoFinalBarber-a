package com.example.proyectofinalbarberia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.util.Log;



import java.io.IOException;

public class EmailService {

    private static final String API_KEY = "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";
    private static final String SENDGRID_API_URL = "https://api.sendgrid.com/v3/mail/send";

    public static void sendEmail(String toEmail, String subject, String content) {
        OkHttpClient client = new OkHttpClient();

        JSONObject email = new JSONObject();
        try {
            email.put("personalizations", new JSONArray()
                    .put(new JSONObject()
                            .put("to", new JSONArray()
                                    .put(new JSONObject()
                                            .put("email", toEmail)))
                            .put("subject", subject)));
            email.put("from", new JSONObject()
                    .put("email", "pauservera88@gmail.com"));
            email.put("content", new JSONArray()
                    .put(new JSONObject()
                            .put("type", "text/plain")
                            .put("value", content)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(email.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(SENDGRID_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("EmailService", "Error sending email", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("EmailService", "Error sending email: " + response);
                } else {
                    Log.d("EmailService", "Email sent successfully");
                }
            }
        });
    }
}




