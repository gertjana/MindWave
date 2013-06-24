import com.typesafe.scalalogging.slf4j.Logging
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import java.nio.CharBuffer
import java.nio.charset.{Charset, CharsetEncoder}
import java.util.Scanner
import scala.swing.event.ButtonClicked
import swing._

object MindWave extends SimpleSwingApplication {

	var client = new ThinkGearSocketClient("localhost", 13854)

  val logArea = new TextArea() {
    rows = 8
    columns = 8
  }

  def top = new MainFrame {
    title = "MindWave"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new FlowPanel {
        contents += new Button("Connect") {
          reactions += {
	            case ButtonClicked(_) => {
              logArea.append("Connecting")
              client.connect()
            }
          }
        }
        contents += new Button("Exit") {
          reactions += {
            case ButtonClicked(_) => {
              client.close
              System.exit(0)
            }
          }
        }
        contents += new Button("Data") {
          reactions += {
            case ButtonClicked(_) => {
              while(client.isDataAvailable()) {
                val data = client.getData
                logArea.append(data)
                Thread.sleep(100)
              }
            }
          }
        }
      }
      contents += logArea
    }
  }
}

class ThinkGearSocketClient(host:String, port:Int) extends Logging {
	var channel:SocketChannel = null
  var connected = false
  var in:Scanner = null




	def connect() = {

     if (!connected) {
       logger.debug("starting connection...")

       channel = SocketChannel.open(new InetSocketAddress(host, port))
       val encoder = Charset.forName("US-ASCII").newEncoder()
       val jsonCommand = "{\"enableRawOutput\": false, \"format\":\"Json\"}\n"
       channel.write(encoder.encode(CharBuffer.wrap(jsonCommand)))

       in = new Scanner(channel)
       connected = true
     } else {
       logger.debug("already connected")
     }
	}

  def isDataAvailable():Boolean = {
    if (connected) {
      in.hasNextLine
    } else {
      false
    }
  }

  def getData:String = in.nextLine()

  def close = {
    if (connected) {
      logger.debug("closing connection")
      in.close
      channel.close
      connected = false

    }
  }
}
