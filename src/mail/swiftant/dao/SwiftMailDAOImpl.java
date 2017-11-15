package mail.swiftant.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import mail.swiftant.domain.MailContent;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SwiftMailDAOImpl extends JdbcDaoSupport implements SwiftMailDAO {

	boolean exist = false;

	@SuppressWarnings("unchecked")
	@Override
	public List<MailContent> loadInbox() {
		List<MailContent> Ilist = new ArrayList<MailContent>();
		try {
			String sql = "spcr_SwiftMail_InBox";
			Ilist = getJdbcTemplate().query(sql, new InboxMessages());
			exist = true;
		} catch (Exception e) {
			exist = false;
			logger.error("Error in Dao" + e);
		}
		return Ilist;
	}

	@SuppressWarnings("rawtypes")
	private final static class InboxMessages implements RowMapper {
		@Override
		public Object mapRow(ResultSet RS, int arg1) throws SQLException {
			MailContent mails = new MailContent();
			mails.setSubject(RS.getString("Subject"));
			mails.setMessage(RS.getString("Message"));
			mails.setAddress(RS.getString("Email"));
			mails.setSerial(RS.getInt("Serial"));
			mails.setIsActive(RS.getInt("IsActive"));
			mails.setTime(RS.getString("CRTS"));
			return mails;
		}
	}

	@Override
	public boolean sendMail(MailContent mailContent) {

		try {

			String Address = mailContent.getAddress();
			String Subject = mailContent.getSubject();
			String Message = mailContent.getMessage();
			String Sql = "EXEC spci_SwiftMail ?,?,?";
			Object obj[] = new Object[] { Address, Subject, Message };
			int typ[] = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
			getJdbcTemplate().update(Sql, obj, typ);
			exist = true;
		} catch (Exception e) {
			exist = false;
			logger.error("Error in Dao" + e);
		}
		return exist;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MailContent> loadOutbox() {
		List<MailContent> Ilist = new ArrayList<MailContent>();
		try {
			String sql = "Exec spcr_SwiftMail_OutBox";
			Ilist = getJdbcTemplate().query(sql, new OutMessages());
			exist = true;
		} catch (Exception e) {
			exist = false;
			logger.error("Error in Dao" + e);
		}
		return Ilist;
	}

	@SuppressWarnings("rawtypes")
	private static final class OutMessages implements RowMapper {

		@Override
		public Object mapRow(ResultSet RS, int arg1) throws SQLException {
			MailContent outmails = new MailContent();
			outmails.setSubject(RS.getString("Subject"));
			outmails.setMessage(RS.getString("Message"));
			outmails.setAddress(RS.getString("Email"));
			outmails.setSerial(RS.getInt("Serial"));
			outmails.setIsActive(RS.getInt("IsActive"));
			outmails.setTime(RS.getString("CRTS"));
			return outmails;
		}
	}

	@Override
	public boolean deleteSelected(List<MailContent> mailcontentDetails) {
		try {

			String sql = "Exec spcu_SwiftMail_Delete ?";
			for (int i = 0; i < mailcontentDetails.size(); i++) {
				Integer serialId = mailcontentDetails.get(i).getSerial();
				Object obj[] = new Object[] { serialId };
				int typ[] = new int[] { Types.INTEGER };
				getJdbcTemplate().update(sql, obj, typ);
				exist = true;
			}
		} catch (Exception e) {
			exist = false;
			logger.error("Error in Dao" + e);
		}
		return exist;
	}

	@Override
	public boolean favourites(MailContent mailContent) {
		try {

			int isActive = mailContent.getIsActive();
			int serial = mailContent.getSerial();
			String sql = "Exec spcu_SwiftMail_Fav ?,?";
			Object obj[] = new Object[] { isActive, serial };
			int type[] = new int[] { Types.INTEGER, Types.INTEGER };
			getJdbcTemplate().update(sql, obj, type);
			exist = true;
		} catch (Exception e) {
			exist = false;
			logger.error("Error in Dao" + e);
		}
		return exist;
	}
}