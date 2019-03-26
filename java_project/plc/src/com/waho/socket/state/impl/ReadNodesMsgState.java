package com.waho.socket.state.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.waho.dao.NodeDao;
import com.waho.dao.impl.NodeDaoImpl;
import com.waho.domain.Device;
import com.waho.domain.Node;
import com.waho.domain.SocketCommand;
import com.waho.socket.state.SocketState;
import com.waho.socket.util.SocketDataHandler;

/**
 * 查询节点信息状态
 * 查询完成所有节点信息后，跳转空闲状态
 * @author mingxin
 *
 */
public class ReadNodesMsgState implements SocketState {

	private Logger logger = Logger.getLogger(this.getClass());

	private int count;
	private int index;
	private int num;

	public ReadNodesMsgState() {
		super();
		this.index = 1;
		this.num = 15;
	}

	@Override
	public SocketState clientDataHandle(byte[] data, int length, SocketDataHandler handler, Device device,
			OutputStream out) {
		SocketCommand sc = SocketCommand.parseSocketCommand(data, length);
		System.out.println(sc);
		if (sc != null && (byte) sc.getCommand() == SocketCommand.CMD_GET_NODE_MSG_REP) {// 如果是读节点信息回复
			try {
				// 第一步、将数据切割
				byte node_num = sc.getData()[0];
				byte max_num = sc.getData()[2];
				byte min = node_num > max_num ? max_num : node_num;
				if (min != 0 && sc.getData().length >= 10) {
					byte[][] msg = new byte[min][8];
					for (int i = 0; i < min; i++) {
						System.arraycopy(sc.getData(), 3 + i * 8, msg[i], 0, 8);
					}
					// 第二步、封装到对象
					Node node = null;
					LinkedList<Node> list = new LinkedList<>();
					for (byte[] bs : msg) {
						node = new Node();
						node.setDeviceid(device.getId());
						node.setDeviceMac(device.getDeviceMac());
						String temp = SocketCommand.parseBytesToHexString(bs, bs.length);
						node.setNodeAddr(temp.substring(0, 6 * 2));
						node.setNodeName(node.getNodeAddr());
						node.setSignal(Integer.parseInt(temp.substring(12, 13), 16));
						node.setRelayLevel(Integer.parseInt(temp.substring(13, 14), 16));
						node.setAgreement(Integer.parseInt(temp.substring(14, 15), 16));
						node.setPhase(Integer.parseInt(temp.substring(15, 16), 16));
						list.add(node);
					}
					// 第三步、写入数据库
					NodeDao nodeDao = new NodeDaoImpl();
					nodeDao.insert(list);
					// 第四步、跳转状态
					this.index += this.num;
					if (this.index >= device.getCurrentNodes()) {
						return IdleState.getInstance();
					} else {
						// 不跳转
					}
				} else {// 小于10，说明无节点信息
					this.index += this.num;
					if (this.index >= device.getCurrentNodes()) {
						return IdleState.getInstance();
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 其他指令一概不处理

		}
		return null;
	}

	@Override
	public void userMsgHanle(Device device, OutputStream out) {
		if (this.count % 10 == 2) {// 每调用10次执行一次，每次调用间隔一个线程周期（1s）
			SocketCommand cmd = new SocketCommand();
			cmd.setCommand(SocketCommand.CMD_GET_NODE_MSG);
			cmd.setData(SocketCommand.generateGeNodeMsgCommandData(index, num));
			try {
				out.write(cmd.tobyteArray());
				logger.info("service to " + device.getDeviceMac() + ": "
						+ SocketCommand.parseBytesToHexString(cmd.tobyteArray(), cmd.tobyteArray().length));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.count++;
	}

}
