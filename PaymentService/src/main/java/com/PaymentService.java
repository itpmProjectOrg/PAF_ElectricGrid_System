package com;

import model.Payment;

//For REST Service
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//For JSON
import com.google.gson.*;
//For XML
import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.Document;

@Path("/Payment")
public class PaymentService {

	Payment PaymentObj = new Payment();
	
	
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readPaymentDetails(){
	    return PaymentObj.readPaymentDetails();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertPayment(@FormParam("billID") String billID,
	@FormParam("paymentAmount") String paymentAmount,
	@FormParam("creditCardType") String creditCardType,
	@FormParam("cardNo") String cardNo,
	@FormParam("expirationalMonth") String expirationalMonth,
    @FormParam("expirationalYear") String expirationalYear,
    @FormParam("cvn") String cvn)
	{
	String output = PaymentObj.insertPayment(billID, paymentAmount, creditCardType, cardNo, expirationalMonth, expirationalYear, cvn);
	return output;
	}
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePayment(String paymentData){
	    //Convert the input string to a JSON object
	    JsonObject paymentObject = new JsonParser().parse(paymentData).getAsJsonObject();
	
	    //Read the values from the JSON object
	    String paymentID = paymentObject.get("paymentID").getAsString();
	    String billID = paymentObject.get("billID").getAsString();
	    String paymentAmount = paymentObject.get("paymentAmount").getAsString();
	    String creditCardType = paymentObject.get("creditCardType").getAsString();
	    String cardNo = paymentObject.get("cardNo").getAsString();
	    String expirationalMonth = paymentObject.get("expirationalMonth").getAsString();
	    String expirationalYear = paymentObject.get("expirationalYear").getAsString();   
	    String cvn = paymentObject.get("cvn").getAsString();   
	    String output = PaymentObj.updatePayment(paymentID, billID, paymentAmount, creditCardType, cardNo, expirationalMonth, expirationalYear, cvn);
	
	    return output;
	}
	
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String deletePaymentDetails(String paymentData){
	   //Convert the input string to an XML document
	   Document doc = Jsoup.parse(paymentData, "", Parser.xmlParser());
	
	   //Read the value from the element <paymentID>
	   String paymentID = doc.select("paymentID").text();
	   String output = PaymentObj.deletePaymentDetails(paymentID);
	   
	   return output;
	}
}
