package com.shuangge.english.entity.server.shop;

import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class PayListResult extends RestResult {
	
	public static final Integer SUCCESS_STATUS = 0;
	public static final Integer UNCHECKED_STATUS = 1;
	public static final Integer FAIL_STATUS = 2;
	
	public static final Integer ERROR_STATUS = 21007; //鑻规灉鍟嗗簵 涓�娆￠獙璇佽繑鍥炵爜  鍚敤2娆￠獙璇�
	
	private List<CashPayData> cashPayDatas;

	public List<CashPayData> getCashPayDatas() {
		return cashPayDatas;
	}

	public void setCashPayDatas(List<CashPayData> cashPayDatas) {
		this.cashPayDatas = cashPayDatas;
	}

}
