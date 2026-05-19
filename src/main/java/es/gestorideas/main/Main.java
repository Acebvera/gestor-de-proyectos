package es.gestorideas.main;

import java.util.List;

import es.gestorIdeas.dao.ProyectoDAO;
import es.gestorIdeas.database.ConexionDB;
import es.gestorideas.model.PrioridadTarea;
import es.gestorideas.model.Proyecto;
import es.gestorideas.model.Tarea;
import es.gestorideas.service.Servicio;

public class Main {
    public static void main(String[] args) {
        Servicio s = new Servicio();

        try {
            //Crear proyecto simple
            Proyecto p1 = new Proyecto("Proyecto Simple", "Solo proyecto sin tareas");
            s.crearProyecto(p1);

            //Crear proyecto con tareas
            Proyecto p2 = new Proyecto("Proyecto Completo", "Proyecto con tareas iniciales");
            List<Tarea> tareasP2 = generarTareasPrueba(2); 
            //s.crearProyectoConTareas(p2, tareasP2);

            //Añadir tarea a proyecto
            Tarea t2 = new Tarea(1, "Nueva tarea añadida", "Se añade después", PrioridadTarea.MEDIA);
            s.crearTarea(t2);

            //Eliminar tarea de proyecto
            s.eliminarTareaDeProyecto(1, 1); // (proyectoId, tareaId)
            
            s.obtenerProyectosActivos();
            s.obtenerProyecto(2);
            s.eliminarProyectoCompleto(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Tarea> generarTareasPrueba(int proyectoId) {
        return List.of(
            new Tarea(proyectoId, "Diseñar modelo BD", "Tablas iniciales", PrioridadTarea.ALTA),
            new Tarea(proyectoId, "Crear API REST", "Endpoints básicos", PrioridadTarea.MEDIA),
            new Tarea(proyectoId, "Implementar UI", "Pantallas principales", PrioridadTarea.MEDIA),
            new Tarea(proyectoId, "Escribir tests", "JUnit básico", PrioridadTarea.BAJA)
        );
    }
}