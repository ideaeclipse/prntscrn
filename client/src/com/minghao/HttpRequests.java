package com.minghao;


import org.json.JSONObject;
import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


/**
 * This class is used to make web requests to the backend api
 *
 * @author {CompanyName}
 */
public class HttpRequests {


    // APIBase of PrintScreen-Clone
    private final String APIBASE = "https://prntscrn-api.thiessem.ca/";

    /**
     * Open a HTTpsURLConnection
     *
     * @param url Endpoint
     * @return HttpsConnection
     * @throws IOException Unable to connect to the REST service
     */
    private HttpsURLConnection openConnection(final URL url) throws IOException {
        return (HttpsURLConnection) url.openConnection();
    }

    /**
     * Send a JSonObject to REST service
     *
     * @param url    endpoint
     * @param Object JSONObject being send to rest service
     * @return auth. token
     */
    String sendJson(final String url, final JSONObject Object) throws IOException {
        HttpsURLConnection con = openConnection(new URL(APIBASE + url));
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        OutputStream os = con.getOutputStream();
        os.write(Object.toString().getBytes(StandardCharsets.UTF_8));
        os.close();
        return printOutputStream(con.getInputStream());
    }

    /**
     * Check if existing token still valid
     *
     * @param url   endpoint
     * @param token auth. token
     * @return status code
     */
    int testToken(final String url, final String token) throws IOException {
        HttpsURLConnection con = openConnection(new URL(APIBASE + url));
        con.setRequestProperty("Authorization", token);
        con.setRequestProperty("Accept", "application/json");
        return con.getResponseCode();
    }

    String postImage(final File file, final String url, final String token) throws IOException {
        if (file.exists()) {
            String attachmentFileName = file.getName();
            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            HttpsURLConnection con = openConnection(new URL(APIBASE + url));
            con.setRequestProperty("Authorization", token);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream request = new DataOutputStream(con.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);
            request.write(Files.readAllBytes(file.toPath()));
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();
            return printOutputStream(con.getInputStream());
        }
        return null;
    }

    /**
     * Get all the version of the PrintScreen-Clone
     *
     * @param url endpoint
     * @return All the version + URL download link
     * @throws IOException Unable to connect to REST service
     */
    public String getVersion(final String url) throws IOException {
        HttpsURLConnection con = openConnection(new URL(APIBASE + url));
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        return printOutputStream(con.getInputStream());

    }

    /**
     * Get the output stream from the API
     *
     * @param inputStream InputStream from the API
     * @return Token from the rest service
     * @throws IOException Unable to read the outputStream
     */
    private String printOutputStream(final InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String string;
        StringBuilder response = new StringBuilder();
        while ((string = in.readLine()) != null) {
            response.append(string);
        }
        in.close();
        return response.toString();
    }

}