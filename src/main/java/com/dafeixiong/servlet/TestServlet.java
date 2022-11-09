package com.dafeixiong.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>项目名称：tomcat-demo</p>
 * <p>类名：com.dafeixiong.tomcat.servlet.TestServlet</p>
 * <p>创建时间: 2022-11-07 18:16 </p>
 * <p>修改时间: 2022-11-07 18:16 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = -4434044999822788546L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String name = req.getParameter("name");
        if (name == null) {
            name = "world";
        }
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, " + name + "!</h1>");
        pw.flush();
    }
}
