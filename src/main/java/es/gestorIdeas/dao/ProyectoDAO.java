package es.gestorIdeas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.gestorIdeas.database.ConexionDB;
import es.gestorIdeas.database.NombresBaseDatos;
import es.gestorideas.model.EstatusProyecto;
import es.gestorideas.model.Proyecto;

public class ProyectoDAO {
	
	
	public int crearProyecto(Proyecto p) {
        String sql = "INSERT INTO " + NombresBaseDatos.TABLA_PROYECTO + 
                     " (" + NombresBaseDatos.COLUMNA_PROYECTO_TITULO + 
                     ", " + NombresBaseDatos.COLUMNA_PROYECTO_DESCRIPCION +
                     ", " + NombresBaseDatos.COLUMNA_PROYECTO_ESTATUS + 
                     ", " + NombresBaseDatos.COLUMNA_PROYECTO_FECHA_CREACION + 
                     ", " + NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION +
                     ", " + NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO + 
                     ") VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, p.getTitulo());
            pstmt.setString(2, p.getDescripcion());
            
            // Guarda el .name()  "TO_DO"
            pstmt.setString(3, p.getEstatus().name());
            
            pstmt.setString(4, p.getFechaCreacion().toString());
            pstmt.setString(5, p.getFechaActualizacion().toString());
            
            pstmt.setInt(6, p.isActivo() ? 1 : 0);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
            	
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        System.out.println("Proyecto guardado con ID: " + idGenerado);
                        return idGenerado;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar proyecto: " + e.getMessage());
            
        }
        
