package Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 *This class handles all communication from the server side controller to the Client_Comm class. 
 *	This is also the synchronize object for server side sync needs.
 */
public class ServerComm implements Runnable{

	private int port_;
	private String host_;
	private ObjectOutputStream oWriter_ = null;
	private ObjectInputStream oReader_ = null;
	private Socket connection_;
	private Controller server_;
	private boolean shutdown_;

	
	public ServerComm() {
		
	}
	public void run() {
		// create new controller and set this as its comm object
		server_ = new Controller();
		server_.setServerComm(this);
		shutdown_ = false;
		
		try {
			oWriter_ = new ObjectOutputStream(connection_.getOutputStream());
			oReader_ = new ObjectInputStream(connection_.getInputStream());
		} catch ( UnknownHostException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while( !shutdown_) {
			try {
				read();
			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		// continuous read
		}
		System.out.println("SERVER_COMM: Shutting Down");
	}
		
	public void read() throws IOException {
		
		String message;
		Object object;
		
			
			message =  receiveMessage();
			switch (message){
			
				// returns available GameVariants
				case "getGameVariants":
					sendMessage("GameVariants");
					sendObject(server_.getGameVariants());
					break;
					
					// resets current game
				case "resetGame":
					server_.resetGame();
					break;
					
					//starts 1 player game
				case "1 Player":
					object = receiveObject();
					server_.setPlayerMode(message);
					server_.newGame((GameVariant) object);
					break;
				//starts 2 player network game
				case "2 Player Network":
					object = receiveObject();
					server_.setPlayerMode(message);
					server_.newGame((GameVariant) object);
					break;
					
				//starts join game 
				case "Join Game":
					server_.setPlayerMode(message);
					break;
					
				//starts 2 player local game
				case "2 Player Local":
					object = receiveObject();
					server_.setPlayerMode(message);
					server_.newGame((GameVariant) object);
					break;
					
					//starts Spectate
				case "Spectate Network":
					server_.setPlayerMode(message);
					break;
					
					// submits move
				case "Move":
					object = receiveObject();
					server_.placeMove((Move) object);
					break;
					
					//gets a list of available games to join (not implemented)
				case "getJoinList":
					sendMessage("JoinList");
					sendObject(Listener.getControllers());
					break;
					
				case "shutdown":
					server_.shutdown();
					shutdown_ = true;
					break;
					
				default:
					System.out.println("Switch default");
			}
		
	}
	// the following 4 methods are communication methods to/from the client Comm
	public void sendMessage(String message) {
		try {
			oWriter_.reset();
			oWriter_.writeObject(message);
			oWriter_.flush();
			System.out.println("SERVER_COMM: wrote String " + message);
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	
  	
	}
	
	
	public String receiveMessage() {
		try {
			String message = (String) oReader_.readObject();
			System.out.println("SERVER_COMM: read String " + message);
			return(message);
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( ClassNotFoundException e ) {
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
			System.out.println("SERVER_COMM: wrote object: " + String.valueOf(object));
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object receiveObject() {
		try {
			Object object = oReader_.readObject();
			System.out.println("SERVER_COMM: read object: " + String.valueOf(object));
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
	
		// sets the connection_ instance var to the supplied socket
	public void setConnection ( Socket s ) {
		connection_ = s;
	}
		
		// returns the current thread
	public Thread getThread() {
		return Thread.currentThread();
	}

	
}
