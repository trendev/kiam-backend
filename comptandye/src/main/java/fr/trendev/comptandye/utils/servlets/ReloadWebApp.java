/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@WebServlet({
    "/dist/professional",
    "/dist/individual",
    "/dist/login",
    "/dist/professional/*",
    "/dist/administrator",
    "/dist/login/*",
    "/dist/administrator/*",
    "/dist/individual/*",
    "/dist/register",
    "/dist/register/*",
    "/dist/unsupported-user-type",
    "/dist/unsupported-user-type/*",
    "/dist/welcome",
    "/dist/welcome/*",
    "/professional",
    "/individual",
    "/login",
    "/professional/*",
    "/administrator",
    "/login/*",
    "/administrator/*",
    "/individual/*",
    "/register",
    "/register/*",
    "/unsupported-user-type",
    "/unsupported-user-type/*",
    "/welcome",
    "/welcome/*"
})
public class ReloadWebApp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/dist/index.html").include(
                request,
                response);
    }

}
