package q6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class CounterSynchronizationSendTask implements Runnable {
	private final MulticastSocket socket;
	private final int valueToSend;
	private final int serverId;
	private final InetAddress multicastAddress;
	private final int multicastPort;
	
	public CounterSynchronizationSendTask(MulticastSocket socket, int valueToSend, int serverId, InetAddress multicastAddress, int multicastPort) {
		this.socket = socket;
		this.valueToSend = valueToSend;
		this.multicastAddress = multicastAddress;
		this.multicastPort = multicastPort;
		this.serverId = serverId;
	}

	@Override
	public void run() {
		byte[] intBuffer = ByteBuffer.allocate(8).putInt(serverId).putInt(this.valueToSend).array();
		
		try {
			socket.send(new DatagramPacket(intBuffer, 8, this.multicastAddress, this.multicastPort));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
