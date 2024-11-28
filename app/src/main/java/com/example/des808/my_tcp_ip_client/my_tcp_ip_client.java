package com.example.des808.my_tcp_ip_client;


import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.adapter.ChatMessageAdapter;
import com.example.des808.my_tcp_ip_client.classs.Adress;
import com.example.des808.my_tcp_ip_client.classs.Chat;
import com.example.des808.my_tcp_ip_client.classs.MessageTime;
import com.example.des808.my_tcp_ip_client.db.DBChatAdapter;
import com.example.des808.my_tcp_ip_client.fragments.SharedPreferenceFragment;
import com.example.des808.my_tcp_ip_client.fragments.fragment_TCP_IP;
import com.example.des808.my_tcp_ip_client.fragments.fragment_exit;
import com.example.des808.my_tcp_ip_client.fragments.fragment_titles;
import com.example.des808.my_tcp_ip_client.interfaces.EnumsAndStatics;
import com.example.des808.my_tcp_ip_client.interfaces.TCPListener;
import com.example.des808.my_tcp_ip_client.interfaces.onFragment_TCP_IP_Init;
import com.example.des808.my_tcp_ip_client.interfaces.onStartFragmentTcpIp;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import static com.example.des808.my_tcp_ip_client.TCPCommunicator.removeAllListeners;
/* // greenrobot eventbus урок на ютубе
    //https://www.youtube.com/watch?v=WnzSkRinnuc*/

