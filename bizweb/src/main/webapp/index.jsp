<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date,
	com.bizcreator.core.session.jdbc.JdbcBase,
	org.springframework.jdbc.core.RowMapper,
	java.sql.*,
	java.util.*,
	com.bizcreator.core.context.BizContext,
	com.bizcreator.core.entity.EntityModel,
	com.bizcreator.core.entity.User,
	com.bizcreator.core.entity.Tenant,
	com.bizcreator.core.entity.CodeName,
	com.bizcreator.util.CollectionUtil,
	org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate,
	org.springframework.jdbc.core.JdbcTemplate,
	com.bizcreator.core.json.BizResultSet" 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
	//1. 初始化BizContext
	new BizContext(application, request, response);

	String module = request.getParameter("module");
	String action = request.getParameter("action");
	
	if (module == null) module = "Home";
	if (action == null) action = "index";
	
	JdbcBase jb = (JdbcBase) BizContext.getBean("jdbcBase");
	
	//2. 登录处理
	if ("User".equals(module) && "Authenticate".equals(action)) {
		String user_name = request.getParameter("user_name");
		String password = request.getParameter("password");
		try {
			User user = (User) jb.queryForObject(User.class, CollectionUtil.toMap(new Object[]{
					"name", user_name, "password", password
			}));
			
			if (user != null) {
				BizContext.getSessionMap().put("User", user);
				BizContext.getSessionMap().put("client_id", user.getClient_id());
				
				Tenant t = (Tenant) jb.queryForObject(Tenant.class, user.getClient_id());
				if (t != null && t.getDs_name() != null) {
					BizContext.getSessionMap().put("ds_name", t.getDs_name());
				}
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	else if ("User".equals(module) && "Logout".equals(action)) {
		
		BizContext.getSessionMap().clear();
		
	}
	
	if (session.getAttribute("User") == null) {
		request.getRequestDispatcher("/modules/User/Login.jsp").forward(request, response);
	}
	else if ("User".equals(module) && "Authenticate".equals(action)) {
		request.getRequestDispatcher("/modules/Home/index.jsp").forward(request, response);
	}
	else {
		request.getRequestDispatcher("/modules/" + module + "/index.jsp").forward(request, response);
	}
	
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>
