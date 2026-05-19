package es.gestorIdeas.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {
    private static final String BD = "gestor-proyectos.db";
    private static final String URL = "jdbc:sqlite:" + BD;
    
    // Una única instancia para toda la aplicación
    private static Connection conexion = null;

    
    public static Connection getConexion() {
        try {
            // Si la conexión no existe o se cerró, abrir una nueva
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL);
                System.out.println("Conexión a la BBDD " + BD + " ... OK");
                
                conexion.createStatement().execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            System.err.println("CRÍTICO: No se pudo conectar a la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    /**
     * Cierra la conexión manualmente si es necesario
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
            }
        }
    }
}