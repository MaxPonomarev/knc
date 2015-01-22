import java.awt.event.*;
import java.io.*;
import java.security.KeyStore;
import java.util.concurrent.*;
import java.util.zip.*;
import javax.net.ssl.*;

class At_net implements Runnable {
  private static final ExecutorService pool = Executors.newCachedThreadPool();
  private static volatile SSLSocketFactory factory = null;
  private final String command;
  private final Object obj;
  private final ActionListener listener;
  private volatile Object result = null;
  private volatile String error;
  private volatile boolean sentData = false;
  private volatile int timeout = 20000;

  public At_net(String command, Object obj, ActionListener listener) {
    this.command = command;
    this.obj = obj;
    this.listener = listener;
  }

  public At_net(String command, Object obj, ActionListener listener, int timeout) {
    this(command, obj, listener);
    this.timeout = timeout;
  }

  public synchronized void start() {
    pool.execute(this);
  }

  public void run() {
    try {
      if (factory == null) {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(new File("cert")), null);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, tmf.getTrustManagers(), null);
        factory = sslContext.getSocketFactory();
      }
      try (SSLSocket socket = (SSLSocket) factory.createSocket(KNC_Terminal.serverIP, KNC_Terminal.serverPort)) {
        socket.setSoTimeout(timeout);
        socket.startHandshake();
        listener.actionPerformed(new ActionEvent(this, 1, "ret_" + command));
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        output.writeBytes(command + "\r\n");
        output.flush();
        listener.actionPerformed(new ActionEvent(this, 2, "ret_" + command));
        GZIPOutputStream zipout = new GZIPOutputStream(socket.getOutputStream());
        ObjectOutputStream objout = new ObjectOutputStream(new BufferedOutputStream(zipout));
        objout.flush();
        objout.writeObject(obj);
        objout.flush();
        zipout.finish();
        zipout.flush();
        sentData = true;
        listener.actionPerformed(new ActionEvent(this, 3, "ret_" + command));
        if (!command.equals("USER_EXIT")) {
          ObjectInputStream in;
          if (command.equals("GET_UPDATE")) in = readInputStreamByteByByte(new BufferedInputStream(new GZIPInputStream(socket.getInputStream())));
          else in = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(socket.getInputStream())));
          result = in.readObject();
          in.close();
        }
        objout.close();
        output.close();
      }
      listener.actionPerformed(new ActionEvent(this, 4, "ret_" + command));
      listener.actionPerformed(new ActionEvent(this, 5, "finished_" + command));
    } catch (Exception ex) {
      ex.printStackTrace();
      result = null;
      error = "fail_" + command + ": " + ex.toString();
      listener.actionPerformed(new ActionEvent(this, 5, "fail_" + command));
    }
  }

  public synchronized Object getResult() {
    return result;
  }

  public synchronized String getError() {
    return error;
  }

  public synchronized Object getObject() {
    return obj;
  }

  public synchronized boolean getStatus() {
    return sentData;
  }

  private ObjectInputStream readInputStreamByteByByte(InputStream in) throws IOException {
    ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
    int c;
    byte[] buf = new byte[4096];
    while ((c = in.read(buf)) != -1) {
      tempOut.write(buf, 0, c);
      listener.actionPerformed(new ActionEvent(this, tempOut.size(), "ret_" + command));
    }
    ByteArrayInputStream tempIn = new ByteArrayInputStream(tempOut.toByteArray());
    return new ObjectInputStream(tempIn);
  }
}