public class my_tcp_ip_client extends AppCompatActivity
    implements  //fragment_titles.OnFragmentInteractionListener,
                //fragment_titles.OnFragmentItemClickListener,
        onStartFragmentTcpIp,
        onFragment_TCP_IP_Init,
        TCPListener,
        //OnSettingsFragment,
                //fragment_TCP_IP.OnFragmentItemClickListenerSwitch,
                fragment_TCP_IP.OnFragmentInteractionListener
{

    private static final String LOG_TAG = "LOG_TAG";
    //private ArrayList<TitleChatsItems> list;
    private final boolean IN_MSG = false;
    private final boolean OUT_MSG = true;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean connectToServer = false;
    public String mMessage;
    public Toast toast;
    public String table_name, ipAdr, port;

    private final Handler UIHandler = new Handler();
    public MenuItem menu_switch_btn;
    public MenuItem menu_clearChat;

    public EditText object,clas,razd,schs;
    public ImageButton btnSend_tx;

    public boolean is_fragment_TcpIP = false;
    fragment_exit dialogFragment;
    fragment_titles fragTitles;
    fragment_TCP_IP fragTCP_IP;
    FragmentManager fManager;
    View sv;

    //private static final String PREFS_FILE = "com.example.des808.my_tcp_ip_client_preferences";
    //private static final String KEY_ANDROMEDA = "switch_andromeda";
    //private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;
    //SharedPreferences sharedPreferencesMain;
    //SharedPreferences.Editor prefEditor;

    List<Chat> chatsList = new ArrayList<>();
    RecyclerView recyclerView;//создаем переменную для отображения сообщений
    Vibrator vibrator;

    DBChatAdapter db_chat_Adapter; //создаем переменную для работы с базой данных
    ChatMessageAdapter chatMessageAdapter;//создаем переменную для работы с clickable
    private AlertDialog dialog;

    public  void startVibrate(int millisecondsVibrate){
        if(vibrator.hasVibrator()){//если нет вибрации то возвращаем false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect effect = VibrationEffect.createOneShot(millisecondsVibrate, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(effect);
            } else {
                vibrator.vibrate(millisecondsVibrate);
            }
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    public void on_FragmentTCP_IP_Init() {
        //сработает когда запустится фрагмент fragment_tcp_ip
        is_fragment_TcpIP = true; //флаг наличия запущенного фрагмента
        String xparam = table_name +"  |  "+ ipAdr + ":" + port;
        TextView x = findViewById( R.id.connect_text );
        x.setText( xparam );
            object = findViewById(R.id.editObjekt);
            clas = findViewById(R.id.editClass);
            razd = findViewById(R.id.editRazd);
            schs = findViewById(R.id.editSchs);
            btnSend_tx = findViewById(R.id.buttonSend_tx);
        // начальная инициализация списка
        initRecyclerView();
        clearRecyclerView();
        if(menu_clearChat!=null)menu_clearChat.setVisible(true);
        ConnectToServer();
        chatsList.addAll(db_chat_Adapter.getMessages());//добавляем сообщения из БД в список
//package:mine
    }
    private  void initRecyclerView(){
        recyclerView = findViewById(R.id.list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatMessageAdapter);// устанавливаем для списка адаптер
    }

/*TODO
    *  String i - текст сообщения
    *  boolean in_out - true - исходящие сообщения output = true
    *  boolean in_out - false - входящие сообщения input = false
    * */
    public void addChatMessage(String i ,boolean in_out){
        Chat chat = new Chat(in_out);//создаем переменную для отображения сообщений в бд
        chat.setMessage_time(MessageTime.getTime());
        if (!in_out){//true - входящие сообщения
            chat.setMessage_in(i);
        }else{//false - исходящие сообщения
            chat.setMessage_out(i);
        }
        long insert = db_chat_Adapter.addDBMessage(chat);//сохраняем сообщение в БД
        chat.setId( insert);
        //Log.d(LOG_TAG, "AddMessage id = " + insert);
        chatsList.add(chat);//выводим сообщение в список
        refreshChatListView();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void refreshChatListView(){
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        int position = recyclerView.getAdapter().getItemCount() - 1;
        if (position >= 0) {
            recyclerView.smoothScrollToPosition(position);
        }
        //recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void clearRecyclerView() {
        chatsList.clear();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

   @Override
    public void on_FragmentTCP_IP_Disconnect(){
        is_fragment_TcpIP = false;//флаг отсутствия запущенного фрагмента
        menu_clearChat.setVisible(false);//скрываем кнопку удаления чата
        DisconnectToServer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_tcp_ip_client );
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        fragTitles = new fragment_titles();
        fragTCP_IP = new fragment_TCP_IP();
        sv = findViewById(R.id.FrLay);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //sharedPreferencesMain = getSharedPreferences(PREFS_FILE, PREFS_MODE);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //создаем переменную для работы с базой данных
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        db_chat_Adapter = new DBChatAdapter(this);
        chatMessageAdapter = new ChatMessageAdapter( chatsList);
        chatMessageAdapter.setOnItemClickListener((position, view, chat) -> {
            long pos = chat.getId();
           Log.d(LOG_TAG, "onItemClick " + position + " onItemClick id=" + pos);
        });

        chatMessageAdapter.setOnItemLongClickListener((position, view, chat) -> {
            long pos = chat.getId();
            long remove =  db_chat_Adapter.deleteMessage(pos);
            chatMessageAdapter.deleteItemByPosition(position);
            //Log.d(LOG_TAG, "RemoveMessage id = " + pos);
           // Log.d(LOG_TAG, "onItemLongClick position=" + position+" onItemLongClick id="+ pos + " remove="+remove);
            return true;
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        fManager = getSupportFragmentManager();
        if(null == savedInstanceState){
            fManager.beginTransaction()
                    .addToBackStack("fragment_titles")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.FrLay,fragTitles,"fragment_titles")
                    .commit();
        }
    }

    @Override
    protected void onResume() {super.onResume();}
    @Override
    protected void onStart() {super.onStart();}

    @Override
    public void onFragmentInteraction(Uri uri) {
        tost("onFragmentInteraction");
    }

    public void replaceFragment(Fragment fragment, String tag){
        Fragment currentFragment = fManager.findFragmentById(R.id.FrLay);
        if((currentFragment != null ? currentFragment.getClass() : null) == fragment.getClass()){return;}
        if(fManager.findFragmentByTag(tag)!= null){
            fManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fManager.beginTransaction().addToBackStack(tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.FrLay,fragment,tag).commit();
                /*.remove(currentFragment)
                .add(R.id.FrLay,fragment,tag).commit();*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        int fragmentsInStack = fManager.getBackStackEntryCount();
       // tost(String.valueOf(fragmentsInStack));
        if (fragmentsInStack > 1) {
            //fManager.popBackStack("fragment_titles",0);
            //fManager.popBackStack();
            fManager.popBackStackImmediate();
        }else if(fragmentsInStack == 1){
            dialogFragment = new fragment_exit( "Так ты точно хочешь выйти???" );
            dialogFragment.show( fManager, "dialog" );
            //или
            //alertDialog("Так ты точно хочешь выйти???");
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void doPositiveClick() {this.finish();}//alertDialog();
    public void doNegativeClick() {}

    @Override
    protected void onPause() {super.onPause();}

    @Override
    protected void onDestroy() {
        super.onDestroy();//EventBus.getDefault().unregister( this );
        try {
            DisconnectToServer();
        }catch (Exception e) {
            Log.d(LOG_TAG, String.valueOf(e));
        }
    }

    //###################################################################################################*/
    public void tost(String msg) {
        toast = Toast.makeText( this, msg, Toast.LENGTH_SHORT );
        toast.show();
    }
    public void tostShort(String msg) {
        toast = Toast.makeText( this, msg, Toast.LENGTH_SHORT );
        toast.show();
    }
    //###################################################################################################

    public void on_startFragmentTcpIp(Adress adr){
        table_name = adr.getName();
        ipAdr = adr.getIp();
        port = adr.getPort();
        replaceFragment(new fragment_TCP_IP(), "fragment_TCP_IP");
    }
    public void onStartFragmentTCP_IP() {
        replaceFragment(new fragment_TCP_IP(), "fragment_TCP_IP");
    }

    public void sendTx(View v)  {
        startVibrate(100);
            object = findViewById(R.id.editObjekt);
            clas = findViewById(R.id.editClass);
            razd = findViewById(R.id.editRazd);
            schs = findViewById(R.id.editSchs);
            String e_object = object.getText().toString();
            String e_clas = clas.getText().toString();
            String e_razd = razd.getText().toString();
            String e_schs = schs.getText().toString();
            String E_text = e_object + e_clas + e_razd + e_schs;

            String x = "5032 18";
            char i = (char) 20;
            E_text = x + E_text + i;//
            if (TCPCommunicator.writeToSocket(E_text, UIHandler, this) == TCPCommunicator.TCPWriterErrors.OK) {
                addChatMessage(E_text,OUT_MSG);
            } else {
                tost("ошибка передачи сообщения");
            }
    }

    public void sendChatTx(View v)  {
        startVibrate(100);
            EditText  E_Send = findViewById( R.id.EChat_Send );
            String E_text = E_Send.getText().toString();
            if(E_text.isEmpty()) {// если поле пустое то вызываем Toast с сообщением что нужно ввести текст
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            E_Send.setText( "" );//сброс текста в поле отправляемых сообщений
            if (TCPCommunicator.writeToSocket(E_text, UIHandler, this) == TCPCommunicator.TCPWriterErrors.OK) {
                addChatMessage(E_text,OUT_MSG);
            } else {
                tost("ошибка передачи сообщения");
            }
    }



    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        Log.d(LOG_TAG, "onSaveInstanceState");
    }



    private void alertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(my_tcp_ip_client.this);
        //alertDialogBuilder.setTitle("Так ты точно хочешь выйти??");
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder
        .setCancelable(true)
        .setPositiveButton("Да", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                //tostShort("Правильный выбор!!");
                my_tcp_ip_client.this.finish();
        }
        })
        .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void ConnectToServer() {
            if(!connectToServer) {
                setupDialog();
                TCPCommunicator tcpClient = TCPCommunicator.getInstance();
                TCPCommunicator.addListener( this );
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
                //DataStore settings = PreferenceManager.getDefaultSharedPreferences( this );
                    tcpClient.init( settings.getString( EnumsAndStatics.SERVER_IP_PREF, ipAdr),
                            parseInt( settings.getString( EnumsAndStatics.SERVER_PORT_PREF, port) ) );
            }
    }

    private void DisconnectToServer(){
        if(TCPCommunicator.getInstance()!= null){
            TCPCommunicator.closeStreams();
            TCPCommunicator.removeAllListeners();
            connectToServer = false;
            menu_switch_btn.setIcon(R.drawable.ic_otkl);
        }
    }

    @Override
    public void onTCPMessageRecieved(String message) {
        // TODO Auto-generated method stub
        final String theMessage = message;
        mMessage = message;
        runOnUiThread(() -> {
            //tost("onTCPMessageRecieved: "+theMessage);
            addChatMessage(theMessage,IN_MSG);
        });
    }


    public void onTCPMessageRecievedByteBuffer(byte[] inMsgByteBuffer,int count){
       String hex = byteBufferToHex(inMsgByteBuffer, count);
        //String message1 = new String(inMsgByteBuffer, 0, count, StandardCharsets.UTF_8);
        //String formattedMessage = String.format("%n%s%n", hex);
        //Snackbar.make(sv,message1+"   "+hex+"\n",Snackbar.LENGTH_SHORT).show();
        //String message2 = new String(inMsgByteBuffer, 0, count, StandardCharsets.US_ASCII);
        String message = new String(inMsgByteBuffer, 0, count, Charset.forName("CP1251"));

        String result = message+"\n"+hex;
        runOnUiThread(() -> {
                addChatMessage(result, IN_MSG);
        });
    }

    public String byteBufferToHex(byte[] inMsgByteBuffer,int count){
    //перевод в hex
        StringBuilder sb = new StringBuilder();

            for (int i = 0; i < count; i++) {

                    if (inMsgByteBuffer[i] == 10 ) {
                        sb.append(String.format("%02X\n ", inMsgByteBuffer[i]));
                    } else {
                        sb.append(String.format("%02X ", inMsgByteBuffer[i]));
                    }

            }
        return sb.toString();
    }
    @Override
    public void onTCPConnectionStatusChanged(boolean isConnectedNow) {
        if(isConnectedNow)
        {
            runOnUiThread(() -> {
                dialog.hide();
                connectToServer = true;
                if(is_fragment_TcpIP){
                    menu_switch_btn.setIcon(R.drawable.ic_podkl);
                }
            });
        }
    }

    private void setupDialog() {//в место устаревшего ProgressDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar progressBar = new ProgressBar(this);
        layout.setPadding(50,50,0,40);
        progressBar.setIndeterminate(true);
        layout.addView(progressBar);

        TextView textView = new TextView(this);
        textView.setText(R.string.please_wait);
        textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        textView.setTextSize(14);
        textView.setPadding(50,50,0,40);
        layout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.connecting);
        builder.setCancelable(true);
        builder.setView(layout);

        dialog = builder.create();
        dialog.show();
    }
    /*public void TimePaused(long i){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {public void run() { }}, i);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate( R.menu.menu_tcp_ip_client, menu );
        menu_clearChat = menu.findItem(R.id.clearChat);
        menu_switch_btn = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
     return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                DisconnectToServer();
                replaceFragment(new SharedPreferenceFragment(), "SharedPreferenceFragment");
                return true;
            case R.id.action_Connect_Disconnect_TCP_IP:
                if (connectToServer == true&&is_fragment_TcpIP == true){
                    DisconnectToServer();
                    //item.setIcon(android.R.drawable.checkbox_off_background);
                    item.setIcon(R.drawable.ic_otkl);
                }
               else if (connectToServer == false&&is_fragment_TcpIP == true){
                    ConnectToServer();
                }
               else if (connectToServer == false&&is_fragment_TcpIP == false){
                    if(ipAdr != null|| port != null) {
                        onStartFragmentTCP_IP();
                    } else {tostShort( "Error" );}
                }
               return true;
            case R.id.clearChat:
                db_chat_Adapter.deleteMesagesAllChat();
                db_chat_Adapter.createTableIfNotExists();
                clearRecyclerView();
                return true;
            default:
               break;
        }
            return super.onOptionsItemSelected(item);
        }
//=================================================================================
}
