package org.TSwork;

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

@WebSocket
public class WebSocketConnection{
	//This Logger will record incoming/outgoing messages
	private static final Logger LOG = Log.getLogger(WebSocketConnection.class);
    
    public static void main(String[] args) throws JSONException{
    	//add the URL to connection
        String url = "wss://va.msg.liveperson.net/ws_api/account/13350576/messaging/consumer/"+getJWT()+"?v=2";

        //Set the SSL 
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setTrustAll(true);

        WebSocketClient client = new WebSocketClient(sslContextFactory);
        try{
            client.start(); //Initialize the connection
            WebSocketConnection socket = new WebSocketConnection();
            Future<Session> fut = client.connect(socket,URI.create(url)); //Set the URI
            Session session = fut.get();
            //Send Request to "chat" using Messaging
            session.getRemote().sendString("{\"kind\":\"req\",\"id\":1,\"type\":\".ams.cm.ConsumerRequestConversation\",\"body\":{\"brandId\":\"13350576\",\"channelType\":\"MESSAGING\"}}");
        }
        catch (Throwable e){
            LOG.warn(e);
        }
    }
//Getting the JWT to create the Web Socket
    public static String getJWT() throws JSONException{
    	//Creating the Oauth Service with Credentials
		OAuthService service = new ServiceBuilder()
			 	.provider(LPAPI.class)
                .apiKey("{API Key}")
                .apiSecret("{API Secret}")
                .build();
		 Token accessToken = new Token("{Access Token}","{Access Token Secret}");
 		
 		//Making the POST request 
 		OAuthRequest request = new OAuthRequest(Verb.POST, "https://va.idp.liveperson.net/api/account/13350576/signup");
		request.addHeader("Content-Type", "application/json");
		service.signRequest(accessToken, request); 
		Response response = request.send();
		
		//Parse the response
		JSONObject jsonObject = new JSONObject(response.getBody());
		String JWT = jsonObject.getString("jwt");
		
		System.out.println(jsonObject.getString("jwt"));
     	return JWT;
    }
    @OnWebSocketConnect
    public void onConnect(Session session){
    	System.out.println("Connected!");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason){
        System.out.println("Closed");
        LOG.info("onClose({}, {})", statusCode, reason);
    }

    @OnWebSocketError
    public void onError(Throwable cause){
        LOG.warn(cause);
    }

    @OnWebSocketMessage
    public void onMessage(String msg){
        LOG.info("Message: {}", msg);
    	System.out.println("Message Received");
    }
}