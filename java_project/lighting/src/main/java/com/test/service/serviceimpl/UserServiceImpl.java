package com.test.service.serviceimpl;

import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dao.DeviceAttrMapper;
import com.test.dao.DeviceMapper;
import com.test.dao.GroupMapper;
import com.test.dao.GroupPairMapper;
import com.test.dao.PloyMapper;
import com.test.dao.PloyOperateMapper;
import com.test.dao.UserMapper;
import com.test.dao.ZigbeeAttrMapper;
import com.test.dao.ZigbeeMapper;
import com.test.domain.DataObject;
import com.test.domain.Device;
import com.test.domain.DeviceAttr;
import com.test.domain.Group;
import com.test.domain.GroupPair;
import com.test.domain.Ploy;
import com.test.domain.PloyOperate;
//import com.test.domain.Group;
//import com.test.domain.ReflectBeanUtils;
import com.test.domain.User;
import com.test.domain.Zigbee;
import com.test.domain.ZigbeeAttr;
import com.test.service.IUserService;

@Service()
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userDao;
	@Autowired
	private DeviceMapper devDao;
	@Autowired
	private ZigbeeMapper zigbeeDao;
	@Autowired
	private GroupMapper groupDao;
	@Autowired
	private GroupPairMapper groupPairDao;
	@Autowired
	private ZigbeeAttrMapper zigbeeAttrDao;
	@Autowired
	private PloyMapper ployDao;
	@Autowired
	private PloyOperateMapper ployOperateDao;
	@Autowired
	private DeviceAttrMapper deviceAttrDao;

	public UserServiceImpl() {
//		System.out.println("UserServiceImpl");
	}

	public User getUserById(int id) {
		return userDao.selectByPrimaryKey(id);
	}
	
	public User getUserByName(String username) {
		return userDao.selectByUsername(username);
	}
	
	public DataObject getDataByUser(User user) {
		DataObject data = new DataObject();
		data.setUser(user);
		data.setDevArr(devDao.selectByUserid(user.getId()));
		data.setZigbeeArr(new ArrayList<Zigbee>());
		for (Device dev : data.getDevArr()) {
			data.getZigbeeArr().addAll(zigbeeDao.selectBydevMac(dev.getDevMac()));
		}
		data.setGroupArr(groupDao.selectByUserid(user.getId()));
		data.setGroupPairArr(groupPairDao.selectByUserid(user.getId()));
		
		data.setZigbeeAttrArr(new ArrayList<ZigbeeAttr>());
		ZigbeeAttr p;
		for (Zigbee zb : data.getZigbeeArr()) {
			p = zigbeeAttrDao.selectByPrimaryKey(zb.getZigbeeMac());
			if (null != p) {
				data.getZigbeeAttrArr().add(p);
			}
		}
		
		data.setDevAttrArr(new ArrayList<DeviceAttr>());
		DeviceAttr dca;
		for (Device dc : data.getDevArr()) {
			dca = deviceAttrDao.selectByPrimaryKey(dc.getDevMac());
			if (null != dca) {
				data.getDevAttrArr().add(dca);
			} else {
				dca = new DeviceAttr();
				dca.setDevMac(dc.getDevMac());
				int result = deviceAttrDao.insertSelective(dca);
				if (result > 0) {
					data.getDevAttrArr().add(dca);
				}
			}
		}
		
		data.setPloyArr(ployDao.selectByUserid(user.getId()));
		data.setPloyOperateArr(new ArrayList<>());
		for (Ploy ploy : data.getPloyArr()) {
			data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(ploy.getId()));
		}
		return data;
	}
	
	public DataObject addDevToUser(Integer userid, String devMac) {
		User user = userDao.selectByPrimaryKey(userid);
		Device dev = devDao.selectByPrimaryKey(devMac);
		DataObject data = new DataObject();
		if (dev == null) {//该设备未连接到服务器
			data.setError("The device is not connected to the server!");
			return data;
		}
		if (user == null) {//用户不存在
			data.setError("User does not exist!");
			return data;
		}
		if (dev.getUserid() == 0 || dev.getUserid() == 100000) {//如果为设备用户id为默认id，说明设备还未被其他用户添加
			dev.setUserid(userid);
			if (devDao.updateByPrimaryKeySelective(dev) > 0) {//将数据库中dev的userid改为当前用户的userid
				return getDataByUser(user);
			} else {
				data.setError("Failed to write to database!");
				return data;
			}
			
		} else {//该设备已被其他用户添加
			data.setError("The device has been added by a user with id" + dev.getUserid().toString());
			return data;
		}
	}
	
	public DataObject removeDevFromUser(Integer userid, String devMac) {
		User user = userDao.selectByPrimaryKey(userid);
		Device dev = devDao.selectByPrimaryKey(devMac);
		DataObject data = new DataObject();
		if (dev == null) {//该设备未连接到服务器
			data.setError("The device is not connected to the server!");
			return data;
		}
		if (user == null) {//用户不存在
			data.setError("User does not exist!");
			return data;
		}
		if (dev.getUserid().equals(userid)) {//如果为设备用户id与输入用户id匹配,可以删除
			dev.setUserid(0);//将设备id改为默认无用户状态id
			if (devDao.updateByPrimaryKeySelective(dev) > 0) {//将数据库中dev的userid改为当前用户的userid
				//将数据库中devMac地址为该集控器的zigbee节点
				return getDataByUser(user);
			} else {
				data.setError("Failed to write to database!");
				return data;
			}
			
		} else {//该设备是其他用户的设备
			data.setError("Insufficient permissions!");
			return data;
		}
	}
	
	public DataObject renameDev(String devMac, String devNewName) {
		Device dev = devDao.selectByPrimaryKey(devMac);
		DataObject data = new DataObject();
		if (dev == null) {//该设备未连接到服务器
			data.setError("The device is not connected to the server!");
			return data;
		}
		dev.setDevName(devNewName);// 将设备名称改为新的名称
		if (devDao.updateByPrimaryKeySelective(dev) > 0) {
			return data;
		} else {
			data.setError("Failed to write to database!");
			return data;
		}
	}
	
	public DataObject renameZigbee(String zigbeeMac, String zigbeeNewName) {
		Zigbee zb = zigbeeDao.selectByPrimaryKey(zigbeeMac);
		DataObject data = new DataObject();
		if (zb == null) {//该节点不存在
			data.setError("This node does not exist!");
			return data;
		}
		zb.setZigbeeName(zigbeeNewName);
		if (zigbeeDao.updateByPrimaryKeySelective(zb) > 0) {
			return data;
		} else {
			data.setError("Failed to write to database!");
			return data;
		}
	}
	
	public DataObject removeZigbee(String zigbeeMac, User user) {
		DataObject data = new DataObject();
		Zigbee zb = zigbeeDao.selectByPrimaryKey(zigbeeMac);
		ZigbeeAttr zbAt = zigbeeAttrDao.selectByPrimaryKey(zigbeeMac);
		if (zb == null) {
			data.setError("This node does not exist!");
			return data;
		}
		//删除分组信息
		ArrayList<GroupPair> groupPairList = groupPairDao.selectByZigbeeMac(zigbeeMac);
		if (groupPairList != null && groupPairList.size() > 0) {
			for (GroupPair gp : groupPairList) {
				groupPairDao.deleteByPrimaryKey(gp.getId());
			}
		}
		//删除拓展信息
		if (zbAt != null) {
			zigbeeAttrDao.deleteByPrimaryKey(zbAt.getZigbeeMac());
		}
		//删除节点信息
		zigbeeDao.deleteByPrimaryKey(zigbeeMac);
		
		return getDataByUser(user);
	}
	
	public DataObject addGroupToUser(int userid, String groupName) {
		User user = userDao.selectByPrimaryKey(userid);
		Group group = new Group();
		group.setGroupName(groupName);
		group.setUserid(userid);
		int result = groupDao.insert(group);
		if (result > 0) {
			return getDataByUser(user);
		} else {
			DataObject data = new DataObject();
			data.setError("Failed to write to database!");
			return data;
		}
		
	}
	
	public DataObject removeGroupFromUser(int userid, int groupid) {
		User user = userDao.selectByPrimaryKey(userid);
		Group group = groupDao.selectByPrimaryKey(groupid);
		DataObject data = new DataObject();
		if (user == null) {//用户不存在
			data.setError("User does not exist!");
			return data;
		}
		if (group.getUserid().equals(userid)) {//如果组的用户id与输入用户id匹配,可以删除
			if (groupDao.deleteByPrimaryKey(groupid) > 0) {
				groupPairDao.deleteByGroupid(groupid);
				//将zigbee中的组id删除, 需要socket通讯操作
				return getDataByUser(user);
			} else {
				data.setError("Failed to write to database!");
				return data;
			}
			
		} else {//该设备是其他用户的设备
			data.setError("Insufficient permissions!");
			return data;
		}
	}
	
	public DataObject renameGroup(int groupid, String groupNewName) {
		Group group = groupDao.selectByPrimaryKey(groupid);
		DataObject data = new DataObject();
		if (group == null) {//该分组不存在
			data.setError("The group does not exist!");
			return data;
		}
		group.setGroupName(groupNewName);
		if (groupDao.updateByPrimaryKeySelective(group) > 0) {
			return data;
		} else {
			data.setError("Failed to write to database!");
			return data;
		}
	}
	
	public DataObject regist(String username, String password, String email, String phone) {
		DataObject data = new DataObject();
		if (username.equals("") || username == null) {
			data.setError("Username can not be empty!");
			return data;
		}
		if (password.equals("") || password == null) {
			data.setError("Password can not be empty!");
			return data;
		}
		User user = userDao.selectByUsername(username);
		if (user != null) {//用户名已被注册
			data.setError("this username has been registered!");
		} else {
			user = new User();
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);
			user.setPhone(phone);
			int result = userDao.insert(user);
			if (result > 0) {//插入成功
			} else {
				data.setError("Registration failed, please try again!");
			}
		}
		return data;
	}
	
	public int changePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		return userDao.updateByPrimaryKey(user);
	}
	
	public Zigbee getZigbeeByMac(String zigbeeMac) {
		return zigbeeDao.selectByPrimaryKey(zigbeeMac);
	}
	
	public Group getGroupById(int groupid) {
		return groupDao.selectByPrimaryKey(groupid);
	}
	
	public DataObject addPloyToUser(Ploy ploy) {
		DataObject data = new DataObject();
		int row = ployDao.insert(ploy);
		if (row < 1) {
			data.setError("Failed to add!");
		} else {
			data.setPloyArr(ployDao.selectByUserid(ploy.getUserid()));
			data.setPloyOperateArr(new ArrayList<>());
			for (Ploy temp : data.getPloyArr()) {
				data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
			}
		}
		return data;
	}
	
	public DataObject removePloyById(Integer id) {
		DataObject data = new DataObject();
		Ploy ploy = ployDao.selectByPrimaryKey(id);
		if (ploy != null) {
			int row = ployDao.deleteByPrimaryKey(id);
			if (row < 1) {
				data.setError("Failed to delete!");
				return data;
			} else {
				ployOperateDao.deleteByPloyid(ploy.getId());
				data.setPloyArr(ployDao.selectByUserid(ploy.getUserid()));
				data.setPloyOperateArr(new ArrayList<>());
				for (Ploy temp : data.getPloyArr()) {
					data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
				}
			}
		}
		
		return data;
	}
	
	public DataObject renamePloyById(Integer id, String newName) {
		DataObject data = new DataObject();
		Ploy ploy = new Ploy();
		ploy.setId(id);
		ploy.setPloyName(newName);
		int row = ployDao.updateByPrimaryKeySelective(ploy);
		if (row < 1) {
			data.setError("Fail to edit!");
		} else {
			ploy = ployDao.selectByPrimaryKey(id);
			data.setPloyArr(ployDao.selectByUserid(ploy.getUserid()));
			data.setPloyOperateArr(new ArrayList<>());
			for (Ploy temp : data.getPloyArr()) {
				data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
			}
		}
		return data;
	}
	
	public DataObject switchPloyById(Integer id, Integer status) {
		DataObject data = new DataObject();
		Ploy ploy = new Ploy();
		ploy.setId(id);
		ploy.setStatus(status);
		int row = ployDao.updateByPrimaryKeySelective(ploy);
		if (row < 1) {
			data.setError("Switch failed!");
		}
		return data;
	}
	
	public DataObject changePloyBindById(Integer id, Integer bindType, String bindData) {
		DataObject data = new DataObject();
		Ploy ploy = new Ploy();
		ploy.setId(id);
		ploy.setBindType(bindType);
		ploy.setBindData(bindData);
		int row = ployDao.updateByPrimaryKeySelective(ploy);
		if (row < 1) {
			data.setError("Fail to edit!");
		} else {
			ploy = ployDao.selectByPrimaryKey(id);
			data.setPloyArr(ployDao.selectByUserid(ploy.getUserid()));
			data.setPloyOperateArr(new ArrayList<>());
			for (Ploy temp : data.getPloyArr()) {
				data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
			}
		}
		return data;
	}
	
	public DataObject savePloyEditById(Integer ployid, ArrayList<PloyOperate> list) {
		DataObject data = new DataObject();
		
		ArrayList<PloyOperate> sourceList = ployOperateDao.selectByPloyid(ployid);// 从数据库中查询出所有operate
		
		if (list != null && list.size() > 0) {// 如果list不为空
			for (PloyOperate sourceOperate : sourceList) {// 遍历数据库中的operate
				PloyOperate operate;
				for (int i = 0; i < list.size(); i++) {// 遍历新编辑的operate
					operate = list.get(i);
					if (operate.getId() != null && operate.getId() == sourceOperate.getId()) {// 如果id相同，更新
						ployOperateDao.updateByPrimaryKeySelective(operate);
						break;
					} else if (i == list.size() - 1) {//如果没有id相同的，删除
						ployOperateDao.deleteByPrimaryKey(sourceOperate.getId());
					}
				}
			}
			
			for (PloyOperate operate : list) {// 插入
				if (operate.getId() == null) {
					ployOperateDao.insert(operate);
				}
			}
		}
		
//		data.setPloyOperateArr(ployOperateDao.selectByPloyid(ployid)); // 从数据库中查询出所有operate
		data.setPloyArr(ployDao.selectByUserid(ployDao.selectByPrimaryKey(ployid).getUserid()));
		data.setPloyOperateArr(new ArrayList<>());
		for (Ploy temp : data.getPloyArr()) {
			data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
		}
		
		return data;
	}
	
	public DataObject ployRefresh(Integer userid) {
		DataObject data = new DataObject();
		data.setPloyArr(ployDao.selectByUserid(userid));
		data.setPloyOperateArr(new ArrayList<>());
		for (Ploy temp : data.getPloyArr()) {
			data.getPloyOperateArr().addAll(ployOperateDao.selectByPloyid(temp.getId()));
		}
		return data;
	}
	
	public Device selectDeviceByDevMac(String devMac) {
		return devDao.selectByPrimaryKey(devMac);
	}

}
