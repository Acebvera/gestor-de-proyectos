package es.gestorIdeas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.gestorIdeas.database.ConexionDB;
import es.gestorIdeas.database.NombresBaseDatos;
import es.gestorideas.model.PrioridadTarea;
import es.gestorideas.model.Tarea;

public class TareaDAO {

	public int crearTarea(Tarea t) {
	    String sql = "INSERT INTO " + NombresBaseDatos.TABLA_TAREA + 
	                 " (" + NombresBaseDatos.COLUMNA_TAREA_PROYECTO_ID + 
	                 ", " + NombresBaseDatos.COLUMNA_TAREA_NOMBRE +
	                 ", " + NombresBaseDatos.COLUMNA_TAREA_NOTA_ADICIONAL + 
	                 ", " + NombresBaseDatos.COLUMNA_TAREA_PRIORIDAD + 
	                 ", " + NombresBaseDatos.COLUMNA_TAREA_COMPLETADA +
	                 ", " + NombresBaseDatos.COLUMNA_TAREA_ACTIVO + 
	                 ") VALUES(?, ?, ?, ?, ?, ?)";

	    // Añado RETURN_GENERATED_KEYS para obtener el ID real de la DB
	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        
	        pstmt.setInt(1, t.getProyectoId());
	        pstmt.setString(2, t.getNombre());
	        pstmt.setString(3, t.getNotaAdicional());
	        pstmt.setString(4, t.getPrioridadTarea().name());
	        pstmt.setInt(5, t.isCompletada() ? 1 : 0);
	        pstmt.setInt(6, t.isActivo() ? 1 : 0);
	        
	        int filasAfectadas = pstmt.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    int idGenerado = rs.getInt(1);
	                    t.setId(idGenerado); 
	                    System.out.println("Tarea creada correctamente con ID: " + idGenerado);
	                    return idGenerado;
	                }
	            }
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Error al insertar tarea: " + e.getMessage());
	    }
	    return -1;
	}

	public List<Tarea> listarTareasPorProyecto(int idProyecto) {
	    List<Tarea> lista = new ArrayList<>();
	    String sql = "SELECT * FROM " + NombresBaseDatos.TABLA_TAREA + 
	                 " WHERE " + NombresBaseDatos.COLUMNA_TAREA_PROYECTO_ID + " = ?" +
	                 " AND " + NombresBaseDatos.COLUMNA_TAREA_ACTIVO + " = 1";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, idProyecto);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Tarea t = new Tarea();
	                t.setId(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_ID));
	                t.setProyectoId(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_PROYECTO_ID));
	                t.setNombre(rs.getString(NombresBaseDatos.COLUMNA_TAREA_NOMBRE));
	                t.setNotaAdicional(rs.getString(NombresBaseDatos.COLUMNA_TAREA_NOTA_ADICIONAL));
	                
	                t.setPrioridadTarea(PrioridadTarea.fromString(rs.getString(NombresBaseDatos.COLUMNA_TAREA_PRIORIDAD)));
	                
	                t.setCompletada(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_COMPLETADA) == 1);
	                t.setActivo(true);
	                
	                lista.add(t);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al listar tareas del proyecto: " + e.getMessage());
	    }
	    return lista;
	}

	public Tarea obtenerTarea(int id) {
	    Tarea tarea = null;
	    String sql = "SELECT * FROM " + NombresBaseDatos.TABLA_TAREA + 
	                 " WHERE " + NombresBaseDatos.COLUMNA_TAREA_ID + " = ?";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, id);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                tarea = new Tarea();
	                tarea.setId(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_ID));
	                tarea.setProyectoId(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_PROYECTO_ID));
	                tarea.setNombre(rs.getString(NombresBaseDatos.COLUMNA_TAREA_NOMBRE));
	                tarea.setNotaAdicional(rs.getString(NombresBaseDatos.COLUMNA_TAREA_NOTA_ADICIONAL));
	                
	                String prioridadStr = rs.getString(NombresBaseDatos.COLUMNA_TAREA_PRIORIDAD);
	                tarea.setPrioridadTarea(PrioridadTarea.fromString(prioridadStr));
	                
	                tarea.setCompletada(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_COMPLETADA) == 1);
	                tarea.setActivo(rs.getInt(NombresBaseDatos.COLUMNA_TAREA_ACTIVO) == 1);
	            }
	        }
	        
	        if (tarea != null) {
	            System.out.println("Tarea recuperada: " + tarea.getNombre());
	        } else {
	            System.out.println("No se encontró ninguna tarea con ID: " + id);
	        }

	    } catch (SQLException e) {
	        System.err.println("Error al consultar la tarea por ID: " + e.getMessage());
	    }
	    
	    return tarea;
	}

	public void actualizarTarea(Tarea t) {
	    String sql = "UPDATE " + NombresBaseDatos.TABLA_TAREA + " SET " + 
	                 NombresBaseDatos.COLUMNA_TAREA_PROYECTO_ID + " = ?, " +
	                 NombresBaseDatos.COLUMNA_TAREA_NOMBRE + " = ?, " + 
	                 NombresBaseDatos.COLUMNA_TAREA_NOTA_ADICIONAL + " = ?, " +  
	                 NombresBaseDatos.COLUMNA_TAREA_PRIORIDAD + " = ?, " + 
	                 NombresBaseDatos.COLUMNA_TAREA_COMPLETADA + " = ?, " +
	                 NombresBaseDatos.COLUMNA_TAREA_ACTIVO + " = ? " +
	                 " WHERE " + NombresBaseDatos.COLUMNA_TAREA_ID + " = ?";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, t.getProyectoId());
	        pstmt.setString(2, t.getNombre());
	        pstmt.setString(3, t.getNotaAdicional());
	        
	        pstmt.setString(4, t.getPrioridadTarea().name()); 
	        
	        pstmt.setInt(5, t.isCompletada() ? 1 : 0);
	        pstmt.setInt(6, t.isActivo() ? 1 : 0);
	        pstmt.setInt(7, t.getId());
	        
	        int filasAfectadas = pstmt.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            System.out.println("Tarea con ID " + t.getId() + " actualizada correctamente.");
	        } else {
	            System.out.println("No se encontró ninguna tarea con el ID " + t.getId());
	        }
	    } catch (SQLException e) {
	        System.err.println("Error crítico al actualizar la tarea: " + e.getMessage());
	    }
	}
	
	public boolean eliminarTareaLogico(int id) {
	    String sql = "UPDATE " + NombresBaseDatos.TABLA_TAREA + " SET " +
	                 NombresBaseDatos.COLUMNA_TAREA_ACTIVO + " = 0 WHERE " +
	                 NombresBaseDatos.COLUMNA_TAREA_ID + " = ?";
	    
	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, id);

	        int filasAfectadas = pstmt.executeUpdate();

	        if (filasAfectadas > 0) {
	            System.out.println("Tarea con ID " + id + " marcada como inactiva.");
	            return true;
	        } else {
	            System.out.println("No se encontró la tarea con ID: " + id);
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Error al ejecutar borrado lógico de tarea: " + e.getMessage());
	    }
	    
	    return false;
	}
}
