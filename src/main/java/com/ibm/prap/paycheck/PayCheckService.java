package com.ibm.prap.paycheck;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.Response;

@Path("/paycheckservice")
public class PayCheckService {
	 @Path("{invoiceNo}") 
	 @GET
	  @Produces("application/json")
	  public Response getClaimStatus(@PathParam("invoiceNo") String invoiceNo) throws JSONException {
		//System.out.println("invoiceNo:- "+invoiceNo);
		ConnectionManager dbCon = new ConnectionManager();
		JSONObject jsonObject = new JSONObject(); 
		jsonObject.put("Claim Status", dbCon.checkStatus(invoiceNo));
		//jsonObject.put("Check Number", dbCon.checkStatus("ERS-4400-109091"));
		//jsonObject.put("Claim Status", dbCon.checkStatus("SSCUS022009766"));
		//String result = "@Produces(\"application/json\") \n\n Output: \n\n" + jsonObject;
		return Response.status(200).entity(jsonObject).build();
	  }
	
	/*@Path("/convertFtoCFromInput")
	@GET
	@Produces("application/json")
	public Response convertFtoCFromInput(){
		JSONObject jObj= new JSONObject();

		float celsius ;
		celsius=(45-32)*5/9;
		jObj.put("F value", 45);
		jObj.put("C Value", celsius);

		String result = "@Produces(\"application/json\") Output: \n\nF to C converter Output: \n\n"+jObj;
		return Response.status(200).entity(result).build();
	}*/  
}
