package com.wyu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wyu.pojo.TypebookCf;
import com.wyu.service.TypebookCfService;

@Controller
public class TypebookCfController {
	@Autowired
	private TypebookCfService service;

	// 1.�û�����
	@RequestMapping(value = "/findTypebookCf", produces = "appliction/json;charset=utf8")
	@ResponseBody
	public String findTypebookCf() {
		//System.out.println("page" + page + ";rows" + rows);
		// ��1��5����ѯǰ���������ݣ���10��5���ӵ�10����ʼ��ѯ5������
		//PageHelper.startPage(page, rows);
		List<TypebookCf> list = service.findCirculationBook();
		list.forEach(li -> System.out.println(li));
		PageInfo<TypebookCf> info = new PageInfo<>(list);
		// �õ��ܼ�¼��
		long total = info.getTotal();
		// ��list���Ϸ�װ��json����
		String jsonlist = JSON.toJSONString(list);
		// ������ɵ�json����
		
		String json = "{\"total\" : " + total + ",\"rows\" : " + jsonlist + "}";
		System.out.println("json=" + json);
		return jsonlist;

	}

}
