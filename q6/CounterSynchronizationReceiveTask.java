package q6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class CounterSynchronizationReceiveTask implements Runnable {
	private final MulticastSocket socket;
	private final int serverId;
	private final InetAddress multicastAddress;
	private final int multicastPort;
	
	public CounterSynchronizationReceiveTask(MulticastSocket socket, int serverId, InetAddress multicastAddress, int multicastPort) {
		this.socket = socket;
		this.serverId = serverId;
		this.multicastAddress = multicastAddress;
		this.multicastPort = multicastPort;
	}
	
	@Override
	public void run() {
		for(;;) {
			byte[] intBuffer = new byte[8];
			DatagramPacket receivedPacket;
			
			try {
				receivedPacket = new DatagramPacket(intBuffer, 8, multicastAddress, multicastPort);
				socket.receive(receivedPacket);
			} catch (IOException e) {
			}
			
			ByteBuffer messageBuffer = ByteBuffer.wrap(intBuffer);
			int receivedFromServerId = messageBuffer.getInt();
			int counterValue = messageBuffer.getInt();
			
			if (this.serverId != receivedFromServerId) {
				Server.SetQueryCounter(counterValue);
				System.out.printf("Received sync message with value %d.\n", counterValue);
			}
		}
	}

}
