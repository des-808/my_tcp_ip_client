package com.example.des808.my_tcp_ip_client;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.adapter.ChatMessageAdapter;
import com.example.des808.my_tcp_ip_client.adapter.ChatsTitleAdapter;
import com.example.des808.my_tcp_ip_client.fragments.SharedPreferenceFragment;
import com.example.des808.my_tcp_ip_client.fragments.fragment_TCP_IP;
import com.example.des808.my_tcp_ip_client.fragments.fragment_exit;
import com.example.des808.my_tcp_ip_client.fragments.fragment_titles;
import com.example.des808.my_tcp_ip_client.interfaces.OnSettingsFragment;
import com.example.des808.my_tcp_ip_client.interfaces.TCPListener;
import com.example.des808.my_tcp_ip_client.interfaces.onFragment_TCP_IP_Init;
import com.example.des808.my_tcp_ip_client.interfaces.onListViewFragmentTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//import static com.example.des808.my_tcp_ip_client.TCPCommunicator.removeAllListeners;
/* // greenrobot eventbus урок на ютубе
    //https://www.youtube.com/watch?v=WnzSkRinnuc*/



public class my_tcp_ip_client extends AppCompatActivity
    implements  fragment_titles.OnFragmentInteractionListener,
                fragment_titles.OnFragmentItemClickListener,
        onListViewFragmentTitle,
        onFragment_TCP_IP_Init,
        TCPListener,
        OnSettingsFragment,
                //fragment_TCP_IP.OnFragmentItemClickListenerSwitch,
                fragment_TCP_IP.OnFragmentInteractionListener
{

    private static final String LOG_TAG = "LOG_TAG";
    //private ArrayList<TitleChatsItems> list;
    private ChatsTitleAdapter C_Adapter;
    private final boolean IN_MSG = false;
    private final boolean OUT_MSG = true;
    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;
   // final int MENU_CANCEL = 3;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean vedromeda_bool;
    public boolean connectToServer = false;
    public String mMessage;
    public Toast toast;
    public String table_name, param2, param3;
    public TitleChatsItems items;
    ListView LVMain;

    public ActionBar actionBar;
    private ProgressDialog dialog;
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

    private static final String PREFS_FILE = "com.example.des808.my_tcp_ip_client_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;

    SharedPreferences sharedPreferencesMain;
    SharedPreferences.Editor prefEditor;

    List<Chat> chatsList = new ArrayList<>();
    RecyclerView recyclerView;//создаем переменную для отображения сообщений
    Vibrator vibrator;

    DBChatAdapter db_chat_Adapter; //создаем переменную для работы с базой данных
    ChatMessageAdapter chatMessageAdapter;//создаем переменную для работы с clickable

    public  boolean isHaveVibrate(){//Проверка наличия вибрации
        return vibrator.hasVibrator();//если нет вибрации то возвращаем false
    }

    @Override
    public void on_ListViewFragmentTitleInit() {
        //сработает когда запустится фрагмент fragment_title
        refreshTitleList();//перезагружает список
        final ListView newlist = findViewById( R.id.list );//fragment_title
        registerForContextMenu( newlist ); //если  раньше запускать будет ошибка. фрагменты не мгновенно запускаются
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void on_SettingsFragment(){
        //////////////////////////////////////////////////
        /*SharedPreferences.Editor editor = sharedPreferencesMain.edit();
        editor.putString("key", "value");//Добавить параметр
        editor.remove("key");//удалить параметр
        editor.apply();*/
        // Snackbar.make(sv, String.valueOf(sharedPreferencesMain.getBoolean(KEY_ANDROMEDA, false)), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void on_FragmentTCP_IP_Init() {
        //сработает когда запустится фрагмент fragment_tcp_ip
        is_fragment_TcpIP = true; //флаг наличия запущенного фрагмента
        String xparam = table_name +"  |  "+param2 + ":" + param3;
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
        menu_clearChat.setVisible(true);
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
        chat.setId((int) insert);
        //Log.d(LOG_TAG, "AddMessage id = " + insert);
        chatsList.add(chat);//выводим сообщение в список
        refreshChatListView();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void refreshChatListView(){
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
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
        actionBar = this.getActionBar();
        fragTitles = new fragment_titles();
        fragTCP_IP = new fragment_TCP_IP();
        sv = findViewById(R.id.FrLay);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        sharedPreferencesMain = getSharedPreferences(PREFS_FILE, PREFS_MODE);
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        db_chat_Adapter = new DBChatAdapter(this);
        chatMessageAdapter = new ChatMessageAdapter( chatsList);
        chatMessageAdapter.setOnItemClickListener((position, view, chat) -> {
            int pos = chat.getId();
            //Drawable foreground = view.getForeground();
            //view.setForeground(foreground);
            // view.setBackgroundColor(Color.parseColor("#0000FF"));
           Log.d(LOG_TAG, "onItemClick " + position + " onItemClick id=" + pos);
        });

        chatMessageAdapter.setOnItemLongClickListener((position, view, chat) -> {
            int pos = chat.getId();
            int remove =  db_chat_Adapter.deleteMessage(pos);
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
    protected void onResume() {super.onResume();
    }

    @Override
    protected void onStart() {super.onStart();}

    @Override
    public void onFragmentInteraction(Uri uri) {
        tost("onFragmentInteraction");
    }

    public void replaceFragment(Fragment fragment, String tag){
        Fragment currentFragment = fManager.findFragmentById(R.id.FrLay);
        if(currentFragment.getClass() == fragment.getClass()){return;}
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
            fManager.popBackStack();
        }else if(fragmentsInStack == 1){
            dialogFragment = new fragment_exit( "Так ты точно хочешь выйти???" );
            dialogFragment.show( fManager, "dialog" );
        }
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void doPositiveClick() {this.finish();}
    public void doNegativeClick() {}

    @Override
    protected void onPause() {
        super.onPause();
      //boolean is_andromeda = mPreferences.getAndromeda();
      //prefEditor = settings.edit();
      //prefEditor.putBoolean( String.valueOf(PREFS_ANDROMEDA), is_andromeda ); //перезаписываем настройки
      //prefEditor.commit(); //сохраняем настройки
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //settings.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        //EventBus.getDefault().unregister( this );
        //Log.d(LOG_TAG, "onDestroy");
        try {
            DisconnectToServer();
            DisconnectToServer();
        }catch (Exception e) {
            Log.d(LOG_TAG, String.valueOf(e));
        }
    }

    public void onClickBtnAdd(View v) {
        //создаём аллерт диалог нового подключения
        openAddDialog();
    }

    private void refreshTitleList() {
       ArrayList<TitleChatsItems> list = DBManager.getInstance( this ).getAllContacts();
        //list = DBManager.getInstance( this ).getAllContacts();
        C_Adapter = new ChatsTitleAdapter( this, list );
        LVMain = findViewById( R.id.list );
        LVMain.setAdapter( C_Adapter );
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

    @Override
    public void onClickSelected(int position) {
        items = C_Adapter.getItem( position );
        //DBManager.getInstance( getApplicationContext() ).getAllContacts(items);
        HashMap<String, String> values = (DBManager.getInstance( getApplicationContext() ).readContact( position ));
        table_name = values.get( "param1" );
        param2 = values.get( "param2" );
        param3 = values.get( "param3" );
        //Log.d(LOG_TAG, String.valueOf( xparam ) );
        ConnectToTable();// создаём или добавляем таблицу в базу
        onStartFragmentTCP_IP();
    }
    /*TODO
     * создаём или добавляем таблицу в базу
     * */
    public void ConnectToTable(){
        DBChatHelper.setTableName(table_name);
        db_chat_Adapter.createTableIfNotExists();
    }
    public void onStartFragmentTCP_IP() {
        replaceFragment(new fragment_TCP_IP(), "fragment_TCP_IP");
    }

    public void sendTx(View v)  {
        vibrator.vibrate(100);
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
        vibrator.vibrate(100);
            EditText  E_Send = findViewById( R.id.EChat_Send );
            String E_text = E_Send.getText().toString();
            if(E_text.isEmpty()) {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            E_Send.setText( "" );
            if (TCPCommunicator.writeToSocket(E_text, UIHandler, this) == TCPCommunicator.TCPWriterErrors.OK) {
                addChatMessage(E_text,OUT_MSG);
            } else {
                tost("ошибка передачи сообщения");
            }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info != null ? info.position : 0;
        items = C_Adapter.getItem( position );
        switch (item.getItemId()) {
            case MENU_RENAME:
                openRemoveDialog( items );
                break;
            case MENU_DELETE:
                openDeleteDialog( items );
                break;
                //case MENU_CANCEL:break;
            default:
                break;
        }
        return super.onContextItemSelected( item );//false;//
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu( menu, v, menuInfo );
        menu.add( 0, MENU_RENAME, 0, "Редактировать" );
        menu.add( 0, MENU_DELETE, 0, "Удалить" );
        //menu.add( 0, MENU_CANCEL, 0, "Отмена" );
    }

    public void openAddDialog() {
        LayoutInflater dlgInfater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View root = dlgInfater.inflate( R.layout.row_pod_menu, null );
        final EditText name_ =  root.findViewById( R.id.detailsName );
        final EditText ipadr_ = root.findViewById( R.id.detailsIpAdr );
        final EditText port_ =  root.findViewById( R.id.detailsPort );

        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( root );
        builder.setMessage( "Добавить запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            TitleChatsItems item = new TitleChatsItems(
                    name_.getText().toString(),
                    ipadr_.getText().toString(),
                    port_.getText().toString() );
            DBManager.getInstance( getApplicationContext() ).addContact( item );
            //list.add( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());
        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openRemoveDialog(final TitleChatsItems item) {
        LayoutInflater dlgInfater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View root = dlgInfater.inflate( R.layout.row_pod_menu, null );
        final EditText name_ = root.findViewById( R.id.detailsName );
        final EditText ipadr_ = root.findViewById( R.id.detailsIpAdr );
        final EditText port_ = root.findViewById( R.id.detailsPort );

        name_.setText( item.getName() );
        ipadr_.setText( item.getIp_adr() );
        port_.setText( item.getPort() );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( root );
        builder.setMessage( "Редактировать запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            item.setName( name_.getText().toString() );
            item.setIp_adr( ipadr_.getText().toString() );
            item.setPort( port_.getText().toString() );

            DBManager.getInstance( getApplicationContext() ).updateContact( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openDeleteDialog(final TitleChatsItems item) {
        LayoutInflater dlgInflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        dlgInflater.inflate( R.layout.row_pod_menu, null );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( String.format( "Удалить контакт %s?", item.getName() ) );

        builder.setPositiveButton( "Удалить", (dialog, id) -> {
            DBManager.getInstance( getApplicationContext() ).deleteContact( item.getID() );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    private void setupDialog() {
        dialog = new ProgressDialog( this, ProgressDialog.STYLE_SPINNER );//STYLE_SPINNER
        dialog.setTitle( "Connecting" );
        dialog.setMessage( "Please wait..." );
        dialog.setIndeterminate( true );
        dialog.show();
    }

    private void ConnectToServer() {
            if(!connectToServer) {
                setupDialog();
                TCPCommunicator tcpClient = TCPCommunicator.getInstance();
                TCPCommunicator.addListener( this );
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
                //DataStore settings = PreferenceManager.getDefaultSharedPreferences( this );
                    tcpClient.init( settings.getString( EnumsAndStatics.SERVER_IP_PREF, param2 ),
                            parseInt( settings.getString( EnumsAndStatics.SERVER_PORT_PREF, param3 ) ) );
            }
    }

    private void DisconnectToServer(){
        if(TCPCommunicator.getInstance()!= null){
            TCPCommunicator.closeStreams();
            TCPCommunicator.removeAllListeners();
            connectToServer = false;
            menu_switch_btn.setIcon(android.R.drawable.checkbox_off_background);
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

    @Override
    public void onTCPMessageRecievedCharBuffer(char[] inMsgCharBuffer, int count, int len) {
        StringBuilder inMsg = new StringBuilder();
						int index = 0;
						if(count >= 0) {
                            do {
                                //if ((inMsgCharBuffer[index]) != '\n' && (inMsgCharBuffer[index]) != 0x0D && (inMsgCharBuffer[index] >= 0 && (inMsgCharBuffer[index]) < 32)) {
                                if ((inMsgCharBuffer[index]) != '\n' && (inMsgCharBuffer[index]) != 0x0D && (inMsgCharBuffer[index] > 0 && (inMsgCharBuffer[index]) < 32)) {
                                    inMsg.append(DecimalToHex.toHex(inMsgCharBuffer[index]));
                                } else {
                                    inMsg.append((inMsgCharBuffer[index]));
                                }
                                index++;
                            } while (count != index);
                        }
        runOnUiThread(() -> {
            //tost("onTCPMessageRecieved: "+theMessage);
            addChatMessage(String.valueOf(inMsg),IN_MSG);
        });
                            inMsg.delete(0,count); // обнуляем буфер
    }

    public void onTCPMessageRecievedChar(final char messageChar){
        String msg= String.valueOf( messageChar );
        String msg2 = "";
        if(messageChar == 0x06){
           msg = " ACK\n";
        }
        if(messageChar == 0x15){
            msg = " NACK\n";
        }
        if (!msg.equals("\n")){
            msg2 = msg2 + messageChar;
        }
        else {
            msg = msg2;
        }
       final String theMessageChar = msg;

        runOnUiThread(() -> addChatMessage(theMessageChar,IN_MSG));
    }
    public void onTCPMessageRecievedInt(final Integer messageInt) {
        final int theMessageInt = messageInt;
        String Message = "";
        switch (theMessageInt){
            case ACK:
                Message = (" ACK");break;
            case NACK:
                Message = (" NACK");break;
            case SUCCESSFULLY:
                Message = (" OK");break;
            default:break;
        }
        /*if (theMessageInt == -1){

        }*/
        final String msg = (Message);
        runOnUiThread(() -> addChatMessage(msg,IN_MSG));
    }

    @Override
    public void onTCPConnectionStatusChanged(boolean isConnectedNow) {
        if(isConnectedNow)
        {
            runOnUiThread(() -> {
                dialog.hide();
                connectToServer = true;
                if(is_fragment_TcpIP == true){
                    menu_switch_btn.setIcon(android.R.drawable.checkbox_on_background);
                }
            });
        }
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
                    item.setIcon(android.R.drawable.checkbox_off_background);
                }
               else if (connectToServer == false&&is_fragment_TcpIP == true){
                    ConnectToServer();
                }
               else if (connectToServer == false&&is_fragment_TcpIP == false){
                    if(param2!= null||param3!= null) {
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
