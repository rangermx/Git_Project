package com.waho.service.impl;

import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.service.NodeService;

public class NodeServiceImpl implements NodeService {

	@Override
	public void setNodeOfflineById(int id) {
		NodeDao nodeDao = new NodeDaoImpl();
		try {
			nodeDao.updateOnlineById(false, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
