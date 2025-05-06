package com.skedlease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import org.json.JSONObject;

public class LoginController {

    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;

    @FXML
    private CheckBox isAdmin;

    private Alert successAlert;
    private Alert loggingInAlert;
    private Alert failureAlert;


    public void createAlerts()
    {
        successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Login status");
        successAlert.setHeaderText("Login status");
        successAlert.setContentText("Login successful");

        loggingInAlert = new Alert(Alert.AlertType.INFORMATION);
        loggingInAlert.setTitle("Login status");
        loggingInAlert.setHeaderText("Login status");
        loggingInAlert.setContentText("Logging in...");

        failureAlert = new Alert(Alert.AlertType.ERROR);
        failureAlert.setTitle("Login status");
        failureAlert.setHeaderText("Login status");
        failureAlert.setContentText("Login unsuccessful");
    }

    

    public void submit(ActionEvent e) {
        System.out.println("Logging in...");

        String token = App.getCSRFToken(); 
        OkHttpClient client = new OkHttpClient();
        createAlerts();

        try {
            JSONObject json = new JSONObject();
            json.put("username", username.getText()); 
            json.put("password", password.getText());  

            MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(json.toString(), JSON_TYPE);

            Request request = new Request.Builder()
                .url("https://skedlease.onrender.com/user/login/")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-CSRFToken", token)
                .post(body)
                .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            if (response.isSuccessful()) {

                JSONObject responseJSON = new JSONObject(response.body().string());
                String role = responseJSON.getJSONObject("user").getString("role");

                if(role.equals("admin"))
                {
                    App.setRoot("admin");
                } else if(role.equals("doctor"))
                {

                } else
                {
                    App.setRoot("primary");
                }
                    
            } else {
                System.out.println("Status: " + response.code());
                String responseText = response.body().string();
                System.out.println("Response: " + responseText);
            }

        } catch (IOException | org.json.JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void register(ActionEvent e) throws IOException
    {
        App.setRoot("register");
    }

}
