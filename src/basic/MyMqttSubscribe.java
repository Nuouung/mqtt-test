package basic;

import org.eclipse.paho.client.mqttv3.*;

// 브로커로부터 메세지를 전달받기 위해 구독신청을 하고 대기하는 객체
public class MyMqttSubscribe implements MqttCallback {

    private MqttClient mqttClient;

    // MQTT 프로토콜을 이용해 브로커에 연결하면서 연결정보를 설정할 수 있는 객체
    private MqttConnectOptions connectOptions;

    public MyMqttSubscribe init(String server, String clientId) {
        try {
            connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            connectOptions.setKeepAliveInterval(30);

            mqttClient = new MqttClient(server, clientId);

            // 클라이언트 객체에 MqttCallback을 등록
            mqttClient.setCallback(this);
            mqttClient.connect(connectOptions);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("===============메세지 도착===================");
        System.out.println(mqttMessage);
        System.out.println("topic: " + topic + " | id: " + mqttMessage.getId() + " | payload: " + mqttMessage.getPayload().toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public boolean subscribe(String topic) {
        boolean result = true;

        try {
            if (topic != null) {
                // 두번째 인자는 qos로 메세지가 도착하기 위한 품질에 값을 설정 - 서비스 품질
                // 0, 1, 2 설정 가능
                mqttClient.subscribe(topic, 0);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    public static void main(String[] args) {
        MyMqttSubscribe myMqttSubscribe = new MyMqttSubscribe();
        myMqttSubscribe.init("tcp://localhost:1883", "myId2").subscribe("iot");
    }
}
