package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.InterfazDeServer;
import common.Mercado;
import common.Usuario;

public class ServerImpl implements InterfazDeServer{

    private boolean inUse;

	public ServerImpl() throws RemoteException {
		UnicastRemoteObject.exportObject(this, 0);
	}
	
	//crear arreglo para respaldo de bd
	private ArrayList<Usuario> bd_usuario_copia = new ArrayList<>();
	private ArrayList<Mercado> bd_mercado_copia = new ArrayList<>(); 
	
	@Override
	public Boolean registrarUsuario(Usuario newUsuario) throws RemoteException{
        functionRequestMutex();
		cargarBDUsuarios();
        Boolean noEncontrado = true;
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(newUsuario.getRut())){
                noEncontrado = false;
            } 
		}
		bd_usuario_copia.add(newUsuario);
		guardarBDUsuarios();
        releaseMutex();
		return noEncontrado;
	}
	
	@Override
	public Usuario iniciarSesion(String rut) throws RemoteException{
        functionRequestMutex();
		cargarBDUsuarios();
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(rut)){
                releaseMutex();
                return usuario;  
            } 
		}

		releaseMutex();
		return null;
	}
	
	@Override
	public void cambiarNombre(String rut, String nombre) throws RemoteException {
        functionRequestMutex();
        try {
            Thread.sleep(5000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
		cargarBDUsuarios();
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(rut)) {
				usuario.setNombre(nombre);
				usuario.setEstado("Actualizado");
			}
		}
		guardarBDUsuarios();
        releaseMutex();
	}
	
	@Override
	public void eliminarCuenta(String rut) throws RemoteException {
        functionRequestMutex();
		cargarBDUsuarios();
		for(Usuario usuario : bd_usuario_copia){
			if(usuario.getRut().equals(rut)) {
				usuario.setEstado("Eliminado");
			}
		}
		guardarBDUsuarios();
		releaseMutex();
	}
	
	
	@Override
	public ArrayList<Usuario> getUsuarios() throws RemoteException {
        functionRequestMutex();
		cargarBDUsuarios();
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
			ArrayList<Mercado> mercadosArray = new ArrayList<>();
			for (JsonNode market : marketsNode) {
				String codigo = market.get("id").asText();
				String moneda_cambio = market.get("base_currency").asText();
				String moneda_pago = market.get("quote_currency").asText();
				double monto_min_orden = market.get("minimum_order_amount").get(0).asDouble();
				Mercado mercado = new Mercado(codigo, moneda_cambio, moneda_pago, monto_min_orden);
				mercadosArray.add(mercado);
            }
			return mercadosArray;
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
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
			LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formatoTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaTimestamp = fechaActual.format(formatoTimestamp);
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
			Mercado mercado = new Mercado(market_id, moneda_cambio, moneda_pago, last_price, min_ask, max_bid, volume, price_variation_24h, price_variation_7d, fechaTimestamp);
			
		return mercado;
			
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}

    @Override
    public Boolean addMercadoToFavoritos(String rut, Mercado mercado_to_favorito) throws RemoteException{
        functionRequestMutex();
        cargarBDMercados();
        mercado_to_favorito.setRut_cliente(rut);

        Boolean noEncontrado = true;
        for(Mercado mercado : bd_mercado_copia){
            if(rut.equals(mercado.getRut_cliente()) && mercado_to_favorito.getId().equals(mercado.getId())){
                noEncontrado = false;
               //mercado = mercado_to_favorito; //Actualizar
               mercado.setFecha_consulta(mercado_to_favorito.getFecha_consulta());
               mercado.setMax_precio_compra(mercado_to_favorito.getMax_precio_compra());
               mercado.setUltimo_precio(mercado_to_favorito.getUltimo_precio());
               mercado.setMin_precio_venta(mercado_to_favorito.getMin_precio_venta());
               mercado.setVolumen(mercado_to_favorito.getVolumen());
               mercado.setVariacion_precio_24h(mercado_to_favorito.getVariacion_precio_24h());
               mercado.setVariacion_precio_7d(mercado_to_favorito.getVariacion_precio_7d());
               mercado.setEstado("Actualizado");
            }
        }
        if(noEncontrado){
            mercado_to_favorito.setEstado("Añadido");
            bd_mercado_copia.add(mercado_to_favorito);
        }
        guardarBDMercados();
        releaseMutex();
        return noEncontrado;
    }

    @Override
    public ArrayList<Mercado> getMercadosFavoritos(String rut) throws RemoteException{
        functionRequestMutex();
        cargarBDMercados();
        ArrayList<Mercado> mercadosFavoritos = new ArrayList<>();
        for(Mercado mercado : bd_mercado_copia){
            if(rut.equals(mercado.getRut_cliente())){
                mercadosFavoritos.add(mercado);
            } 
        }
        releaseMutex();
        return mercadosFavoritos;
    }

    @Override
    public Boolean eliminarMercadoFavoritoUsuario(String rut, String id_mercado) throws RemoteException{
        functionRequestMutex();
        cargarBDMercados();
        Boolean encontrado = false;
        for(Mercado mercado : bd_mercado_copia){
            if(rut.equals(mercado.getRut_cliente()) && id_mercado.equals(mercado.getId())){
               mercado.setEstado("Eliminado");
               encontrado = true;
            }
        }
        guardarBDMercados();
        releaseMutex();
        return encontrado;
    }
	
	public void cargarBDUsuarios() {
		bd_usuario_copia = new ArrayList<>();
		Connection connection = null;
		Statement query = null;
		ResultSet resultados = null;
		
		
		try {
			String url = "jdbc:mysql://localhost:3306/markettracker";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			query = connection.createStatement();
			String sql = "SELECT * FROM usuarios";
			resultados = query.executeQuery(sql);
			while (resultados.next()) {
				String rut = resultados.getString("rut");
				String nombre = resultados.getString("nombre");
				Usuario newUsuario = new Usuario(rut, nombre);
				
				bd_usuario_copia.add(newUsuario);
			}
			
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD :C");
		}
	}
	
	public void cargarBDMercados() {
        bd_mercado_copia = new ArrayList<>();
        Connection connection = null;
        Statement query = null;
        ResultSet resultados = null;
        
        try {
            String url = "jdbc:mysql://localhost:3306/markettracker";
            String username = "root";
            String password_BD = "";
            
            connection = DriverManager.getConnection(url, username, password_BD);
            query = connection.createStatement();
            String sql = "SELECT * FROM mercados";
            resultados = query.executeQuery(sql);
            while (resultados.next()) {
                String id = resultados.getString("id_mercado");
                String rut_cliente = resultados.getString("rut_cliente");
                String moneda_cambio = resultados.getString("moneda_cambio");
                String moneda_pago = resultados.getString("moneda_pago");
                double ultimo_precio = resultados.getDouble("ultimo_precio");
                double min_precio_venta = resultados.getDouble("min_precio_venta");
                double max_precio_compra = resultados.getDouble("max_precio_compra");
                double volumen = resultados.getDouble("volumen");
                double variacion_precio_24h = resultados.getDouble("variacion_precio_24h");
                double variacion_precio_7d = resultados.getDouble("variacion_precio_7d");
                String fecha_consulta = resultados.getString("fecha_consulta");
                
                Mercado newMercado = new Mercado(id, rut_cliente, moneda_cambio, moneda_pago, 
                                                 ultimo_precio, min_precio_venta, max_precio_compra, volumen, 
                                                 variacion_precio_24h, variacion_precio_7d, fecha_consulta);
                bd_mercado_copia.add(newMercado);
            }
            
            connection.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar a la BD :C");
        }
    }
	
	public void guardarBDMercados() throws RemoteException {
        Connection connection = null;
        PreparedStatement preparedStatement_insert = null;
        PreparedStatement preparedStatement_update = null;
        PreparedStatement preparedStatement_delete = null;

        try {
            String url = "jdbc:mysql://localhost:3306/markettracker";
            String username = "root";
            String password_BD = "";
            
            connection = DriverManager.getConnection(url, username, password_BD);
            
            String sql_insert = "INSERT INTO `mercados` (`id_mercado`, `rut_cliente`, `moneda_cambio`, `moneda_pago`, `ultimo_precio`, `min_precio_venta`, `max_precio_compra`, `volumen`, `variacion_precio_24h`, `variacion_precio_7d`, `fecha_consulta`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sql_update = "UPDATE `mercados` SET `moneda_cambio` = ?, `moneda_pago` = ?, `ultimo_precio` = ?, `min_precio_venta` = ?, `max_precio_compra` = ?, `volumen` = ?, `variacion_precio_24h` = ?, `variacion_precio_7d` = ?, `fecha_consulta` = ? WHERE `id_mercado` = ? AND `rut_cliente` = ?";
            String sql_delete = "DELETE FROM `mercados` WHERE `id_mercado` = ? AND `rut_cliente` = ?";
            
            preparedStatement_insert = connection.prepareStatement(sql_insert);
            preparedStatement_update = connection.prepareStatement(sql_update);
            preparedStatement_delete = connection.prepareStatement(sql_delete);
            
            String id_mercado, rut_cliente, moneda_cambio, moneda_pago, fecha_consulta;
            double ultimo_precio, min_precio_venta, max_precio_compra, volumen, variacion_precio_24h, variacion_precio_7d;
            boolean seRealizanCambios = false;
            for (Mercado mercado : bd_mercado_copia) {
                String estado = mercado.getEstado();
                id_mercado = mercado.getId();
                rut_cliente = mercado.getRut_cliente();
                moneda_cambio = mercado.getMoneda_cambio();
                moneda_pago = mercado.getMoneda_pago();
                ultimo_precio = mercado.getUltimo_precio();
                min_precio_venta = mercado.getMin_precio_venta();
                max_precio_compra = mercado.getMax_precio_compra();
                volumen = mercado.getVolumen();
                variacion_precio_24h = mercado.getVariacion_precio_24h();
                variacion_precio_7d = mercado.getVariacion_precio_7d();
                fecha_consulta = mercado.getFecha_consulta();

                if (estado.equals("Añadido")) {
                    seRealizanCambios = true;
                    preparedStatement_insert.setString(1, id_mercado);
                    preparedStatement_insert.setString(2, rut_cliente);
                    preparedStatement_insert.setString(3, moneda_cambio);
                    preparedStatement_insert.setString(4, moneda_pago);
                    preparedStatement_insert.setDouble(5, ultimo_precio);
                    preparedStatement_insert.setDouble(6, min_precio_venta);
                    preparedStatement_insert.setDouble(7, max_precio_compra);
                    preparedStatement_insert.setDouble(8, volumen);
                    preparedStatement_insert.setDouble(9, variacion_precio_24h);
                    preparedStatement_insert.setDouble(10, variacion_precio_7d);
                    preparedStatement_insert.setString(11, fecha_consulta);
                    preparedStatement_insert.addBatch();
                } else if (estado.equals("Actualizado")) {
                    seRealizanCambios = true;
                    preparedStatement_update.setString(1, moneda_cambio);
                    preparedStatement_update.setString(2, moneda_pago);
                    preparedStatement_update.setDouble(3, ultimo_precio);
                    preparedStatement_update.setDouble(4, min_precio_venta);
                    preparedStatement_update.setDouble(5, max_precio_compra);
                    preparedStatement_update.setDouble(6, volumen);
                    preparedStatement_update.setDouble(7, variacion_precio_24h);
                    preparedStatement_update.setDouble(8, variacion_precio_7d);
                    preparedStatement_update.setString(9, fecha_consulta);
                    preparedStatement_update.setString(10, id_mercado);
                    preparedStatement_update.setString(11, rut_cliente);
                    preparedStatement_update.addBatch();
                } else if (estado.equals("Eliminado")) {
                    seRealizanCambios = true;
                    preparedStatement_delete.setString(1, id_mercado);
                    preparedStatement_delete.setString(2, rut_cliente);
                    preparedStatement_delete.addBatch();
                }
            }
            
            if (seRealizanCambios) {
                preparedStatement_insert.executeBatch();
                preparedStatement_update.executeBatch();
                preparedStatement_delete.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar a la BD :C");
        } finally {
            try {
                if (preparedStatement_insert != null) {
                    preparedStatement_insert.close();
                }
                if (preparedStatement_update != null) {
                    preparedStatement_update.close();
                }
                if (preparedStatement_delete != null) {
                    preparedStatement_delete.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void guardarBDUsuarios() throws RemoteException {
        Connection connection = null;
        PreparedStatement preparedStatement_insert = null;
        PreparedStatement preparedStatement_update = null;
        PreparedStatement preparedStatement_delete = null;
        
        try {
            String url = "jdbc:mysql://localhost:3306/markettracker";
            String username = "root";
            String password_BD = "";
            
            connection = DriverManager.getConnection(url, username, password_BD);
            
            String sql_insert = "INSERT INTO `usuarios` (`rut`, `nombre`) VALUES (?, ?)";
            String sql_update = "UPDATE `usuarios` SET `nombre` = ? WHERE `rut` = ?";
            String sql_delete = "DELETE FROM `usuarios` WHERE `rut` = ?";
            
            preparedStatement_insert = connection.prepareStatement(sql_insert);
            preparedStatement_update = connection.prepareStatement(sql_update);
            preparedStatement_delete = connection.prepareStatement(sql_delete);
            
            String nombre, rut, estado;
            boolean seRealizanCambios = false;
            for (Usuario usuario : bd_usuario_copia) {
                estado = usuario.getEstado();
                if (estado.equals("Actualizado")) {
                    seRealizanCambios = true;
                    rut = usuario.getRut();
                    nombre = usuario.getNombre();
                    
                    preparedStatement_update.setString(1, nombre);
                    preparedStatement_update.setString(2, rut);
                    preparedStatement_update.addBatch();
                }
                
                if (estado.equals("Añadido")) {
                    seRealizanCambios = true;
                    rut = usuario.getRut();
                    nombre = usuario.getNombre();
                    
                    preparedStatement_insert.setString(1, rut);
                    preparedStatement_insert.setString(2, nombre);
                    preparedStatement_insert.addBatch();
                }

                if (estado.equals("Eliminado")) {
                    seRealizanCambios = true;
                    rut = usuario.getRut();
                    
                    preparedStatement_delete.setString(1, rut);
                    preparedStatement_delete.addBatch();
                }
            }
            
            if (seRealizanCambios) {
                preparedStatement_insert.executeBatch();
                preparedStatement_update.executeBatch();
                preparedStatement_delete.executeBatch();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar a la BD :C");
        } finally {
            try {
                if (preparedStatement_delete != null) {
                    preparedStatement_delete.close();
                }
                if (preparedStatement_update != null) {
                    preparedStatement_update.close();
                }
                if (preparedStatement_insert != null) {
                    preparedStatement_insert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void functionRequestMutex() throws RemoteException {
        while(true) {
			if(requestMutex()) {
				System.out.println("Base de datos liberada para su uso");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Esperando acceso a la base de datos...");
		} 
    }

    @Override
	public synchronized boolean requestMutex() throws RemoteException {
		if(inUse) {
			return false;
		} else {
			inUse = true;
			return true;
		}
	}

	@Override
	public synchronized void releaseMutex() throws RemoteException {
		inUse = false;	
	}

}

