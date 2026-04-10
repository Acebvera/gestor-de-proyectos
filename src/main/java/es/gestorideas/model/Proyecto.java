package es.gestorideas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
	private int id;
	private String titulo;
	private String descripcion;
	private EstatusProyecto estatus;
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;
	private boolean activo;
	private List<Tarea> tareas; //Relación 1 a muchos
	
	//Constructor vacío
	public Proyecto () {
		this.tareas = new ArrayList<Tarea>();
	}
	
	//Constructor para crear uno nuevo desde la interfaz
	public Proyecto(String titulo, String descripcion) {
		this();
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.estatus = EstatusProyecto.TO_DO;
		this.fechaCreacion = LocalDateTime.now();
		this.fechaActualizacion = LocalDateTime.now();
		this.activo = true;
	}
	
	//Constructor para cuando JDBC lee la base de datos
	public Proyecto (int id, String titulo, String descripcion, EstatusProyecto estatus, 
                    LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, boolean activo) {
		this();
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.fechaCreacion = fechaCreacion;
		this.fechaActualizacion = fechaActualizacion;
	}
	
	/**
	 * Calcula el porcentaje de progreso basado en tareas completadas.
	 * Si no hay tareas, el progreso es 0.
	 */
	public double getProgreso () {
		if (tareas == null || tareas.isEmpty()) {
			return 0.0;
		}
		
		long completadas = tareas.stream().filter(Tarea::isCompletada).count();
		
		for (Tarea tarea: tareas) {
			if (tarea.isCompletada()) {
				//...
			}
		}
		return (double) completadas / tareas.size() * 100;
	}

	public int getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public EstatusProyecto getEstatus() {
		return estatus;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public boolean isActivo() {
		return activo;
	}

	public List<Tarea> getTareas() {
		return tareas;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setEstatus(EstatusProyecto estatus) {
		this.estatus = estatus;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setTareas(List<Tarea> tareas) {
		this.tareas = tareas;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Proyecto [id=");
		builder.append(id);
		builder.append(", titulo=");
		builder.append(titulo);
		builder.append(", descripcion=");
		builder.append(descripcion);
		builder.append(", estatus=");
		builder.append(estatus);
		builder.append(", fechaCreacion=");
		builder.append(fechaCreacion);
		builder.append(", fechaActualizacion=");
		builder.append(fechaActualizacion);
		builder.append(", activo=");
		builder.append(activo);
		builder.append(", tareas=");
		builder.append(tareas);
		builder.append("]");
		return builder.toString();
	}
	
	
}
