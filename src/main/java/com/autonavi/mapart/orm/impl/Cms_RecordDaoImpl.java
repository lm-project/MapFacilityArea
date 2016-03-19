package com.autonavi.mapart.orm.impl;

import com.autonavi.mapart.entity.Cms_Record;
import com.autonavi.mapart.orm.Cms_RecordDao;

public class Cms_RecordDaoImpl extends BasicMyBatisDao implements Cms_RecordDao {

	@Override
	public void insertCmsRecord(Cms_Record cmsdata) {
		super.save("Cms_RecordMapper.insert", cmsdata);
	}

}
