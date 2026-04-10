package es.gestorideas.model;

public enum EstatusProyecto {
	TO_DO("Por hacer"),
	DOING("En progreso"),
	DONE("Finalizado");
	
	private String nombre;
	
	EstatusProyecto(String nombre) { 
		this.nombre = nombre; 
	}
	
	public String getNombre () {
		return nombre;
	}
}
