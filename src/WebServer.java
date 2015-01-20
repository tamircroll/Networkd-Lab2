import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author kashi
 *
 */
public class WebServer {
	
	private int port, maxThreads;
	private String defaultPage;
	private File root;
	private ServerSocket server;
	private ExecutorService threadsPool;

	public WebServer(File root, String defaultPage, int port, int maxThreads) throws IOException {
		
		this.port = port;
		this.maxThreads = maxThreads;
		this.root = root;
		this.defaultPage = defaultPage;
		threadsPool = Executors.newFixedThreadPool(this.maxThreads);
		server = new ServerSocket(port);
		System.out.println("Listening port: " + this.port);
	}


	public void run() {
		while(true) {
			try {
				Socket connectiont = server.accept();
				HTTPConnection HttpConnection = new HTTPConnection(connectiont, root, defaultPage);
				threadsPool.execute(HttpConnection);
				
			} catch (IOException e) {
				System.out.println("WARN: Failed to create the new connection");
			}				
		}
	}
	

}
