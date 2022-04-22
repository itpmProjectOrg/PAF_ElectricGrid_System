package com.paf.BillingService.resource;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.paf.BillingService.model.*;

@Path("/bills")
public class BillResource {
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Bill> getBills(Bill b){
		
		String accountNo = b.getAccountNo();
		List<Bill> billList = BillRepository.getBills(accountNo);
		return billList;
		
	}
	
	@GET
	@Path("/{year}-{month}")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Bill getBillsBasedMonthYear(Bill b, @PathParam("year") String year, @PathParam("month") String month){
		
		String accountNo = b.getAccountNo();
		Bill mybill = BillRepository.getBillBasedMonthYear(accountNo, month, year);
		return mybill;
	}
	
	@POST
	@Consumes ({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Bill addNewBill(@FormParam("accountNo") String accountNo, @FormParam("month") String month, @FormParam("year") String year, @FormParam("consumeUnits") int consumeUnits) {
		
		Bill mybill = BillRepository.addNewBill(accountNo, month, year, consumeUnits);
		return mybill;
	}
	
	@PUT
	@Consumes ({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Bill updateBill(@FormParam("billID") int billID,@FormParam("accountNo") String accountNo, @FormParam("month") String month, @FormParam("year") String year, @FormParam("consumeUnits") int consumeUnits) {
		
		Bill mybill = BillRepository.updateBill(billID, accountNo, month, year, consumeUnits);
		return mybill;
	}
	
	@DELETE
	@Consumes ({MediaType.APPLICATION_JSON})
	@Produces (MediaType.TEXT_PLAIN)
	public String deleteBill(Bill b) {
		
		int billID = b.getBillID();
		String output = BillRepository.deleteBill(billID);
		return output;
	}
}
