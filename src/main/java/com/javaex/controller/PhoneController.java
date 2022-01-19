package com.javaex.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller
@RequestMapping(value="/phone")
public class PhoneController {

	@RequestMapping(value="/writeForm", method= {RequestMethod.GET, RequestMethod.POST})
	public String writeForm() {
		System.out.println("PhoneController>writeForm()");
		
		return "/WEB-INF/views/writeForm.jsp";
	}
	
	/*
	@RequestMapping(value="/phone/write", method= {RequestMethod.GET, RequestMethod.POST})
	public String write(@RequestParam("name") String name, 
						@RequestParam("hp") String hp, 
						@RequestParam("company") String company) {
		System.out.println("PhoneController>write()");
		
		PersonVo pvo = new PersonVo(name, hp, company);
		PhoneDao pDao = new PhoneDao();
		pDao.PersonInsert(pvo);
		
		return "";
	}
	*/
	

	@RequestMapping(value="/write", method= {RequestMethod.GET, RequestMethod.POST})
	public String write(@ModelAttribute PersonVo pvo) {
		System.out.println("PhoneController>write()");
		
		//저장
		PhoneDao pDao = new PhoneDao();
		pDao.PersonInsert(pvo);
		
		//리다이렉트
		
		return "redirect:/phone/list";
	}
	
	@RequestMapping(value="/list", method= {RequestMethod.GET, RequestMethod.POST})
	public String list(Model model) {
		System.out.println("PhoneController>list()");
		
		//Dao에서 리스트를 가져온다
		PhoneDao pDao = new PhoneDao();
		List<PersonVo> pList = pDao.getPersonList();
		System.out.println(pList);
		
		//컨트롤러 ----> DS 데이터를 보낸다 (model)
		model.addAttribute("pList", pList);	
		
		return "/WEB-INF/views/list.jsp";
	}
	
	@RequestMapping(value="/updateForm", method= {RequestMethod.GET, RequestMethod.POST})
	public String updateForm(Model model, @RequestParam("id") int id) {
		System.out.println("PhoneController>updateForm()");
		
		PhoneDao pDao = new PhoneDao();
		PersonVo pvo = pDao.getPerson(id);
		
		model.addAttribute("pvo", pvo);
		
		return "/WEB-INF/views/updateForm.jsp";
	}
	
	@RequestMapping(value="/update", method= {RequestMethod.GET, RequestMethod.POST})
	public String delete(@ModelAttribute PersonVo pvo, @RequestParam("id") int id) {
		System.out.println("PhoneController>update()");
		
		PhoneDao pDao = new PhoneDao();
		pDao.PersonUpdate(id, pvo);
		
		return "redirect:/phone/list";
	}

	
}