        return -1; 
    }
	
	public Proyecto obtenerProyecto(int id) {
	    Proyecto proyecto = null;
	    String sql = "SELECT * FROM " + NombresBaseDatos.TABLA_PROYECTO + 
	                 " WHERE " + NombresBaseDatos.COLUMNA_PROYECTO_ID + " = ?";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, id);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	            	
	                proyecto = new Proyecto();
	                
	                proyecto.setId(rs.getInt(NombresBaseDatos.COLUMNA_PROYECTO_ID));
	                proyecto.setTitulo(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_TITULO));
	                proyecto.setDescripcion(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_DESCRIPCION));
	                
	                String estatusStr = rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_ESTATUS);
	                proyecto.setEstatus(EstatusProyecto.fromString(estatusStr));
	                
	                proyecto.setFechaCreacion(LocalDateTime.parse(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_FECHA_CREACION)));
	                proyecto.setFechaActualizacion(LocalDateTime.parse(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION)));
	                
	                proyecto.setActivo(rs.getInt(NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO) == 1);
	            }
	        }
	        
	        if (proyecto != null) {
	            System.out.println("Proyecto recuperado: " + proyecto.getTitulo());
	            System.out.println(proyecto.toString());
	        } else {
	            System.out.println("No se encontró ningún proyecto con ID: " + id);
	        }

	    } catch (SQLException e) {
	        System.err.println("Error al consultar proyecto por ID: " + e.getMessage());
	    }
	    
	    return proyecto;
	}
	
	public void actualizarProyecto(Proyecto p) {
	    String sql = "UPDATE " + NombresBaseDatos.TABLA_PROYECTO + " SET " + 
	                 NombresBaseDatos.COLUMNA_PROYECTO_TITULO + " = ?, " +
	                 NombresBaseDatos.COLUMNA_PROYECTO_DESCRIPCION + " = ?, " + 
	                 NombresBaseDatos.COLUMNA_PROYECTO_ESTATUS + " = ?, " +  
	                 NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION + " = ?, " +
	                 NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO + " = ? " +
	                 " WHERE " + NombresBaseDatos.COLUMNA_PROYECTO_ID + " = ?";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        p.setFechaActualizacion(LocalDateTime.now());
	        
	        pstmt.setString(1, p.getTitulo());
	        pstmt.setString(2, p.getDescripcion());
	        pstmt.setString(3, p.getEstatus().name()); 
	        pstmt.setString(4, p.getFechaActualizacion().toString());
	        pstmt.setInt(5, p.isActivo() ? 1 : 0);
	        pstmt.setInt(6, p.getId()); 
	        
	        int filasAfectadas = pstmt.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            System.out.println("Proyecto ID " + p.getId() + " actualizado correctamente.");
	        } else {
	            System.out.println("No se pudo actualizar: El Proyecto ID " + p.getId() + " no existe o no hay cambios.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error crítico al actualizar proyecto: " + e.getMessage());
	    }
	}
	
	public boolean existeProyecto(int id) {
	    boolean existe = false;
	    //Sólo necesito saber si existe
	    String sql = "SELECT 1 FROM " + NombresBaseDatos.TABLA_PROYECTO + 
	                 " WHERE " + NombresBaseDatos.COLUMNA_PROYECTO_ID + " = ?" +
	                 " AND " + NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO + " = 1";
	    
	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, id);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            existe = rs.next(); 
	        }
	    
	    } catch (SQLException e) {
	        System.err.println("Error al verificar existencia del proyecto: " + e.getMessage());
	    }

	    return existe;
	}
	
	public boolean eliminarProyectoLogico(int id) {
	    String sql = "UPDATE " + NombresBaseDatos.TABLA_PROYECTO + " SET " +
	                 NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO + " = 0, " +
	                 NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION + " = ? " +
	                 " WHERE " + NombresBaseDatos.COLUMNA_PROYECTO_ID + " = ?";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, LocalDateTime.now().toString());
	        pstmt.setInt(2, id);

	        int filasAfectadas = pstmt.executeUpdate();
	        
	        if (filasAfectadas > 0) {
	            System.out.println("Proyecto ID " + id + " marcado como inactivo (borrado lógico).");
	            return true;
	        } else {
	            System.out.println("No se encontró el proyecto ID " + id + " para eliminar.");
	        }

	    } catch (SQLException e) {
	        System.err.println("Error al ejecutar borrado lógico: " + e.getMessage());
	    }
	    
	    return false;
	}
	
	public List<Proyecto> listarProyectosActivos() {
		
	    List<Proyecto> lista = new ArrayList<>();
	    String sql = "SELECT * FROM " + NombresBaseDatos.TABLA_PROYECTO + 
	                 " WHERE " + NombresBaseDatos.COLUMNA_PROYECTO_ACTIVO + " = 1" +
	                 " ORDER BY " + NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION + " DESC";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            Proyecto p = new Proyecto();
	            p.setId(rs.getInt(NombresBaseDatos.COLUMNA_PROYECTO_ID));
	            p.setTitulo(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_TITULO));
	            p.setDescripcion(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_DESCRIPCION));
	            
	            String estatusStr = rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_ESTATUS);
	            p.setEstatus(EstatusProyecto.fromString(estatusStr));
	            
	            p.setFechaCreacion(LocalDateTime.parse(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_FECHA_CREACION)));
	            p.setFechaActualizacion(LocalDateTime.parse(rs.getString(NombresBaseDatos.COLUMNA_PROYECTO_FECHA_ACTUALIZACION)));
	            p.setActivo(true); // Ya es true porque se indica en el WHERE
	            System.out.println(p.toString());
	            lista.add(p);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al listar proyectos: " + e.getMessage());
	    }
	    return lista;
	}

	public List<Proyecto> buscarProyectos(String termino) {
	    List<Proyecto> resultados = new ArrayList<>();
	    String sql = "SELECT * FROM Proyectos WHERE (titulo LIKE ? OR descripcion LIKE ?) AND activo = 1";

	    try (Connection conn = ConexionDB.getConexion();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        String filtro = "%" + termino + "%";
	        pstmt.setString(1, filtro);
	        pstmt.setString(2, filtro);

	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            resultados.add(new Proyecto(
	                rs.getInt("id"),
	                rs.getString("titulo"),
	                rs.getString("descripcion"),
	                EstatusProyecto.valueOf(rs.getString("estatus")),
	                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
	                rs.getTimestamp("fecha_actualizacion").toLocalDateTime(),
	                rs.getBoolean("activo")
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return resultados;
	}
	
}
