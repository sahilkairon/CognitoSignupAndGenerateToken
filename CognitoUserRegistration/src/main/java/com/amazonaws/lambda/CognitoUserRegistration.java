package com.amazonaws.lambda;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CognitoUserRegistration implements RequestHandler<APIGatewayProxyRequest, APIGatewayProxyResponse> {
	private String AccessKey = "AKIAYUCMZEDM5Q3UW7ZH";
	private String SecretKey = "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";

	private String AppClientId = "6t6iamdp92fp3mbrpfua79rtq1";
	private String userName = "demo@gmail.com";
	private String password = "Test@123";

	APIGatewayProxyResponse response = new APIGatewayProxyResponse();

	@Override
	public APIGatewayProxyResponse handleRequest(APIGatewayProxyRequest input, Context context) {
		context.getLogger().log("Input: " + input);

		try {
			AWSCredentials cred = new BasicAWSCredentials(AccessKey, SecretKey);
			AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
			AWSCognitoIdentityProvider client = AWSCognitoIdentityProviderClientBuilder.standard()
					.withCredentials(credProvider).withRegion(Regions.AP_SOUTH_1).build();
			context.getLogger().log("Cognito Client created !");
			
//			AttributeType [] attribute = new AttributeType[1];
//			
//			AttributeType email = new AttributeType();
//			email.setName("email");
//			email.setValue("test2@gmail.com");
//			
//			attribute[0] = email;
//			
//			
//			
//
//			SignUpRequest request = new SignUpRequest().withClientId(AppClientId).withUsername(userName)
//					.withPassword(password).withUserAttributes(attribute);
//					
//			context.getLogger().log("request for signup is setUp");
//
//			SignUpResult result = client.signUp(request);
//			context.getLogger().log("user signed up successfully !");
			
//			Generating token for logged in user 
			
			 Map<String, String> authParams = new LinkedHashMap<String, String>() {{
		            put("USERNAME",  userName);
		            put("PASSWORD", password);
		        }};
		        
		        context.getLogger().log("auth parameters set up in Map");
			
		     AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest().withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).
		    		 withUserPoolId("ap-south-1_yrtfbVZ3h").withClientId(AppClientId).withAuthParameters(authParams);
		     
		     context.getLogger().log("authentication client created");
		     
		     AdminInitiateAuthResult authResult = client.adminInitiateAuth(authRequest);
		        AuthenticationResultType resultType = authResult.getAuthenticationResult();
		        
		        context.getLogger().log("authentication request successfully sent !");
		        
		        Map<String, String> tokens = new LinkedHashMap<String, String>();
		        
		        tokens.put("accessToken", resultType.getAccessToken());
		        
		        
			response.setBody(tokens.get("accessToken"));
			response.setStatusCode(200);

			return response;
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			response.setBody(e.getMessage());
			response.setStatusCode(400);
			return response;
		}
	}

}
