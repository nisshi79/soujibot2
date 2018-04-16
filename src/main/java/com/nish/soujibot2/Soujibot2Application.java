package com.nish.soujibot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
@SpringBootApplication
@EnableScheduling
public class Soujibot2Application {

    public static void main(String[] args) {
        SpringApplication.run(Soujibot2Application.class, args);
    }

    final static int TYOUSEI = -2;




    @Scheduled(cron = "0 10 12 * * *", zone = "Asia/Tokyo")
    public void executeAlarm() {
        //プッシュする処理を呼び出す
        pushAlert();

    }

    @EventMapping
    public void pushAlert() {
        final LineMessagingClient client = LineMessagingClient
                .builder("2+/yuT8ezRCZwr0JDyl3ounEW6xBb5ih1ZiTp1pg04Ey0bPqTTV3cGcnYcuWswzKYMZSkpqiWglXMc0ETbpYPBy5THgL/b09/pRqgn2K3nFLqNTiDbyoHOMNwH65yjWzwiuMWuFzpB2DH0ZuA2t/kQdB04t89/1O/w1cDnyilFU=")
                .build();
        StringBuilder sb = new StringBuilder();

        String souji = "";
        for (int cId = 1; cId <= 8; cId++) {
            sb.append(Draw(cId));
        }

        String str = new String(sb);
        String aaa = "chec";
        final TextMessage textMessage = new TextMessage(str);
        final PushMessage pushMessage = new PushMessage(
                "Caaeed7cc0b737e9453fc5c3087450337"/*"C6803ac7017f9c247098d440b27131a38"*/,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = client.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(botApiResponse);

    }

    private static int getJD() {

        //フォーマットを設定して出力
        int D = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//日
        int M = Calendar.getInstance().get(Calendar.MONTH) + 1;//月
        int Y = Calendar.getInstance().get(Calendar.YEAR);//年

        if (M == 1 || M == 2) {
            Y = Y - 1;
            M = M + 12;
        }
        int A = (Y / 100);
        int B = 2 - A + (A / 4);
        double JD = (365.25 * (Y + 4716)) + (30.6001 * (M + 1)) + D + B - 1524.5;
        int jD = (int) JD;
        return jD;
    }

    private static int getMJD(){
        int D = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//日
        int M = Calendar.getInstance().get(Calendar.MONTH)+1;//月
        int Y = Calendar.getInstance().get(Calendar.YEAR);//年

        if(M==1||M==2){
            Y=Y-1;
            M=M+12;
        }
        int A=(Y/100);
        int B=2 - A + (A / 4) ;
        double JD = (365.25*(Y + 4716)) + (30.6001*(M + 1)) + D + B - 1524.5;
        return (int)(JD - 2400000.5);
    }

    private static int getGId(int cId) {
        int cW = (((getMJD()+3) / 7 )+TYOUSEI) % 4;
        int gId = cId - (cW * 2) % 8;
        if (gId <= 0) gId += 8;
        return gId;
    }

    @EventMapping
    String Draw(int cId) {

        //日:1, 月:2, 火:3, 水:4, 木:5, 金:6, 土:7


        int doW = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (cId) {
            case 1:
                if (doW == 6) {
                    return "教室:" + getGName(getGId(cId)) + "\r\n";

                }
                return "";

            case 4:
                if (doW == 2 || doW == 6) {
                    return "女子トイレ:" + getGName(getGId(cId)) + "\r\n";
                }
                return "";

            case 5:
                if (doW == 2 || doW == 6) {
                    return "渡り廊下:" + getGName(getGId(cId)) + "\r\n";
                }
                return "";

            case 6:
                if (doW == 2) {
                    return "教室:" + getGName(getGId(cId)) + "\r\n";
                }
                return "";

            case 7:
                if (doW == 3) {
                    return "教室:" + getGName(getGId(cId)) + "\r\n";
                }
                return "";

            case 8:
                if (doW == 5) {
                    return "教室:" + getGName(getGId(cId)) + "\r\n";
                }
                if (doW == 4) return "今日の掃除はありません";
                return "";

            default:
                return "";
        }
    }

    private String getGName(int gId) {
        switch (gId) {
            case 1:
                return "A";

            case 2:
                return "B";

            case 3:
                return "C";

            case 4:
                return "D";

            case 5:
                return "E";

            case 6:
                return "F";

            case 7:
                return "G";

            case 8:
                return "H";

            default:
                return "";
        }
    }
}