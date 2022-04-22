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

}
