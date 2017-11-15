package mail.swiftant.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mail.swiftant.domain.DemoClass;
import mail.swiftant.domain.JsonView;
import mail.swiftant.domain.MailContent;
import mail.swiftant.service.SwiftMailService;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SwiftMailController {

	@Autowired
	private SwiftMailService mailservice;

	@Autowired
	private JavaMailSender mailSender;

	@SuppressWarnings("rawtypes")
	HashMap result = new HashMap();
	JsonView view = new JsonView();
	ModelMap map = new ModelMap();
	ObjectMapper mapper = new ObjectMapper();
	MailContent mailContent = new MailContent();
	String jsonString;
	private static Logger logger = Logger.getLogger(SwiftMailController.class);

	@RequestMapping(value = "/Home.htm", method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("/HTML/Home.html");
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/message.htm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView sendMail(HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException,
			JsonMappingException, IOException {
		mapper.configure(
				org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		mailContent = mapper.readValue(request.getParameter("data"),
				MailContent.class);

		List<MailContent> outMessages = new ArrayList<MailContent>();

		try {
			String recipientAddress = mailContent.getAddress();
			String subject = mailContent.getSubject();
			String message = mailContent.getMessage();
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setText(message);
			mailSender.send(email);

			mailservice.sendMail(mailContent);
			outMessages = mailservice.loadOutbox();
			result.put("status", 1);
			result.put("outMessages", outMessages);
			jsonString = mapper.writeValueAsString(result);
			map.addObject("jsonString", jsonString);

		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}

		return new ModelAndView(view, map);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/loadInbox.htm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView loadInbox(HttpServletRequest request,
			HttpServletResponse response) {

		List<MailContent> inboxMessages = new ArrayList<MailContent>();

		try {
			inboxMessages = mailservice.loadInbox();
			result.put("status", 1);
			result.put("inboxMessages", inboxMessages);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/loadOutbox.htm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView loadOutbox(HttpServletRequest request,
			HttpServletResponse response) {

		List<MailContent> outboxMessages = new ArrayList<MailContent>();

		try {
			outboxMessages = mailservice.loadOutbox();
			result.put("status", 1);
			result.put("outboxMessages", outboxMessages);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);

	}

	@SuppressWarnings({"deprecation", "unchecked" })
	@RequestMapping(value = "/deleteSelected.htm", method = {
			RequestMethod.POST, RequestMethod.GET })
	public ModelAndView deleteSelected(HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			DemoClass list = mapper.readValue(request.getParameter("data"),
					DemoClass.class);
			List<MailContent> mailcontentDetails = (List<MailContent>) list
					.getListmail();
			boolean status = mailservice.deleteSelected(mailcontentDetails);
			result.put("status", status);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/favourites.htm", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ModelAndView favourites(HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			mapper.configure(
					org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			mailContent = mapper.readValue(request.getParameter("data"),
					MailContent.class);
			boolean status = mailservice.favourites(mailContent);
			result.put("status", status);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);
	}
}
