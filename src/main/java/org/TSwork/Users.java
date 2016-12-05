package org.TSwork;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Users {
	public static void main(String[] args){
		//Creating the Oauth service with my credentials.
		OAuthService service = new ServiceBuilder()
			 	.provider(LPAPI.class)
                .apiKey("{API Key}")
                .apiSecret("{API Secret}")
                .build();
		 Token accessToken = new Token("{Access Token}","{Access Token Secret}");
		 
//User List via an OauthRequest
		 OAuthRequest request = new OAuthRequest(Verb.GET, "https://va.ac.liveperson.net/api/account/13350576/configuration/le-users/users");
		 request.addHeader("Content-Type", "application/json");
		 
		 // sign the request
		 service.signRequest(accessToken, request); 
		 Response response = request.send();
		 // print the response to the console
		 System.out.println(response.getMessage());
		 System.out.println(response.getCode());
		 System.out.println(response.getBody());
		 
		 
//Skills List via an OauthRequest
		 request = new OAuthRequest(Verb.GET, "https://va.ac.liveperson.net/api/account/13350576/configuration/le-users/skills");
		 // sign the request
		 service.signRequest(accessToken, request); 
		 response = request.send();
		 // print the response to the console
		 System.out.println(response.getMessage());
		 System.out.println(response.getCode());
		 System.out.println(response.getBody());
	 }

}