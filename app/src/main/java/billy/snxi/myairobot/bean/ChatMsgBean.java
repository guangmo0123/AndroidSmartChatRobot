package billy.snxi.myairobot.bean;

/**
 * Created by Administrator on 2018-05-21.
 */
public class ChatMsgBean {

    private String date;
    private String msg;
    private MsgType msgType;

    public ChatMsgBean() {
    }

    public ChatMsgBean(String date, String msg, MsgType msgType) {
        this.date = date;
        this.msg = msg;
        this.msgType = msgType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public enum MsgType {
        MSG_FROM,
        MSG_TO;
    }

}
