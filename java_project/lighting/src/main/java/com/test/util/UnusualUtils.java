package com.test.util;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.dao.UnusualTemperatureMapper;
import com.test.domain.UnusualTemperature;
import com.test.domain.ZigbeeAttr;

@Component
public class UnusualUtils {

	@Autowired
	private UnusualTemperatureMapper utDao;

	public static UnusualUtils testUtils;

	@PostConstruct
	public void init() {
		testUtils = this;
	}

	public static boolean IsTemperatureUnusual(ZigbeeAttr attr) {
		float temperature = (float) attr.getTemperature() / 100;
		if (temperature == 0 || temperature >= 50.0) {
			return true;
		}
		return false;
	}

	public static void RecordUnusualTemperatureToDB(ZigbeeAttr attr) {
		if (IsTemperatureUnusual(attr)) {
			// 将数据写入数据库
			UnusualTemperature ut = new UnusualTemperature();
			ut.setZigbeeMac(attr.getZigbeeMac());
			ut.setTemperature((float) attr.getTemperature() / 100);
			ut.setHumidity((float) attr.getHumidity() / 100);
			ut.setDate(new Date());
			testUtils.utDao.insert(ut);
		}
	}
}
