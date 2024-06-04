package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String rut;
	private String nombre;
	private ArrayList<Mercado> mercados_consultados = new ArrayList<>(); //Agregado en futuras entregas
    private String estado; //Ninguno - Actualizado - Borrado - Añadido
	
	public Usuario(String rut, String nombre) {
		this.setRut(rut);
		this.setNombre(nombre);
		this.setEstado("Ninguno"); 	
	}

	public Usuario(String rut, String nombre, String estado) {
		this.setRut(rut);
		this.setNombre(nombre);
		this.setEstado(estado); 	
	}
	
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<Mercado> getMercados_consultados() {
		return mercados_consultados;
	}

	public void setMercados_consultados(ArrayList<Mercado> mercados_consultados) {
		this.mercados_consultados = mercados_consultados;
	}
	
}