package q6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class CounterSynchronizationReceiveTask implements Runnable {
	private final DatagramSocket socket;
	
	public CounterSynchronizationReceiveTask(DatagramSocket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		for(;;) {
			byte[] intBuffer = new byte[4];
			DatagramPacket receivedPacket = new DatagramPacket(intBuffer, 4);
			
			try {
				socket.receive(receivedPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Server.SetQueryCounter(ByteBuffer.wrap(intBuffer).getInt());
		}
	}

}
