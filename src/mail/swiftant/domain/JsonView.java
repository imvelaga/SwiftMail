package mail.swiftant.domain;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class JsonView implements View {

	public String getContentType() 
	{
		return "application/json";
	}
	
	@SuppressWarnings("rawtypes")
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        writer.print(model.get("jsonString").toString());
        writer.close();
    }

}