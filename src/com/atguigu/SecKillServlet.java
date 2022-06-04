package com.atguigu;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * 秒杀案例
 * @author Lenovo
 */
public class SecKillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SecKillServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userid = new Random().nextInt(50000) +"" ;
        String prodid =request.getParameter("prodid");

        boolean isSuccess= SecKill_redisByScript.doSecKill(userid,prodid);
        response.getWriter().print(isSuccess);
    }

}
