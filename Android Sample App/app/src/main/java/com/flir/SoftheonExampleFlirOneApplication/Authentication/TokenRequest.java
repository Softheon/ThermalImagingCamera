package com.flir.SoftheonExampleFlirOneApplication.Authentication;

/**
 * The token request object
 */
public class TokenRequest {

    /**
     * The identity server endpoint
     */
    public String oAuthEndpoint;

    /**
     * The client ID
     */
    public String clientId;

    /**
     * The client secret
     */
    public String clientSecret;

    /**
     * The requested resource
     */
    public String scopes;
}
