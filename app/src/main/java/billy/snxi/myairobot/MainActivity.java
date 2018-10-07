package billy.snxi.myairobot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import billy.snxi.myairobot.adapter.ChatMsgAdapter;
import billy.snxi.myairobot.bean.ChatMsgBean;
import billy.snxi.myairobot.utils.DateUtils;
import billy.snxi.myairobot.utils.HttpUtils;
import billy.snxi.myairobot.utils.RobotReplyUtils;

public class MainActivity extends Activity {
    private Context context;
    private TextView tv_thinking;
    private ListView lv_msg;
    private EditText et_input_msg;
    private List<ChatMsgBean> mList;
    private ChatMsgAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
        initList();
    }

    private void initView() {
        tv_thinking = (TextView) findViewById(R.id.tv_thinking);
        lv_msg = (ListView) findViewById(R.id.lv_msg);
        et_input_msg = (EditText) findViewById(R.id.et_input_msg);
        lv_msg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = mList.get(position).getMsg();
                String urlKey = "http://";
                int urlIndex = msg.indexOf(urlKey);
                if (urlIndex == -1) {
                    urlKey = "https://";
                    urlIndex = msg.indexOf(urlKey);
                    if (urlIndex == -1) {
                        return false;
                    }
                }
                String url = msg.substring(urlIndex);
                Log.d("billy", url);
                openAppDefaultBrowser(url);
                return false;
            }
        });
    }

    private void initList() {
        mList = new ArrayList<>();
        mList.add(new ChatMsgBean(DateUtils.fromat(new Date()), "Hello，我是智能机器人，小林！", ChatMsgBean.MsgType.MSG_FROM));
        mAdapter = new ChatMsgAdapter(context, mList);
        lv_msg.setAdapter(mAdapter);
    }

    /**
     * 发送消息
     *
     * @param view
     */
    public void onSendMsg(View view) {
        String msg = et_input_msg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        //向listView添加一个发送消息的记录
        mList.add(new ChatMsgBean(DateUtils.fromat(new Date()), msg, ChatMsgBean.MsgType.MSG_TO));
        mAdapter.notifyDataSetChanged();
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                tv_thinking.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                return HttpUtils.getReplyMsgByServer(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                if (TextUtils.isEmpty(s)) {
                    mList.add(new ChatMsgBean(DateUtils.fromat(new Date()), "AI机器人回复信息失败！", ChatMsgBean.MsgType.MSG_FROM));
                } else {
                    Log.d("billy", s);
                    try {
                        JSONObject jsonData = new JSONObject(s);
                        RobotReplyUtils.addRealResultToList(mList, jsonData);
                        et_input_msg.setText(null);
                    } catch (JSONException e) {
                    }
                }
                mAdapter.notifyDataSetChanged();
                tv_thinking.setVisibility(View.GONE);
            }
        };
        task.execute(msg);

    }

    /**
     * 打开系统默认的浏览器
     */
    public void openAppDefaultBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

}
