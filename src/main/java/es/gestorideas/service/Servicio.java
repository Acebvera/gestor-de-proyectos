package es.gestorideas.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gestorIdeas.dao.ProyectoDAO;
import es.gestorIdeas.dao.TareaDAO;
import es.gestorideas.model.EstatusProyecto;
import es.gestorideas.model.PrioridadTarea;
import es.gestorideas.model.Proyecto;
import es.gestorideas.model.Tarea;

public class Servicio {
	private ProyectoDAO proyectoDAO;
	private TareaDAO tareaDAO;

	
	public Servicio() {
        this.proyectoDAO = new ProyectoDAO();
        this.tareaDAO = new TareaDAO();
    }
	
	
	public int crearProyecto(Proyecto p) throws Exception {
		int idGenerado = 0;
		if (p.getTitulo() == null || p.getTitulo().isEmpty()) {
			throw new Exception("El proyecto debe tener nombre");
		}
		
		validarLongitudTexto(p.getTitulo(), 50, "Título");
	    validarLongitudTexto(p.getDescripcion(), 250, "Descripción");
		
		// Guardar proyecto
	    idGenerado = proyectoDAO.crearProyecto(p);
	    p.setId(idGenerado);
		
	    return idGenerado;
	}

	public void crearProyectoConTareas(Proyecto p, List<Tarea> tareas) throws Exception {
	    int idProyecto = crearProyecto(p);
	    
	    if (tareas != null && !tareas.isEmpty()) {
	        for (Tarea t : tareas) {
	            t.setProyectoId(idProyecto);
	            tareaDAO.crearTarea(t);
	        }
	    }
	}
	
	
	public int crearTarea(Tarea t) throws Exception {
	    if (t.getNombre() == null || t.getNombre().trim().isEmpty()) {
	        throw new Exception("La tarea debe tener un nombre válido.");
	    }

	    if (!proyectoDAO.existeProyecto(t.getProyectoId())) {
	        throw new Exception("No se puede crear la tarea: El proyecto asociado no existe o está inactivo.");
	    }

	    int idTarea = tareaDAO.crearTarea(t);
	    t.setId(idTarea);
	    
	    return idTarea;
	}
	
	public void actualizarProyecto(Proyecto p) throws Exception {
	    if (p.getTitulo() == null || p.getTitulo().trim().isEmpty()) {
	        throw new Exception("El título del proyecto no puede estar vacío.");
	    }
	    
	    validarLongitudTexto(p.getTitulo(), 50, "Título");
	    validarLongitudTexto(p.getDescripcion(), 250, "Descripción");
	    
	    p.setFechaActualizacion(LocalDateTime.now());
	    
	    proyectoDAO.actualizarProyecto(p);
	    System.out.println("Proyecto ID " + p.getId() + " actualizado en la base de datos.");
	}
	
	public void actualizarTarea(Tarea t) throws Exception {
	    if (t.getNombre() == null || t.getNombre().trim().isEmpty()) {
	        throw new Exception("El nombre de la tarea es obligatorio.");
	    }

	    if (t.getProyectoId() <= 0) {
	        throw new Exception("La tarea debe estar vinculada a un proyecto válido.");
	    }

	    tareaDAO.actualizarTarea(t);
	    
	    // Tras actualizar una tarea, recalcular el estatus del proyecto
	    actualizarEstatusProyectoAutomatico(t.getProyectoId());
	}
	
	public Proyecto obtenerProyecto(int id) throws Exception {
	    if (id < 1) {
	        throw new Exception("El ID suministrado no es válido.");
	    }
	    
	    Proyecto proyecto = proyectoDAO.obtenerProyecto(id);
	    
	    if (proyecto == null) {
	        throw new Exception("El proyecto con ID " + id + " no existe en la base de datos.");
	    }
	    
	    return proyecto;
	}
	
	public List<Proyecto> obtenerProyectosActivos (){
		
		return proyectoDAO.listarProyectosActivos();
	}
	
	public List<Tarea> obtenerTareasDeProyecto(int proyectoId) throws Exception {
	    if (proyectoId <= 0) {
	        throw new Exception("ID de proyecto no válido.");
	    }
	    
	    return tareaDAO.listarTareasPorProyecto(proyectoId);
	}
	
	public Tarea obtenerTarea(int id) throws Exception {
	    if (id < 1) {
	        throw new Exception("El ID de la tarea no es válido.");
	    }
	    
	    Tarea tarea = tareaDAO.obtenerTarea(id);
	    
	    if (tarea == null) {
	        throw new Exception("La tarea solicitada no existe.");
	    }
	    
	    return tarea;
	}
	
	public void actualizarEstadoTarea(int tareaId, boolean completada) throws Exception {
		
	    Tarea tarea = tareaDAO.obtenerTarea(tareaId);
	    if (tarea == null) {
	        throw new Exception("No se puede actualizar: la tarea no existe.");
	    }

	    tarea.setCompletada(completada);
	    
	    
	    tareaDAO.actualizarTarea(tarea);
	    
	    System.out.println("Tarea " + tareaId + " marcada como " + (completada ? "COMPLETADA" : "PENDIENTE"));
	    
	    actualizarEstatusProyectoAutomatico(tarea.getProyectoId());
	}
	

