package com.minghao;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SuppressWarnings("WeakerAccess")
public class HttpRequests{

    private final String APIBASE = "https://prntscrn-api.thiessem.ca/";

    private HttpsURLConnection openConnection(final URL url)throws IOException{
        return (HttpsURLConnection) url.openConnection();
    }

    public String sendJson(final String url, final JSONObject Object) throws IOException {
        HttpsURLConnection con = openConnection(new URL(APIBASE+url));
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        OutputStream os = con.getOutputStream();
        os.write(Object.toString().getBytes(StandardCharsets.UTF_8));
        os.close();

        return printOutputStream(con.getInputStream());
    }

    private String printOutputStream(final InputStream inputStream)throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String string;
        StringBuilder response = new StringBuilder();
        while ((string = in.readLine()) != null) {
            response.append(string);
        }
        in.close();
        return response.toString();
    }

    public String postImage(String fileName, String url, String token) throws IOException{
        File file = new File(fileName);
        if (file.exists()) {
            HttpsURLConnection con = openConnection(new URL(APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("Accept", "application/json");
            DataOutputStream request = new DataOutputStream(con.getOutputStream());
            request.writeBytes("Content-Disposition: form-data;file=\"" + fileName);
            request.write(Files.readAllBytes(file.toPath()));
            request.flush();
            request.close();
            return printOutputStream(con.getInputStream());
        }
        return null;
    }

}
