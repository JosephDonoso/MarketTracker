package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfazDeServer extends Remote {
	ArrayList<Usuario> getUsuarios() throws RemoteException;
	ArrayList<Mercado> infoGeneralMercado() throws RemoteException;
	Mercado infoEspecificaMercado(String id) throws RemoteException;
	String getDataFromApi() throws RemoteException;
	Boolean registrarUsuario(Usuario newUsuario) throws RemoteException;
	Usuario iniciarSesion(String rut) throws RemoteException;
	void guardar() throws RemoteException;
}

