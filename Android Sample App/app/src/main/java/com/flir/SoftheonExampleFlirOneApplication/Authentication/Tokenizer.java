package com.flir.SoftheonExampleFlirOneApplication.Authentication;

import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Requests an access token from the Identity Server
 */
public class Tokenizer {

    /**
     * Gets an access token from the identity server
     * @param endPoint - The URL to the Identity Server
     * @param clientId - The Client ID
     * @param clientSecret - The Client Secret
     * @param scope - The requested resource access
     * @return - The Access Token
     */
    public static String getToken(String endPoint, String clientId, String clientSecret, String scope) {
        TokenRequest request = new TokenRequest();
        request.oAuthEndpoint = endPoint;
        request.clientId = clientId;
        request.clientSecret = clientSecret;
        request.scopes = scope;

        TokenResponse response = Tokenizer.requestAccessToken(request);

        return response.accessToken;
    }

    /**
     * Requests an access token from the identity server
     * @param tokenRequest - The token request
     * @return - The token response object
     */
    private static TokenResponse requestAccessToken(TokenRequest tokenRequest) {
        //Initialize the HTTP URL Connection
        HttpURLConnection conn = null;
        TokenResponse response = null;

        try {
            //Create the URL object from the identity server endpoint
            URL url = new URL(tokenRequest.oAuthEndpoint);
            //Initialize the http url connection
            conn = (HttpURLConnection) url.openConnection();
            //Set http request to POST
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //Set content type of post body to url query string
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //Set the authorization in the header to Basic
            conn.setRequestProperty("Authorization", "Basic " + encodeClientCredentials(tokenRequest.clientId, tokenRequest.clientSecret));
            //Create the post body message which will include the resource we are trying to get access too
            String queryString = "grant_type=client_credentials&scope=" + tokenRequest.scopes;

            //Initialize an output stream and execute the HTTP POST
            OutputStream os = conn.getOutputStream();
            os.write(queryString.getBytes());
            os.flush();

            //Make sure we got a HTTP 200 Status Code back. If not something went wrong, print the status code
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            //Initialize a buffered reader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String output;
            while((output = br.readLine()) != null) {
                builder.append(output);
            }

            ObjectMapper mapper = new ObjectMapper();

            response = mapper.readValue(builder.toString(), TokenResponse.class);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return response;
    }

    private static String encodeClientCredentials(String clientId, String clientSecret) throws UnsupportedEncodingException {
        String clientCredentials = clientId + ":" + clientSecret;

        byte[] bytesEncoded = clientCredentials.getBytes("UTF-8");
        return Base64.encodeToString(bytesEncoded, Base64.DEFAULT);
    }
}
