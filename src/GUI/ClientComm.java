package GUI;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import Core.Controller;
import Core.GameState;
import Core.GameVariant;

/**
 * 
 *	This class handles all communication from the client GUI Controller to the Server_Comm class. 
 *	This is also the synchronize object for client side sync needs.
 */
public class ClientComm implements Runnable{

	private int port_;
	private String host_;
	public ObjectOutputStream oWriter_;
	public ObjectInputStream oReader_;
	private Socket connection_;
	private MainController client_;
	private boolean shutdown_;
	
	public ClientComm() {
	}
	
		
	public void run() {
		
		shutdown_ = false;
		try {
			connection_ = new Socket(host_,port_);
			oWriter_ = new ObjectOutputStream(connection_.getOutputStream());
			oReader_ = new ObjectInputStream(connection_.getInputStream());
		} catch ( UnknownHostException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (this) {
			this.notifyAll();
		}
		read();	
	}
	
	
	// handles all read operations blocks waiting to read from server
	public void read() {
		
		String message;
		Object object;
		
		while( !shutdown_) {
			message = receiveMessage();
			switch (message){
			
				case "GameState":		// incoming GameState object
					object = receiveObject();
					client_.setGameState((GameState) object);
					break;
					
				case "EndGame":
					
				case "GameVariants":	// incoming GameVariants List object
					object = receiveObject();
					client_.setGameVariants((List<GameVariant>) object);
					break;

			}
			synchronized(this) {
  			this.notifyAll();
      }
		}
		
	}
	// the following 4 communication methods are used for all communication
	
	public void sendMessage(String message) {
		
		try {
			oWriter_.reset();
			oWriter_.writeObject(message);
			oWriter_.flush();
			System.out.println("GUI_COMM: wrote String: " + message);
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String receiveMessage() {
		try {
			String message = (String) oReader_.readObject();
			System.out.println("GUI_COMM: read String: " + message);
			return(message);
		} catch ( IOException | ClassNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendObject(Object object) {
		try {
			oWriter_.reset();
			oWriter_.writeObject(object);
			oWriter_.flush();
			System.out.println("GUI_COMM: wrote object: " + String.valueOf(object));
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	
	}
	
	public Object receiveObject() {
		try {
			Object object = oReader_.readObject();
			System.out.println("GUI_COMM: read object: " + String.valueOf(object));
			return(object);
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( ClassNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// sets the client which uses this comm class
	public void setClient(MainController client) {
		client_ = client;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort ( int port ) {
		port_ = port;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost ( String host ) {
		host_ = host;
	}
}
