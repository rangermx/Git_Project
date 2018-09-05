package com.waho.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.waho.dao.NodeDao;
import com.waho.domain.Device;
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

	@Override
	public int insert(Node node) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update(
				"INSERT INTO nodes (`deviceid`, `deviceMac`, `nodeAddr`, `nodeName`, `signal`, `relayLevel`, `agreement`, `Phase`) VALUES (?,?,?,?,?,?,?,?)",
				node.getDeviceid(), node.getDeviceMac(), node.getNodeAddr(), node.getNodeName(), node.getSignal(),
				node.getRelayLevel(), node.getAgreement(), node.getPhase());
	}

	@Override
	public int insert(List<Node> list) throws Exception {
		Object[][] params = new Object[list.size()][8];
		for (int i = 0; i < list.size(); i++) {
			params[i][0] = list.get(i).getDeviceid();
			params[i][1] = list.get(i).getDeviceMac();
			params[i][2] = list.get(i).getNodeAddr();
			params[i][3] = list.get(i).getNodeName();
			params[i][4] = list.get(i).getSignal();
			params[i][5] = list.get(i).getRelayLevel();
			params[i][6] = list.get(i).getAgreement();
			params[i][7] = list.get(i).getPhase();
		}

		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		int[] result = qr.batch(
				"INSERT INTO nodes (`deviceid`, `deviceMac`, `nodeAddr`, `nodeName`, `signal`, `relayLevel`, `agreement`, `Phase`) VALUES (?,?,?,?,?,?,?,?)",
				params);
		int sum = 0;
		for (int i : result) {
			sum += i;
		}
		return sum;
	}

	@Override
	public int deletNodesByDevice(Device device) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("delete from nodes where deviceMac=?", device.getDeviceMac());
	}

}
