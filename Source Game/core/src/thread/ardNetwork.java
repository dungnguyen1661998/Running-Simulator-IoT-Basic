package thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.mygdx.game.GameScreen;

import actors.Runner;

public class ardNetwork extends Thread{
	public BufferedReader reader;
	public  int dataAction;
	public int data = 0;
	public Runner runner;
	public ardNetwork() {
		int port = 50050;
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is listening on port " + port);
			Socket socket = serverSocket.accept();
			System.out.println("New client connected");

			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int getData() {
		return data;
	}
	public ardNetwork(Runner runner) {
		this();
		this.runner = runner;
	}
	public void run() {
		String data = null;
		while(true) {
			try {
				data = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataAction = Integer.parseInt(data);
			System.out.println(data);
			//performevent(Integer.parseInt(data));
		}
	}
	public int getDataAction() {
		return dataAction;
	}
	private void performevent(int data) {
		// TODO Auto-generated method stub
		switch(data) {
		case 0:
			runner.dodge();
			break;
		case 1:
			if (runner.isDodging()) {
				runner.stopDodge();
			}
			break;
		case 2:
			if (runner.isDodging()) {
				runner.stopDodge();
			}
			runner.jump();
			break;
		default:
		}
	}
}