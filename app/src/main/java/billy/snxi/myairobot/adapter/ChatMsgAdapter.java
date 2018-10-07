package billy.snxi.myairobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import billy.snxi.myairobot.R;
import billy.snxi.myairobot.bean.ChatMsgBean;

public class ChatMsgAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_FROM = 0;
    private static final int ITEM_VIEW_TYPE_TO = 1;
    private LayoutInflater mInflater;
    private List<ChatMsgBean> mList;

    public ChatMsgAdapter(Context context, List<ChatMsgBean> list) {
        mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsgBean chatMsgBean = mList.get(position);
        if (chatMsgBean.getMsgType() == ChatMsgBean.MsgType.MSG_FROM) {
            return ITEM_VIEW_TYPE_FROM;
        } else {
            return ITEM_VIEW_TYPE_TO;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ChatMsgBean chatMsgBean = mList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //当消息为来自对方时的item
            if (getItemViewType(position) == ITEM_VIEW_TYPE_FROM) {
                convertView = mInflater.inflate(R.layout.item_chat_msg_from, null);
            } else {
                convertView = mInflater.inflate(R.layout.item_chat_msg_to, null);
            }
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_date.setText(chatMsgBean.getDate());
        viewHolder.tv_msg.setText(chatMsgBean.getMsg());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_msg;
        TextView tv_date;
    }
}
