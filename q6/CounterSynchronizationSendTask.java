package q6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class CounterSynchronizationSendTask implements Runnable {
	private final DatagramSocket socket;
	private final int valueToSend;
	
	public CounterSynchronizationSendTask(DatagramSocket socket, int valueToSend) {
		this.socket = socket;
		this.valueToSend = valueToSend;
	}

	@Override
	public void run() {
		byte[] intBuffer = ByteBuffer.allocate(4).putInt(this.valueToSend).array();
		
		try {
			socket.send(new DatagramPacket(intBuffer, 4));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
