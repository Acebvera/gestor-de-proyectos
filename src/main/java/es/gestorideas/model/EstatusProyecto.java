package es.gestorideas.model;

public enum EstatusProyecto {
	
	TO_DO("Por hacer"),
	DOING("En progreso"),
	DONE("Finalizado");

	private final String nombre;

	EstatusProyecto(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}

	
    public static EstatusProyecto fromString(String valor) {
        if (valor == null) return TO_DO; 

        for (EstatusProyecto e : values()) {
            if (e.nombre.equalsIgnoreCase(valor) || e.name().equalsIgnoreCase(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estatus desconocido: " + valor);
    }
}