	public void eliminarTareaDeProyecto(int proyectoId, int tareaId) throws Exception {
	    Tarea tarea = tareaDAO.obtenerTarea(tareaId);

	    if (tarea == null) {
	        throw new Exception("La tarea no existe.");
	    }

	    if (tarea.getProyectoId() != proyectoId) {
	        throw new Exception("Error de integridad: La tarea no pertenece al proyecto indicado.");
	    }
	    
	    boolean exito = tareaDAO.eliminarTareaLogico(tareaId);
	    
	    if (!exito) {
	        throw new Exception("No se pudo eliminar la tarea de la base de datos.");
	    }
	}
	
	public void eliminarProyectoCompleto(int id) throws Exception {
	    Proyecto proyecto = proyectoDAO.obtenerProyecto(id);
	    if (proyecto == null) {
	        throw new Exception("El proyecto no existe.");
	    }
	    
	    List<Tarea> tareas = tareaDAO.listarTareasPorProyecto(id);
	    
	    // Borrado lógico de cada tarea
	    for (Tarea t : tareas) {
	        tareaDAO.eliminarTareaLogico(t.getId());
	    }
	    // Borrado lógico del proyecto
	    boolean exito = proyectoDAO.eliminarProyectoLogico(id);
	    
	    if (!exito) {
	        throw new Exception("Error al intentar eliminar el proyecto.");
	    }
	}

	private void actualizarEstatusProyectoAutomatico(int proyectoId) throws Exception {
	    Proyecto proyecto = proyectoDAO.obtenerProyecto(proyectoId);
	    List<Tarea> tareas = tareaDAO.listarTareasPorProyecto(proyectoId);

	    if (proyecto == null || tareas.isEmpty()) {
	        return; // No hay nada que calcular
	    }

	    long tareasCompletadas = tareas.stream().filter(Tarea::isCompletada).count();
	    int totalTareas = tareas.size();

	    EstatusProyecto nuevoEstatus;

	    if (tareasCompletadas == totalTareas) {
	        // Todas terminadas
	        nuevoEstatus = EstatusProyecto.DONE;
	    } else if (tareasCompletadas > 0) {
	        // Al menos una terminada, pero no todas
	        nuevoEstatus = EstatusProyecto.DOING;
	    } else {
	        // Ninguna terminada
	        nuevoEstatus = EstatusProyecto.TO_DO;
	    }

	    if (proyecto.getEstatus() != nuevoEstatus) {
	        proyecto.setEstatus(nuevoEstatus);
	        proyecto.setFechaActualizacion(LocalDateTime.now());
	        proyectoDAO.actualizarProyecto(proyecto);
	        System.out.println("Estatus del proyecto " + proyectoId + " actualizado automáticamente a: " + nuevoEstatus);
	    }
	}
	
	public List<Proyecto> buscarProyectos(String termino) {
	    if (termino == null || termino.trim().isEmpty()) {
	        return obtenerProyectosActivos();
	    }
	    
	    return proyectoDAO.buscarProyectos(termino.trim());
	}
	
	public String formatearFecha(LocalDateTime fecha) {
	    if (fecha == null) return "N/A";
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    
	    if (fecha.toLocalDate().equals(LocalDate.now())) {
	        return "Hoy a las " + fecha.format(DateTimeFormatter.ofPattern("HH:mm"));
	    }
	    
	    return fecha.format(formatter);
	}
	
	public Map<String, Integer> obtenerResumenTareas(int proyectoId) throws Exception {
	    List<Tarea> tareas = obtenerTareasDeProyecto(proyectoId);
	    Map<String, Integer> stats = new HashMap<>();
	    
	    int pendientes = (int) tareas.stream().filter(t -> !t.isCompletada()).count();
	    int completadas = (int) tareas.stream().filter(Tarea::isCompletada).count();
	    int urgentes = (int) tareas.stream()
	                        .filter(t -> !t.isCompletada() && t.getPrioridadTarea() == PrioridadTarea.ALTA)
	                        .count();
	    
	    stats.put("pendientes", pendientes);
	    stats.put("completadas", completadas);
	    stats.put("urgentes", urgentes);
	    
	    return stats;
	}
	
	public List<Tarea> obtenerTareasOrdenadasPorPrioridad(int proyectoId) throws Exception {
	    List<Tarea> tareas = obtenerTareasDeProyecto(proyectoId);
	    
	    // Ordena ALTA > MEDIA > BAJA
	    tareas.sort((t1, t2) -> t1.getPrioridadTarea().compareTo(t2.getPrioridadTarea()));
	    
	    return tareas;
	}
	
	private void validarLongitudTexto(String texto, int max, String campo) throws Exception {
	    if (texto != null && texto.length() > max) {
	        throw new Exception("El campo " + campo + " es demasiado largo (máximo " + max + " caracteres).");
	    }
	}
	
	
}
