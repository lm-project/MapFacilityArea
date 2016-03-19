package com.autonavi.mapart.orm.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Qaresult;
import com.autonavi.mapart.orm.QaResultDao;

public class QaResultDaoImpl extends BasicMyBatisDao implements QaResultDao {
	
	@Override
	public Qaresult insertQaresult(Qaresult qar) {
		 super.save("qaresultMapper.insertQaResult", qar);
		 return qar;
	}

	@Override
	public List<Qaresult> getQaResultListByTaskitemid(int taskitemid)
			throws DataAccessException {
		return super.getList("qaresultMapper.getQaresultListByTaskitemid", taskitemid);
	}

	

	

}
