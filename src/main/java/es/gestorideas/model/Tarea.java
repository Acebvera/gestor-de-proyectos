package es.gestorideas.model;

public class Tarea {
	private int id;
	private String nombre;
	private String notaAdicional;
	private PrioridadTarea prioridad;
	private boolean completada;
	private boolean activo; // Para eliminación lógica
	
	public Tarea () {
		this.id = 0;
		this.nombre = "";
		this.notaAdicional = "";
		this.prioridad = PrioridadTarea.BAJA;
		this.completada = false;
		this.activo = true;
	}
	
	public Tarea(int id, String nombre, String notaAdicional, 
			PrioridadTarea prioridadTarea, boolean completada, boolean activo) {
		
		this.id = id;
		this.nombre = nombre;
		this.notaAdicional = notaAdicional;
		this.prioridad = prioridadTarea;
		this.completada = completada;
		this.activo = activo;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNotaAdicional() {
		return notaAdicional;
	}

	public PrioridadTarea getPrioridadTarea() {
		return prioridad;
	}

	public boolean isCompletada() {
		return completada;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNotaAdicional(String notaAdicional) {
		this.notaAdicional = notaAdicional;
	}

	public void setPrioridadTarea(PrioridadTarea prioridadTarea) {
		this.prioridad = prioridadTarea;
	}

	public void setCompletada(boolean completada) {
		this.completada = completada;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tarea [id=");
		builder.append(id);
		builder.append(", nombre=");
		builder.append(nombre);
		builder.append(", notaAdicional=");
		builder.append(notaAdicional);
		builder.append(", prioridadTarea=");
		builder.append(prioridad);
		builder.append(", completada=");
		builder.append(completada);
		builder.append(", activo=");
		builder.append(activo);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
