package com.flir.SoftheonExampleFlirOneApplication.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The Token Response object
 */
public class TokenResponse {

    /**
     * The access token
     */
    @JsonProperty("access_token")
    public String accessToken;

    /**
     * When the token expires
     */
    @JsonProperty("expires_in")
    public String expiresIn;

    /**
     * The token type
     */
    @JsonProperty("token_type")
    public String tokenType;
}
