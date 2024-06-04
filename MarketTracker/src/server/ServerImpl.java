package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.InterfazDeServer;
import common.Mercado;
import common.Usuario;

public class ServerImpl implements InterfazDeServer{

	public ServerImpl() throws RemoteException {
		conectarBD();
		UnicastRemoteObject.exportObject(this, 0);
	}
	
	//crear arreglo para respaldo de bd
	private ArrayList<Usuario> bd_usuario_copia = new ArrayList<>();
	private ArrayList<Mercado> bd_mercado_copia = new ArrayList<>(); //Futuras entregas
	
	public void conectarBD() {
		Connection connection = null;
		Statement query = null;
		//PreparedStatement test = null;
		ResultSet resultados = null;
		
		
		try {
			String url = "jdbc:mysql://localhost:3306/markettracker";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			
			//TODO Metodos con la BD
			query = connection.createStatement();
			String sql = "SELECT * FROM usuarios";
			//INSERT para agregar datos a la BD, PreparedStatement
			
			resultados = query.executeQuery(sql);
			while (resultados.next()) {
				String rut = resultados.getString("rut");
				String nombre = resultados.getString("nombre");
				Usuario newUsuario = new Usuario(rut, nombre);
				
				bd_usuario_copia.add(newUsuario);
				
			}
			/*
			String sql1 = "SELECT * FROM mercados";
			resultados = query.executeQuery(sql1);
			while (resultados.next()) {
				String rut = resultados.getString("rut");
				String nombre = resultados.getString("nombre");
				Usuario newUsuario = new Usuario(rut, nombre);
				bd_usuario_copia.add(newUsuario);
			}
			*/

			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD :C");
		}
	}
	
	@Override
	public ArrayList<Usuario> getUsuarios() throws RemoteException {
		// TODO Auto-generated method stub
		return bd_usuario_copia;
	}

	@Override
	public String getDataFromApi() {
		String output = null;
		 
		try {
            // URL de la API REST, el listado de APIs públicas está en: 
			// https://github.com/juanbrujo/listado-apis-publicas-en-chile
            URL apiUrl = new URL("https://www.buda.com/api/v2/markets");

            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configura la solicitud (método GET en este ejemplo)
            connection.setRequestMethod("GET");

            // Obtiene el código de respuesta
            int responseCode = connection.getResponseCode();

            // Procesa la respuesta si es exitosa
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Cierra la conexión y muestra la respuesta
                in.close();
                output = response.toString();
            } else {
                System.out.println("Error al conectar a la API. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		//Como resultado tenemos un String output que contiene el JSON de la respuesta de la API
		return output;
	}

	@Override
	public ArrayList<Mercado> infoGeneralMercado() throws RemoteException {
		String output = null;
		 
		try {
            // URL de la API REST, el listado de APIs públicas está en: 
			// https://github.com/juanbrujo/listado-apis-publicas-en-chile
            URL apiUrl = new URL("https://www.buda.com/api/v2/markets");

            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configura la solicitud (método GET en este ejemplo)
            connection.setRequestMethod("GET");

            // Obtiene el código de respuesta
            int responseCode = connection.getResponseCode();

            // Procesa la respuesta si es exitosa
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Cierra la conexión y muestra la respuesta
                in.close();
                output = response.toString();
            } else {
                System.out.println("Error al conectar a la API. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		//Como resultado tenemos un String output que contiene el JSON de la respuesta de la API
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			/**/ 
			JsonNode jsonNode = objectMapper.readTree(output);
			JsonNode marketsNode = jsonNode.get("markets");
			List<String> mercadosList = new ArrayList<>();
			ArrayList<Mercado> mercadosArray = new ArrayList<>();
			for (JsonNode market : marketsNode) {
				String codigo = market.get("id").asText();
				String moneda_cambio = market.get("base_currency").asText();
				String moneda_pago = market.get("quote_currency").asText();
				double monto_min_orden = market.get("minimum_order_amount").get(0).asDouble();
				Mercado mercado = new Mercado(codigo, moneda_cambio, moneda_pago, monto_min_orden);
				mercadosArray.add(mercado);
            }
			//System.out.println(mercadosArray.length);
			//return new Object[] {mercadosArray[0]};
			//return new Object[] {codigo, nombre, fecha, unidad_medida, valor};
			return mercadosArray;
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Boolean registrarUsuario(Usuario newUsuario) throws RemoteException{
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(newUsuario.getRut())) return false;
		}
		bd_usuario_copia.add(newUsuario);
		return true;
	}

	@Override
	public Usuario iniciarSesion(String rut) throws RemoteException{
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(rut)) return usuario;
		}

		return null;
	}

	@Override
	public Mercado infoEspecificaMercado(String id) throws RemoteException {
		String output = null;
		 
		try {
            // URL de la API REST, el listado de APIs públicas está en: 
			// https://github.com/juanbrujo/listado-apis-publicas-en-chile
            URL apiUrl = new URL("https://www.buda.com/api/v2/markets/" + id + "/ticker");

            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configura la solicitud (método GET en este ejemplo)
            connection.setRequestMethod("GET");

            // Obtiene el código de respuesta
            int responseCode = connection.getResponseCode();

            // Procesa la respuesta si es exitosa
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Cierra la conexión y muestra la respuesta
                in.close();
                output = response.toString();
            } else {
                System.out.println("Error al conectar a la API. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		//Como resultado tenemos un String output que contiene el JSON de la respuesta de la API
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			/**/ 
			
			JsonNode jsonNode = objectMapper.readTree(output);
			JsonNode marketsNode = jsonNode.get("ticker");
			
			String market_id = marketsNode.get("market_id").asText();
			String[] partes = market_id.split("-");
	        String moneda_cambio = partes[0];
	        String moneda_pago = partes[1];
			double last_price = marketsNode.get("last_price").get(0).asDouble();
			double min_ask = marketsNode.get("min_ask").get(0).asDouble();
			double max_bid = marketsNode.get("max_bid").get(0).asDouble();
			double volume = marketsNode.get("volume").get(0).asDouble();
			double price_variation_24h = marketsNode.get("price_variation_24h").asDouble();
			double price_variation_7d = marketsNode.get("price_variation_7d").asDouble();
			Mercado mercado = new Mercado(market_id, moneda_cambio, moneda_pago, last_price, min_ask, max_bid, volume, price_variation_24h, price_variation_7d);
			
		return mercado;
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void guardar() throws RemoteException{
		Connection connection = null;
		Statement query = null;
		
		try {
			String url = "jdbc:mysql://localhost:3306/markettracker";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			
			//TODO Metodos con la BD
			//INSERT INTO `usuarios` (`rut`, `nombre`) VALUES ('123', 'Jorge'), ('111', 'Seba'), ('222', 'Joseph')
			query = connection.createStatement();
			String sql = "INSERT INTO `usuarios` (`rut`, `nombre`) VALUES ";
			
			String values = "";
			String nombre, rut, estado;
			Boolean seAñade = false;
			for(Usuario usuario : bd_usuario_copia){
				estado = usuario.getEstado();
				if(estado.equals("Añadido")){
					seAñade = true;
					values += "('";
					rut = usuario.getRut();
					values += rut;
					values += "', '";
					nombre = usuario.getNombre();
					values += nombre;
					values += "'), ";
				}
			}
			if (seAñade){
				sql += values;
				int ultimaComaIndex = sql.lastIndexOf(',');
				sql = sql.substring(0, ultimaComaIndex);
				query.executeUpdate(sql);
			}
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD :C");
		}
	}
}

