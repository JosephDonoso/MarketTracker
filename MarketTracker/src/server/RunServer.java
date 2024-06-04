package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import common.InterfazDeServer;

public class RunServer {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		InterfazDeServer server = new ServerImpl();
		Registry registry = LocateRegistry.createRegistry(5000);
		registry.bind("server", server);
		System.out.println("Servidor arriba!!");
		
	}
}
