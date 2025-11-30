package com.meditrack.servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.meditrack.dao.UserDAO;
import com.meditrack.model.User;

/**
 * ログイン処理を行うサーブレット
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    /**
     * ログイン画面を表示（GET）
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // すでにログイン済みの場合は患者一覧へ
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("patientList");
            return;
        }
        
        // ログイン画面を表示
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * ログイン処理（POST）
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // パラメータ取得
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // バリデーション
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "ユーザー名とパスワードを入力してください");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        // 認証処理
        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.authenticate(username, password);
            
            if (user != null) {
                // 認証成功：セッションにユーザー情報を保存
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());
                
                System.out.println("✅ ログイン成功: " + user.getName());
                
                // 患者一覧画面へリダイレクト
                response.sendRedirect("patientList");
                
            } else {
                // 認証失敗
                request.setAttribute("errorMessage", "ユーザー名またはパスワードが正しくありません");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
                dispatcher.forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "データベースエラーが発生しました");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
            dispatcher.forward(request, response);
            
        } finally {
            userDAO.closeConnection();
        }
    }
}