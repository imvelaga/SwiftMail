package mail.swiftant.service;

import java.util.List;

import mail.swiftant.dao.SwiftMailDAO;
import mail.swiftant.domain.DemoClass;
import mail.swiftant.domain.MailContent;

import org.springframework.beans.factory.annotation.Autowired;

public class SwiftMailService {
	@Autowired
	private SwiftMailDAO dao;

	public List<MailContent> loadInbox() {
		return dao.loadInbox();
	}

	public boolean sendMail(MailContent mailContent) {
		return dao.sendMail(mailContent);
		
	}

	public List<MailContent> loadOutbox() {
		return dao.loadOutbox();
	}

	public boolean deleteSelected(List<MailContent> mailcontentDetails) {
		return dao.deleteSelected(mailcontentDetails);
		
	}

	public boolean favourites(MailContent mailContent) {
		return dao.favourites(mailContent);
	}

}
