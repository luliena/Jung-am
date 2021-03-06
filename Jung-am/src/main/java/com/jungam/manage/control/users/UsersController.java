package com.jungam.manage.control.users;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jungam.manage.dao.UserDao;
import com.jungam.manage.util.CryptoUtils;
import com.jungam.manage.vo.UserVO;

@Controller
public class UsersController {
	private final static Logger logger = Logger.getLogger(UsersController.class);
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	protected ModelAndView userList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		ArrayList<UserVO> users = null;
		
		logger.trace("uesrList");
		
		mv.setViewName("login/list");
		
		if((users = userDao.list()) == null) users = new ArrayList<UserVO>();
		
		mv.addObject("list", users);
		
		return mv;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	protected ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
		return new ModelAndView("login/login");
	}
	
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	protected ModelAndView checkLogin(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("check login");
		
		String id = request.getParameter("id");
		String password = CryptoUtils.encodeBase64(CryptoUtils.getSHA256(request.getParameter("password")));
		logger.trace(id + " - " + password);
		
		UserVO checkUser = userDao.getUser(id);
		
		if(checkUser == null) {
			logger.trace("Incorrect ID");
			ModelAndView mv = new ModelAndView("login/login"); 
			mv.addObject("message", "Incorrect ID");
			return mv;
		} else if(password.compareTo(checkUser.getPassword()) != 0) {
			logger.trace("Incorrect Password");
			ModelAndView mv = new ModelAndView("login/login"); 
			mv.addObject("message", "Incorrect Password");
			return mv;
		}
		
		// insert session
		
		logger.trace("login success : " + id);
		return new ModelAndView(new RedirectView("noticeList.do"));
	}
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.GET)
	protected ModelAndView registerUser(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("login/register");
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	protected ModelAndView addUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		UserVO user = new UserVO();
		
		logger.debug("called addUser");
		
		user.setId(request.getParameter("id"));
		user.setPassword(CryptoUtils.encodeBase64(CryptoUtils.getSHA256(request.getParameter("password"))));
		user.setName(request.getParameter("name"));
		user.setEmail(request.getParameter("phone1") + request.getParameter("phone2")+ request.getParameter("phone3"));
		user.setEmail(request.getParameter("email") + request.getParameter("e_domain"));
		
		logger.trace(user.toString());
		
		userDao.addUser(user);
		
		mv.setViewName("login/list");
		
//		if(checkId == null)	mv.addObject("list", noticeList);
		
		return new ModelAndView(new RedirectView("login.do"));
	}
	
	@RequestMapping(value = "/checkDuplId", method = RequestMethod.POST)
	protected void checkDuplicateId(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("called checkDuplicateId");
		boolean isDuplicateId = true;
		String id = request.getParameter("id");
		logger.debug("id : " + id);
		
		UserVO checkId = userDao.getUser(id);

		if(checkId == null) isDuplicateId = false;
		logger.debug("duplicate Id : " + isDuplicateId);
		
		try {
			response.getWriter().print(String.valueOf(isDuplicateId));
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
