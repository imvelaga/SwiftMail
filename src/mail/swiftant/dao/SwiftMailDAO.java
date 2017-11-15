package mail.swiftant.dao;

import java.util.List;

import mail.swiftant.domain.MailContent;

public interface SwiftMailDAO {
	
	public List<MailContent> loadInbox();

	public boolean sendMail(MailContent mailContent);

	public List<MailContent> loadOutbox();

	public boolean deleteSelected(List<MailContent> mailcontentDetails);

	public boolean favourites(MailContent mailContent);

}
