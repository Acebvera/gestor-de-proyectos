package es.gestorideas.model;

public enum PrioridadTarea {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String nombre;

    PrioridadTarea(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

   
    public static PrioridadTarea fromString(String valor) {
        if (valor == null) return MEDIA;

        for (PrioridadTarea p : values()) {
            if (p.name().equalsIgnoreCase(valor) || p.nombre.equalsIgnoreCase(valor)) {
                return p;
            }
        }
        
        System.err.println("Prioridad desconocida detectada: " + valor + ". Asignando MEDIA.");
        return MEDIA;
    }
}