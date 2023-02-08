package server;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "server.Servlet", urlPatterns = {"/swipe/left/","/swipe/right/"})
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.getWriter().write("hey");
//        response.getWriter().flush();
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String leftOrRight = request.getServletPath();
        Gson gson = new Gson();
        try {
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = request.getReader().readLine()) != null) {
                sb.append(s);
            }
            Swipe swipe = (Swipe) gson.fromJson(sb.toString(), Swipe.class);
//            if(leftOrRight.equals("/swipe/left/") || leftOrRight.equals("/swipe/right/"))

            if (!isNumeric(swipe.getSwiper()) || !isNumeric(swipe.getSwipee())
                    || swipe.getComment() == null || swipe.getComment().length() == 0 || swipe.getComment().length() > 256) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Bad Request!");
                response.getWriter().flush();
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                String swipeJsonString = gson.toJson(swipe);
                response.getWriter().print(swipeJsonString);
                response.getWriter().flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Bad Request!");
            response.getWriter().flush();
        }
    }
    protected boolean isNumeric(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
