package cac.crud22033.controlador;

import cac.crud22033.modelo.Alumno;
import cac.crud22033.modelo.Modelo;
import cac.crud22033.modelo.ModeloFactory;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/app"})
public class AppServlet extends HttpServlet {

    private Modelo model;
    private final String URI_LIST = "listadoAlumnos.jsp";
    private final String URI_REMOVE = "/WEB-INF/pages/alumnos/borrarAlumno.jsp";
    private final String URI_EDIT = "/WEB-INF/pages/alumnos/editarAlumno.jsp";

    @Override
    public void init() throws ServletException {
        this.model = getModelo();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        accion = accion == null ? "" : accion;
        int id;
        Alumno alu;
        switch (accion) {
            case "edit":
                id = Integer.parseInt(request.getParameter("id"));
                alu = model.getAlumno(id);
                request.setAttribute("yaTieneFoto", !alu.getFoto().contains("no-face"));
                request.setAttribute("alumnoAEditar", alu);
                request.getRequestDispatcher(URI_EDIT).forward(request, response);
                break;
            case "remove":
                id = Integer.parseInt(request.getParameter("id"));
                alu = model.getAlumno(id);
                request.setAttribute("alumnoABorrar", alu);
                request.getRequestDispatcher(URI_REMOVE).forward(request, response);
                break;
            default:
                request.setAttribute("listaAlumnos", model.getAlumnos());
                request.getRequestDispatcher(URI_LIST).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Alumno alu;
        String accion = request.getParameter("accion");
        accion = accion == null ? "" : accion;
        int id;
        switch (accion) {
            case "add":
                alu = new Alumno();
                cargarAlumnoSegunParams(alu, request);
                model.addAlumno(alu);
                break;
            case "update":
                id = Integer.parseInt(request.getParameter("id"));
                alu = model.getAlumno(id);
                cargarAlumnoSegunParams(alu, request);
                model.updateAlumno(alu);
                break;
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                model.removeAlumno(id);
                break;
        }
        doGet(request, response);
    }

    private void cargarAlumnoSegunParams(Alumno a, HttpServletRequest request) {
        a.setNombre(request.getParameter("nombre"));
        a.setApellido(request.getParameter("apellido"));
        a.setMail(request.getParameter("mail"));
        a.setFechaNacimiento(request.getParameter("fechaNac"));
        a.setFoto(request.getParameter("fotoBase64"));
    }

    private Modelo getModelo() throws ServletException {
        Modelo m = null;
        try ( InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(is);
            String tipoModelo = props.getProperty("tipoModelo");
            m = ModeloFactory.getInstance().crearModelo(tipoModelo);
        } catch (IOException ex) {
            throw new ServletException("Error de E/S al al leer 'config' para iniciar el Servlet", ex);
        }
        return m;
    }
}
