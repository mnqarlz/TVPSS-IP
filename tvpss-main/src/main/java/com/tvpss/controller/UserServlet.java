package com.tvpss.controller;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.tvpss.mongodb.MongoDBConnectionManager;
import com.tvpss.model.User;
import com.tvpss.enums.Role;
import org.bson.Document;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MongoCollection<Document> collection;

    public UserServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	MongoDatabase database = MongoDBConnectionManager.getDatabase();
        collection = database.getCollection("users");  
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("list")) {
            listUsers(request, response); 
        } else if (action.equals("create")) {
            showCreateForm(request, response);  
        } else if (action.equals("edit")) {
            showEditForm(request, response);  
        } else if (action.equals("delete")) {
            deleteUser(request, response);  
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("create")) {
            createUser(request, response);  
        } else if (action.equals("update")) {
            updateUser(request, response); 
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int roleValue = doc.getInteger("role"); 
                Role role = Role.fromInt(roleValue);

                User user = new User(
                        doc.getObjectId("_id").toString(),
                        doc.getString("name"),
                        doc.getString("email"),
                        role,  
                        doc.getString("state"),
                        doc.getString("district"),
                        doc.getString("password")
                );
                users.add(user);
            }
        }

        request.setAttribute("users", users);  
        request.getRequestDispatcher("/WEB-INF/views/user/list.jsp").forward(request, response);  
    }

    /**
     * Displays the form to create a new user.
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/user/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");
        User user = getUserById(userId);  
        request.setAttribute("user", user);  
        request.getRequestDispatcher("/WEB-INF/views/user/edit.jsp").forward(request, response);  
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int roleValue = Integer.parseInt(request.getParameter("role"));
        Role role = Role.fromInt(roleValue); 
        String state = request.getParameter("state");
        String district = request.getParameter("district");
        String password = request.getParameter("password");

        Document userDoc = new Document("name", name)
                .append("email", email)
                .append("role", role.getRoleValue())  
                .append("state", state)
                .append("district", district)
                .append("password", password);

        collection.insertOne(userDoc);

        response.sendRedirect("UserServlet?action=list");  
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int roleValue = Integer.parseInt(request.getParameter("role"));
        Role role = Role.fromInt(roleValue);  
        String state = request.getParameter("state");
        String district = request.getParameter("district");
        String password = request.getParameter("password");

        Document updatedUser = new Document("name", name)
                .append("email", email)
                .append("role", role.getRoleValue())  
                .append("state", state)
                .append("district", district)
                .append("password", password);

        collection.updateOne(Filters.eq("_id", new org.bson.types.ObjectId(userId)),
                new Document("$set", updatedUser));

        response.sendRedirect("UserServlet?action=list");  
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("id");

        collection.deleteOne(Filters.eq("_id", new org.bson.types.ObjectId(userId)));

        response.sendRedirect("UserServlet?action=list");  
    }

    private User getUserById(String id) {
        Document doc = collection.find(Filters.eq("_id", new org.bson.types.ObjectId(id))).first();

        if (doc != null) {
            int roleValue = doc.getInteger("role");  
            Role role = Role.fromInt(roleValue);  

            return new User(
                    doc.getObjectId("_id").toString(),
                    doc.getString("name"),
                    doc.getString("email"),
                    role,  
                    doc.getString("state"),
                    doc.getString("district"),
                    doc.getString("password")
            );
        }
        return null;
    }

    @Override
    public void destroy() {
    	MongoDBConnectionManager.close();
    }
}
