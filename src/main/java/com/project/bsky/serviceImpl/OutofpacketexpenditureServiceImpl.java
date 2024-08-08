package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.OutofpacketexpenditureMaster;
import com.project.bsky.repository.OutofpacketexpenditureRepository;
import com.project.bsky.service.OutofpacketexpenditureService;

@Service

public class OutofpacketexpenditureServiceImpl implements OutofpacketexpenditureService{
	@Autowired
	private OutofpacketexpenditureRepository outofpacketexpenditurerepo;

	@Override
	public Response savemst(OutofpacketexpenditureMaster outofpacketexpendituremaster) throws Exception {
		Response response=new Response();
		try {
			Integer count=outofpacketexpenditurerepo.checkduplicate(outofpacketexpendituremaster.getExpenditurename());
			if(count==0) {
				outofpacketexpendituremaster.setCreatedon(new Date());
				outofpacketexpendituremaster.setStatusflag(0);
				outofpacketexpenditurerepo.save(outofpacketexpendituremaster);
				response.setStatus("200");
				response.setMessage("Record Added Successfully");
			}else {
				response.setStatus("400");
				response.setMessage("Record Already Exist");
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

	@Override
	public List<Object> getexpendituremst() throws Exception {
		List<Object> list=new ArrayList<>();
		try {
			List<Object[]> objarr=outofpacketexpenditurerepo.getexpendituremst();
			for(Object[] obj:objarr) {
				Map<String,Object> response = new HashMap<>();
				response.put("expenditureId", obj[0]);
				response.put("expenditurename", obj[1]);
//				response.put("documenttype", obj[2]);
				response.put("createbyname", obj[2]);
				response.put("createtime", obj[3]);
				response.put("statusflag", obj[4]);
				list.add(response);
			}
		}catch (Exception e) {
			throw new Exception(e);
		}
		return list;
	}

	@Override
	public Response update(OutofpacketexpenditureMaster outofpacketexpendituremaster) throws Exception {
		Response response=new Response();
		try {
			OutofpacketexpenditureMaster outofpacketexpendituremaster1=outofpacketexpenditurerepo.getduplicate(outofpacketexpendituremaster.getExpenditurename());
			if(outofpacketexpendituremaster1==null) {
				outofpacketexpendituremaster1=outofpacketexpenditurerepo.findById(outofpacketexpendituremaster.getExpenditureId()).get();
				outofpacketexpendituremaster1.setExpenditurename(outofpacketexpendituremaster.getExpenditurename());
				outofpacketexpendituremaster1.setStatusflag(outofpacketexpendituremaster.getStatusflag());
				outofpacketexpendituremaster1.setUpdatedby(outofpacketexpendituremaster.getCreatedby());
				outofpacketexpendituremaster1.setUpdatedon(new Date());
				outofpacketexpenditurerepo.save(outofpacketexpendituremaster1);
				response.setStatus("200");
				response.setMessage("Record Updated Successfully");
			}else {
				if(outofpacketexpendituremaster1.getExpenditureId()==outofpacketexpendituremaster.getExpenditureId()) {
					outofpacketexpendituremaster1.setExpenditurename(outofpacketexpendituremaster.getExpenditurename());
					outofpacketexpendituremaster1.setStatusflag(outofpacketexpendituremaster.getStatusflag());
					outofpacketexpendituremaster1.setUpdatedby(outofpacketexpendituremaster.getCreatedby());
					outofpacketexpendituremaster1.setUpdatedon(new Date());
					outofpacketexpenditurerepo.save(outofpacketexpendituremaster1);
					response.setStatus("200");
					response.setMessage("Record Updated Successfully");
				}else {
					response.setStatus("400");
					response.setMessage("Record Already Exist");
				}
			}			
		}catch (Exception e) {
			throw new Exception(e);
		}
		return response;
	}

}
