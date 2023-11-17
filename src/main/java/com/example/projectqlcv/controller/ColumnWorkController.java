package com.example.projectqlcv.controller;

import com.example.projectqlcv.DAO.ColumnDAO;

import com.example.projectqlcv.model.Card;
import com.example.projectqlcv.DAO.IColumDAO;
import com.example.projectqlcv.DAO.IUserDAO;
import com.example.projectqlcv.DAO.UserDAO;
import com.example.projectqlcv.model.Column;
import com.example.projectqlcv.model.Group;
import com.example.projectqlcv.model.Table;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ColumnWorkController", value = "/column")
public class ColumnWorkController extends HttpServlet {
    IColumDAO iColumDAO = null;
    IUserDAO userDAO = null;

    @Override
    public void init() throws ServletException {
        iColumDAO = new ColumnDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "addColumn":
                createColumn(request, response);
                break;
            case "addCart":
                createCart(request, response);
                break;
        }

    }

    private void createCart(HttpServletRequest request, HttpServletResponse response) {
        try {
            int idColumn = Integer.parseInt(request.getParameter("idColumn"));
            String name = request.getParameter("name");
            Card card = new Card();
            card.setIdColumn(idColumn);
            card.setName(name);
            iColumDAO.addCard(card);
            response.sendRedirect("/column");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createColumn(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("idTable"));
            String name = request.getParameter("name");
            iColumDAO.addColumnWork(id,name);
          response.sendRedirect("/column");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "delete":
                deleteColumn(request,response);
                break;
            default:
                showAllColumn(request,response);
                break;
        }
    }

    private void deleteColumn(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        iColumDAO.deleteColumnWork(id);
        List<Column> listColumn= iColumDAO.selectAllColumn();
        request.setAttribute("listColumn", listColumn);
        try {
        request.getRequestDispatcher("home/tableView.jsp").forward(request,response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAllColumn(HttpServletRequest request, HttpServletResponse response) {
        List<Column> listColumn= iColumDAO.selectAllColumn();
        List<Card> listCard = iColumDAO.selectAllCard();
        List<Table> listTable = userDAO.selectAllTable();
        HttpSession session = request.getSession();
        try {
            session.setAttribute("listCard",listCard);
            session.setAttribute("listColumn", listColumn);
            session.setAttribute("listTable",listTable);
            request.getRequestDispatcher("home/tableView.jsp").forward(request,response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
