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
	public List<Node> selectNodesByUserid(int userid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		List<Node> list = qr.query("select * from node where userid=?", new BeanListHandler<>(Node.class), userid);
		return list;
	}

	@Override
	public int updateUseridByid(int userid, int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE node SET userid=? WHERE id=?", userid, id);
	}

	@Override
	public int insert(Node node) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update(
				"INSERT INTO `wifi_project`.`node` (`mac`, `nodeName`, `type`, `power`, `precentage`, `switchState`, `ssid`, `pw`, `userid`, `temperature`, `humidity`, `online`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				node.getMac(), node.getNodeName(), node.getType(), node.getPower(), node.getPrecentage(),
				node.getSwitchState(), node.getSsid(), node.getPw(), node.getUserid(), node.getTemperature(),
				node.getHumidity(), node.isOnline());
	}

	@Override
	public Node selectNodeByMac(String mac) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		Node node = qr.query("select * from node where mac=?", new BeanHandler<>(Node.class), mac);
		return node;
	}

	@Override
	public int updateByMac(Node node) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE node SET type=?, power=?, online=?, precentage=?, switchState=?, ssid=?, pw=? WHERE mac=?",
				node.getType(), node.getPower(), true, node.getPrecentage(), node.getSwitchState(), node.getSsid(),
				node.getPw(), node.getMac());
	}

	@Override
	public int updatePrecentageAndSwitchStateByMac(Node node) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE node SET precentage=?, switchState=? WHERE mac=?", node.getPrecentage(),
				node.getSwitchState(), node.getMac());
	}

	@Override
	public Node selectNodeById(int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		Node node = qr.query("select * from node where id=?", new BeanHandler<>(Node.class), id);
		return node;
	}

	@Override
	public int updateOnlineById(boolean b, int id) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE node SET online=? WHERE id=?", b, id);
	}

	@Override
	public int updateNodeNameById(int id, String nodeName) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		return qr.update("UPDATE node SET nodeName=? WHERE id=?", nodeName, id);
	}

	@Override
	public int[] updateUseridByid(int userid, List<Integer> idList) throws Exception {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		Object[][] params = new Object[idList.size()][];
		for (int i = 0; i < idList.size(); i++) {
			params[i] = new Object[]{0, idList.get(i)};
		}
		return qr.batch("UPDATE node SET userid=? WHERE id=?", params);
	}

	@Override
	public List<Node> selectOnlineNodesByUserid(int userid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		List<Node> list = qr.query("select * from node where userid=? and online=?", new BeanListHandler<>(Node.class), userid, true);
		return list;
	}

}
