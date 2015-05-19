package q6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class CounterSynchronizationReceiveTask implements Runnable {
	private final MulticastSocket socket;
	private final int serverId;
	
	public CounterSynchronizationReceiveTask(MulticastSocket socket, int serverId) {
		this.socket = socket;
		this.serverId = serverId;
	}
	
	@Override
	public void run() {
		for(;;) {
			byte[] intBuffer = new byte[8];
			DatagramPacket receivedPacket = new DatagramPacket(intBuffer, 8);
			
			try {
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
