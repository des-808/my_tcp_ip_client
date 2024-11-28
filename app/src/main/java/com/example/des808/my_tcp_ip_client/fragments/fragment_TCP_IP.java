package com.example.des808.my_tcp_ip_client.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.adapter.ChatMessageAdapter;
import com.example.des808.my_tcp_ip_client.classs.Chat;
import com.example.des808.my_tcp_ip_client.db.DBChatAdapter;
import com.example.des808.my_tcp_ip_client.interfaces.onFragment_TCP_IP_Init;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class fragment_TCP_IP extends Fragment {
    private OnFragmentInteractionListener mListener;
    private onFragment_TCP_IP_Init mFragmentTCPIPInit;
    View view;
    private static final String PREFS_FILE = "com.example.des808.my_tcp_ip_client_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;
    private static final String FRAGMENT_NAME = "fragment_TCP_IP ";
    SharedPreferences sharedPreferencesMain;
    //private Button button;

    private static final String LOG_TAG = "LOG_TAG" ;

    private DBChatAdapter db_chat_Adapter;
    private ChatMessageAdapter chatMessageAdapter;


    private List<Chat> chatsList = new ArrayList<>();
    private RecyclerView recyclerView;//создаем переменную для отображения сообщений
    private Vibrator vibrator;
    public static ProgressDialog dialog;
    private static boolean connectToServer;
   /* public MenuItem menu_switch_btn;
    public MenuItem menu_clearChat;*/
    public EditText object,clas,razd,schs;
    public ImageButton btnSend_tx;
    private boolean is_fragment_TcpIP;
    private Bundle savedInstanceState;


    public fragment_TCP_IP() {
        // Required empty public constructor
    }

    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    // TODO: Rename and change types and number of parameters
    //public static fragment_TCP_IP newInstance(String param1, String param2, String param3) {
    public static fragment_TCP_IP newInstance(int number) {
        Bundle args = new Bundle();
        fragment_TCP_IP fragment = new fragment_TCP_IP();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragment_TCP_IP_Init){
            mFragmentTCPIPInit = (onFragment_TCP_IP_Init) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context + " must implement OnFragmentInteractionListener" );
        }
        //Log.d(LOG_TAG, "onAttach FragmentTCP_IP");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate( savedInstanceState );
        //if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_tcp_ip, container, false );
        sharedPreferencesMain = getActivity() != null ? getActivity().getSharedPreferences(PREFS_FILE, PREFS_MODE) : null;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentTCPIPInit.on_FragmentTCP_IP_Init();
        //on_FragmentTCP_IP_Init();
        chatMessageOrChatAndromedaLayout();
        //Log.d(LOG_TAG, "onStart FragmentTCP_IP");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Log.d(LOG_TAG, "onDeath FragmentTCP_IP");
        mFragmentTCPIPInit.on_FragmentTCP_IP_Disconnect();
        //DisconnectToServer();//Отключаемся от сервера
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void chatMessageOrChatAndromedaLayout(){
        View newView;
        ViewGroup fragmentLayout = (ViewGroup) getView().findViewById(R.id.fragmentLayout);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        fragmentLayout.removeAllViews();
        // Проверяем, существует ли ключ KEY_ANDROMEDA в файле PREFS_FILE
        if (sharedPreferencesMain.contains(KEY_ANDROMEDA)) {
            // Если ключ существует
            Log.d(LOG_TAG, MessageFormat.format("{0}.{1} -> {2}", FRAGMENT_NAME, KEY_ANDROMEDA, sharedPreferencesMain.getBoolean(KEY_ANDROMEDA, false)));
            if(sharedPreferencesMain.getBoolean(KEY_ANDROMEDA,false) ){
                newView = getLayoutInflater().inflate(R.layout.row_andromeda_send, null);
            }else {//false режим текста
                newView = getLayoutInflater().inflate(R.layout.row_chat_send, null);
            }
        } else {
            // Если ключ не существует, выводим сообщение об ошибке
            Log.e(LOG_TAG, "Ключ " + KEY_ANDROMEDA + " не найден в файле " + PREFS_FILE);
            return;
        }
        newView.setLayoutParams(params);
        fragmentLayout.addView(newView);
    }

    /*private  void initRecyclerView(){
        recyclerView = getActivity().findViewById(R.id.list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chatMessageAdapter);// устанавливаем для списка адаптер
    }*/
    /*TODO
     *  String i - текст сообщения
     *  boolean in_out - true - исходящие сообщения output = true
     *  boolean in_out - false - входящие сообщения input = false
     * */
    /*public void addChatMessage(String i ,boolean in_out){
        Chat chat = new Chat(in_out);//создаем переменную для отображения сообщений в бд
        chat.setMessage_time(MessageTime.getTime());
        if (!in_out){//true - входящие сообщения
            chat.setMessage_in(i);
        }else{//false - исходящие сообщения
            chat.setMessage_out(i);
        }
        long insert = db_chat_Adapter.addDBMessage(chat);//сохраняем сообщение в БД
        chat.setId((int) insert);
        //Log.d(LOG_TAG, "AddMessage id = " + insert);
        chatsList.add(chat);//выводим сообщение в список
        refreshChatListView();
    }*/
    @SuppressLint("NotifyDataSetChanged")
    private void refreshChatListView(){
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearRecyclerView() {
        chatsList.clear();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }



















}
