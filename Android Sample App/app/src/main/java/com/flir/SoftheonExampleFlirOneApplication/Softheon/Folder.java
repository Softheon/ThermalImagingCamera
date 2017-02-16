package com.flir.SoftheonExampleFlirOneApplication.Softheon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flir.SoftheonExampleFlirOneApplication.JSON.ThermalImageReadingResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Softheon Folder
 */
public class Folder {

    //The folder id
    @JsonIgnore
    public int folderId;

    //The folder name
    @JsonProperty("Name")
    public String folderName;

    //The folder type
    @JsonProperty("Type")
    public int folderType;

    @JsonProperty("Profiles")
    public Profile[] profiles;

    @JsonProperty("Acl")
    final public int acl = -1;

    @JsonProperty("State")
    final public int state = 0;

    public Folder() {

    }

    public Folder(int folderId, String folderName, int folderType) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderType = folderType;
    }

    public Integer upload(String endPoint, String accessToken) {
        //Initialize the HTTP URL Connection
        HttpURLConnection conn = null;

        try {
            //Create the URL object
            URL url = new URL(endPoint);
            //Initialize the http url connection
            conn = (HttpURLConnection) url.openConnection();
            //Set http request to POST
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //Set content type of post body to url query string
            conn.setRequestProperty("Content-Type", "application/json");
            //Set the authorization in the header to Basic
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(this);
            //Initialize an output stream and execute the HTTP POST
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            //Make sure we got a HTTP 200 Status Code back. If not something went wrong, print the status code
            if(conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
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

            return mapper.readValue(builder.toString(), ThermalImageReadingResponse.class).getFolderID();

        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return null;
    }
}
