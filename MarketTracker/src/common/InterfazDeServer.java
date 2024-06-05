package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfazDeServer extends Remote {
	Boolean registrarUsuario(Usuario newUsuario) throws RemoteException;
	Usuario iniciarSesion(String rut) throws RemoteException;
	void cambiarNombre(String rut, String nombre) throws RemoteException;
	void eliminarCuenta(String rut) throws RemoteException;
	ArrayList<Mercado> infoGeneralMercado() throws RemoteException;
	Mercado infoEspecificaMercado(String id) throws RemoteException;
	Boolean addMercadoToFavoritos(String rut, Mercado mercado) throws RemoteException;
	ArrayList<Mercado> getMercadosFavoritos(String rut) throws RemoteException;
	Boolean eliminarMercadoFavoritoUsuario(String rut, String id_mercado) throws RemoteException;
	String getDataFromApi() throws RemoteException;
	ArrayList<Usuario> getUsuarios() throws RemoteException;
	// TODO MUTEX
	boolean requestMutex() throws RemoteException;
	void releaseMutex() throws RemoteException;
}

