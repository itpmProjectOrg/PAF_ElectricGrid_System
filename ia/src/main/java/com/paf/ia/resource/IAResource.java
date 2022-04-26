package com.paf.ia.resource;
import javax.ws.rs.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paf.ia.model.IARepo;

@Path("/alerts")
public class IAResource {
	IARepo ia = new IARepo();
	
	@GET 
	@Path("/")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.TEXT_HTML})
	public String readAlerts() {
		return ia.readAlerts();
		}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	@Produces(MediaType.TEXT_PLAIN)
	public String insertAlerts(@FormParam("alertDescription")String dis,@FormParam("scheduleTime") String time,@FormParam("scheduleDate")String date) {
			String output = ia.insertAlerts(dis, time, date);
			return output;
		
	}
	
	@PUT
	@Path("/") 
	@Consumes(MediaType.APPLICATION_JSON) 
	@Produces(MediaType.TEXT_PLAIN)
	public String updateAlert(String itemData) {
	
	//Convert the input string to a JSON object
	JsonObject itemObject = new JsonParser().parse(itemData).getAsJsonObject();
	//Read the values from the JSON object
	String itemID = itemObject.get("alertID").getAsString();
	String itemCode = itemObject.get("alertDescription").getAsString(); 
	String itemName = itemObject.get("scheduleTime").getAsString(); 
	String itemDesc = itemObject.get("scheduleDate").getAsString();
	String output = ia.updateAlert(itemID, itemCode, itemName, itemDesc); 
	return output;
	}
	
	@DELETE
	@Path("/") 
	@Consumes(MediaType.APPLICATION_XML) 
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteAlert(String itemData) {
	
	//Convert the input string to an XML document
	Document doc = Jsoup.parse(itemData, "", Parser.xmlParser()); //Read the value from the element <itemID>
	String itemID = doc.select("alertID").text();
	String output = ia.deleteAlert(itemID); 
	return output;
	}

}
