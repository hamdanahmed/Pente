package Core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *  Listener for the networking component
 *
 */
public class Listener implements Runnable{

	
	/**
	 * Main server.
	 */
	
	static Socket connection_;
	static boolean shutdown_;
	static ServerSocket socket_;
	static Thread thread1_;
	static List<Thread> threads_;
	static List<ServerComm> serverComms_;
	static Stack<Controller> controllers_;
	static List<Game> observeGames_;
	
	
	// moving to run() method to auto launch Listener with Game Main 
	public static void main ( String[] args ) {
		Listener listener = new Listener();
		listener.run();
	}
	
	public void run() {
		threads_ = new ArrayList<Thread>();
		serverComms_ = new ArrayList<ServerComm>();
		controllers_ = new Stack<Controller>();
		observeGames_ = new ArrayList<Game>();
		shutdown_ = false;
		

		try {
			socket_ = new ServerSocket(9000);
			System.out.println("LISTENER: Started");

			while ( !shutdown_ ) {
				connection_ = socket_.accept();
				ServerComm serverComm = new ServerComm();
				serverComm.setConnection(connection_);
				thread1_ = new Thread(serverComm);
				thread1_.start();
				threads_.add(thread1_);
				serverComms_.add(serverComm);

			}
			socket_.close();

		} catch ( IOException e ) {
			System.out.println("Terminating Program...");
		}
		for ( Thread t : threads_ ) {
			if ( t.isAlive() ) {
				try {
					t.join(5000);
					t.interrupt();
				} catch ( InterruptedException e ) {}
			}
		}
		System.out.println("LISTENER: Shutdown");
		System.exit(0);
	}
	
	static public void registerController(Controller ctl) {
		controllers_.push(ctl);
	}
	
	static public void unregisterController(Controller ctl) {
		controllers_.remove(ctl);
	}
	
	static public Stack<Controller> getControllers(){
		return controllers_;		
	}

	static public Controller getController() {
		if(controllers_.empty()) {
			return null;
		}
		return controllers_.pop();
	}
	
	static public void registerGame(Game game) {
		observeGames_.add(game);
	}
	
	static public void unregisterGame(Game game) {
		observeGames_.remove(game);
	}
	
	static public List<Game> getGames(){
		return observeGames_;		
	}

	static public Game getGame() {
		if(observeGames_.isEmpty()) {
			return null;
		}
		return observeGames_.get(0);
	}
	
	static public void shutdown() throws IOException {
		shutdown_ = true;
		socket_.close();
	}
}
