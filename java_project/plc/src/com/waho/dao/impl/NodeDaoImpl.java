package com.waho.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.waho.dao.NodeDao;
import com.waho.domain.Node;
import com.waho.util.C3P0Utils;

public class NodeDaoImpl implements NodeDao {

	@Override
	public List<Node> selectNodesByDeviceid(int deviceid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from nodes where deviceid=?", new BeanListHandler<Node>(Node.class), deviceid);
	}

	@Override
	public Node selectNodeById(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.query("select * from nodes where id=?", new BeanHandler<Node>(Node.class), id);
	}

}
