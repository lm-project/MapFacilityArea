package com.autonavi.mapart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.orm.QaResultDao;
import com.autonavi.mapart.service.QaResultService;

@Service
public class QaResultServiceImpl implements QaResultService {
	
	@Autowired
	private QaResultDao qaresultdao;
	
	@Override
	public Qaresult insertQaResult(Qaresult qaresult) {
		return qaresultdao.insertQaresult(qaresult);
	}

}
