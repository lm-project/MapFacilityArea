package com.autonavi.mapart.orm;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.autonavi.mapart.entity.Qaresult;
public interface QaResultDao {
	/***
	 * 
	 * 
	 * insert qaresult info
	 */
	Qaresult insertQaresult(Qaresult qaresult);
	
	/**
	 * 根据任务项id查询qaresult数据
	 * @param taskitemid
	 * @return
	 * @throws DataAccessException
	 */
	List<Qaresult> getQaResultListByTaskitemid(int taskitemid) throws DataAccessException;
}
