package com.ludateam.wechat.qy.vo;

import java.util.ArrayList;
import java.util.List;

import com.ludateam.wechat.qy.entity.TaxEnterprise;

public class BindingRep extends ResultRep {

	/** 对返回码的文本描述内容 */
	private List<TaxEnterprise> bindingList = new ArrayList<TaxEnterprise>();

	public List<TaxEnterprise> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<TaxEnterprise> bindingList) {
		this.bindingList = bindingList;
	}
}
