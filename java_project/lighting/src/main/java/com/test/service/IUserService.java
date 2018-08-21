package com.test.service;

import java.util.ArrayList;

import com.test.domain.DataObject;
import com.test.domain.Device;
import com.test.domain.Group;
import com.test.domain.Ploy;
import com.test.domain.PloyOperate;
import com.test.domain.User;
import com.test.domain.Zigbee;

public interface IUserService {

    public User getUserById(int id);
    
	public User getUserByName(String username);
	
	public DataObject getDataByUser(User user);
	
	public DataObject addDevToUser(Integer userid, String devMac);
	
	public DataObject removeDevFromUser(Integer userid, String devMac);
    
	public DataObject renameDev(String devMac, String devNewName);
	
	public DataObject renameZigbee(String zigbeeMac, String zigbeeNewName);
	
	public DataObject removeZigbee(String ZigbeeMac, User user);
	
	public DataObject addGroupToUser(int userid, String groupName);
	
	public DataObject removeGroupFromUser(int userid, int groupid);
	
	public DataObject renameGroup(int groupid, String groupNewName);
	
	public DataObject regist(String username, String password, String email, String phone);
	
	public int changePassword(User user, String newPassword);
	
	public Zigbee getZigbeeByMac(String zigbeeMac);
	
	public Group getGroupById(int groupid);
	
	public DataObject addPloyToUser(Ploy ploy);
	
	public DataObject removePloyById(Integer id);
	
	public DataObject renamePloyById(Integer id, String newName);
	
	public DataObject switchPloyById(Integer id, Integer status);
	
	public DataObject changePloyBindById(Integer id, Integer bindType, String bindData);
	
	public DataObject savePloyEditById(Integer ployid, ArrayList<PloyOperate> list);
	
	public DataObject ployRefresh(Integer userid);
	
	public Device selectDeviceByDevMac(String devMac);
}
