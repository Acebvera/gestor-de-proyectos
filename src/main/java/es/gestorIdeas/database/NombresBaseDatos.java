package es.gestorIdeas.database;

public class NombresBaseDatos {
	
	//Evitar crear instancia de esta clase7
	private NombresBaseDatos() {}
	
	//Tabla proyecto
	public static final String TABLA_PROYECTO = "proyectos";
	public static final String COLUMNA_PROYECTO_ID = "id";
	public static final String COLUMNA_PROYECTO_TITULO = "titulo";
	public static final String COLUMNA_PROYECTO_DESCRIPCION = "descripcion";
	public static final String COLUMNA_PROYECTO_ESTATUS = "estatus";
	public static final String COLUMNA_PROYECTO_FECHA_CREACION = "fecha_creacion";
	public static final String COLUMNA_PROYECTO_FECHA_ACTUALIZACION = "fecha_actualizacion";
	public static final String COLUMNA_PROYECTO_ACTIVO = "activo";
	
	//Tabla tarea
	public static final String TABLA_TAREA = "tareas";
	public static final String COLUMNA_TAREA_ID = "id";
	public static final String COLUMNA_TAREA_PROYECTO_ID = "proyecto_id";
	public static final String COLUMNA_TAREA_NOMBRE = "nombre";
	public static final String COLUMNA_TAREA_NOTA_ADICIONAL = "nota_adicional";
	public static final String COLUMNA_TAREA_PRIORIDAD = "prioridad";
	public static final String COLUMNA_TAREA_COMPLETADA = "completada";
	public static final String COLUMNA_TAREA_ACTIVO = "activo";
}
