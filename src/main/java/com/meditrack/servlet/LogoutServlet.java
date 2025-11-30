package com.meditrack.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ログアウト処理を行うサーブレット
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // セッションを破棄
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userName = (String) session.getAttribute("userName");
            session.invalidate();
            System.out.println("✅ ログアウト: " + userName);
        }
        
        // ログイン画面へリダイレクト
        response.sendRedirect("login");
    }
}