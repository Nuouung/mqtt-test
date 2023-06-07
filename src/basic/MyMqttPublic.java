package basic;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttPublic {

    private MqttClient client;

    public MyMqttPublic() {
        // MqttClient는 publisher도 될 수 있고 client가 될 수도 있다.
        try {
            // 브로커 서버
            client = new MqttClient("tcp://localhost:1883", "myId");

            // 브로커 접속
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean send(String topic, String message) {
        try {
            // 브로커로 전송할 메세지 생성
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());

            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void close() {
        if (client != null) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final MyMqttPublic myMqttPublic = new MyMqttPublic();

        new Thread(new Runnable() {
            public void run() {
                int i = 1;
                String message = "";
                while (true) {
                    if (i == 5) break;

                    if (i % 2 == 1) message = "led_on";
                    else message = "led_off";

                    myMqttPublic.send("iot", message);

                    i++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                myMqttPublic.close(); // 작업 완료되면 종료
            }
        }).start();
    }
}
