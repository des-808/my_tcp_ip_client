package com.example.des808.my_tcp_ip_client;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.des808.my_tcp_ip_client.adapter.CustomAdapter;
import com.example.des808.my_tcp_ip_client.adapter.MessageAdapter;
import com.example.des808.my_tcp_ip_client.fragments.SharedPreferenceFragment;
import com.example.des808.my_tcp_ip_client.fragments.fragment_TCP_IP;
import com.example.des808.my_tcp_ip_client.fragments.fragment_exit;
import com.example.des808.my_tcp_ip_client.fragments.fragment_titles;
import com.example.des808.my_tcp_ip_client.interfaces.OnSettingsFragment;
import com.example.des808.my_tcp_ip_client.interfaces.TCPListener;
import com.example.des808.my_tcp_ip_client.interfaces.onFragment_TCP_IP_Init;
import com.example.des808.my_tcp_ip_client.interfaces.onListViewFragmentTitle;
import com.google.android.material.snackbar.Snackbar;

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
                fragment_TCP_IP.OnFragmentInteractionListener{

    private static final String LOG_TAG = "LOG_TAG";
    private ArrayList<TitleChatsItems> list;
    private CustomAdapter C_Adapter;
    private ListView listView;
    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;
    final int MENU_CANCEL = 3;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean vedromeda_bool;
    public boolean connectToServer = false;
    public int i;
    public int x;
    public String mMessage;
    DBHelper dbHelper;
    DBChatHelper dbChatHelper;
    public Toast toast;
    public String npc;
    public String param1, param2, param3;
    public TitleChatsItems items;
    public TextView chatText;
    ListView LVMain;

    public CharSequence message;
    public ActionBar actionBar;
    private ProgressDialog dialog;
    private RecyclerView chatRecyclerView;
    public static String currentUserName;
    private final Handler UIHandler = new Handler();
    public MenuItem menu_andromeda;
    public MenuItem menu_switch_btn;

    public EditText object,clas,razd,schs;
    public ImageButton btnSend_tx;

    public boolean is_fragment_TcpIP = false;
    fragment_exit dialogFragment;
    fragment_titles fragTitles;
    fragment_TCP_IP fragTCP_IP;
    FragmentManager fManager;
    FragmentTransaction fTrans;
    View sv;

    //private static final String PREFS_FILE = "my_tcp_ip_client_preferences";
    private static final String PREFS_FILE = "root_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;
    Preferences_Class preferences_class;

    ArrayList<MessageChat> chats = new ArrayList<MessageChat>();
    RecyclerView recyclerView;//создаем переменную для отображения сообщений
    Vibrator vibrator;

    DBChatAdapter adapter; //создаем переменную для работы с базой данных

   /* void  boolean isHaveVibrate(){//Проверка наличия вибрации
        return vibrator.hasVibrator();//если нет вибрации то возвращаем false
    }*/

    @Override
    public void on_ListViewFragmentTitleInit() {
        //сработает когда запустится фрагмент fragment_title
        refreshList();//перезагружает список
        final ListView newlist = (ListView) findViewById( R.id.list );//fragment_title
        registerForContextMenu( newlist ); //если  раньше запускать будет ошибка. фрагменты не мгновенно запускаются
    }

    @Override
    public void on_FragmentTCP_IP_Init() {
        //сработает когда запустится фрагмент fragment_tcp_ip
        is_fragment_TcpIP = true; //флаг наличия запущенного фрагмента
        String xparam = param1+"  |  "+param2 + ":" + param3;
        TextView x = (TextView) findViewById( R.id.connect_text );
        x.setText( xparam );
        //EditText y = (EditText) findViewById( R.id.EChat_Send );
        //y.setText( "" );
        object =     (EditText) findViewById( R.id.editObjekt );
        clas =       (EditText) findViewById( R.id.editClass );
        razd =       (EditText) findViewById( R.id.editRazd );
        schs =       (EditText) findViewById( R.id.editSchs );
        btnSend_tx = (ImageButton) findViewById( R.id.buttonSend_tx );
        //sw = (Switch) findViewById(R.id.switch1);
        // начальная инициализация списка
        // initTestChatRecyclerView();
        List<String> list = new ArrayList<>();
        initRecyclerView();
        clearRecyclerView();
        ConnectToServer();
    }
    private  void initRecyclerView(){
        recyclerView = findViewById(R.id.list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // создаем адаптер
        MessageAdapter adapter = new MessageAdapter(this, chats);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    public void addChatMessage(String i){
Chat chat = new Chat(param1);
MessageChat mChat = new MessageChat(param1,i);

        chats.add(mChat);
            chat.setMessage_in(mChat.getMessage());
            chat.setData_in_message(mChat.getTime());
            chat.setMessage_out("ни чего не отправляю");
            chat.setData_out_message("ни в какое время");
        adapter.open();
     // long count = adapter.getCount(param1);
      //tost(String.valueOf(count));
        adapter.addMessage(param1,chat);
        adapter.close();
        refreshChatListView();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void refreshChatListView(){
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void clearRecyclerView() {
        chats.clear();
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }
    private void initTestChatRecyclerView() {
        chats.add(new MessageChat (param1,"Бразилия"));
        chats.add(new MessageChat (param1,"Аргентина"));
        chats.add(new MessageChat (param1,"Колумбия"));
        chats.add(new MessageChat (param1,"Уругвай"));
        chats.add(new MessageChat (param1,"Чили"));
    }

    @Override
    public void on_FragmentTCP_IP_Disconnect(){
        is_fragment_TcpIP = false;//флаг отсутствия запущенного фрагмента
        DisconnectToServer();
    }

    @Override
    public void on_FragmentTCP_IP_Switch() {
        tost("Сработка");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_tcp_ip_client );
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        actionBar = this.getActionBar();
        fragTitles = new fragment_titles();
        fragTCP_IP = new fragment_TCP_IP();
        final FrameLayout edit = (FrameLayout) findViewById( R.id.FrLay );
        sv = findViewById(R.id.FrLay);
        //////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences(PREFS_FILE, PREFS_MODE);
        preferences_class = new Preferences_Class();
        preferences_class.setAndromeda(sharedPreferences.getBoolean(KEY_ANDROMEDA,false));

        //////////////////////////////////////////////////
        adapter = new DBChatAdapter(this);
        //////////////////////////////////////////////////
        fManager = getSupportFragmentManager();
        if(null == savedInstanceState){
            fManager.beginTransaction()
                    .addToBackStack("fragment_titles")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.FrLay,fragTitles,"fragment_titles")
                    //.replace(R.id.FrLay,fragTitles,"fragment_titles")
                    .commit();
        }
        //fManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_MATCH_ACTIVITY_OPEN).add(R.id.FrLay,new fragment_titles()).addToBackStack("fragment_titles").commit();
        //Log.d(LOG_TAG, "onCcreate");
    }

    /*public void save(View view){
        adapter.open();
        //chats.add();
            adapter.addMessage(param1,chat);
            //adapter.updateMessage(user);
        adapter.close();
    }*/

    @Override
    protected void onResume() {super.onResume();}

    /*private void initChatRecyclerView() {
        fTrans = getSupportFragmentManager().beginTransaction();
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }*/

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
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
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
    @Override
    public void on_SettingsFragment(Preferences_Class p_class){
        //////////////////////////////////////////////////
        preferences_class.setAndromeda(p_class.getAndromeda());
        Snackbar.make(sv, String.valueOf(preferences_class.getAndromeda()), Snackbar.LENGTH_SHORT).show();
        vedromeda_bool = preferences_class.getAndromeda();;
    }

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
       // EventBus.getDefault().unregister( this );
        //Log.d(LOG_TAG, "onDestroy");
        try {
            DisconnectToServer();
        }catch (Exception e){}
    }

    public void onClickBtnAdd(View v) {
        //создаём аллерт дилог нового подключения
        openAddDialog();
    }

    private void refreshList() {
        list = DBManager.getInstance( this ).getAllContacts();
        C_Adapter = new CustomAdapter( this, list );
        LVMain = (ListView) findViewById( R.id.list );
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
        param1 = values.get( "param1" );
        param2 = values.get( "param2" );
        param3 = values.get( "param3" );
        //Log.d(LOG_TAG, String.valueOf( xparam ) );
        onStartFragmentTCP_IP();
    }
    public void onStartFragmentTCP_IP() {
        replaceFragment(new fragment_TCP_IP(), "fragment_TCP_IP");
    }

    public void sendTx(View v)  {
        vibrator.vibrate(100);
            object = (EditText) findViewById(R.id.editObjekt);
            clas = (EditText) findViewById(R.id.editClass);
            razd = (EditText) findViewById(R.id.editRazd);
            schs = (EditText) findViewById(R.id.editSchs);
            String e_object = object.getText().toString();
            String e_clas = clas.getText().toString();
            String e_razd = razd.getText().toString();
            String e_schs = schs.getText().toString();
            String E_text = e_object + e_clas + e_razd + e_schs;

            String x = "5032 18";
            char i = (char) 20;
            E_text = x + E_text + i;//
            if (TCPCommunicator.writeToSocket(E_text, UIHandler, this) == TCPCommunicator.TCPWriterErrors.OK) {
                addChatMessage(E_text);
            } else {
                tost("ошибка передачи сообщения");
            }
    }

    public void sendChatTx(View v)  {
        vibrator.vibrate(500);
            EditText  E_Send = (EditText) findViewById( R.id.EChat_Send );
            String E_text = E_Send.getText().toString();
            if(E_text.length()==0) {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            E_Send.setText( "" );
            if (TCPCommunicator.writeToSocket(E_text, UIHandler, this) == TCPCommunicator.TCPWriterErrors.OK) {
                addChatMessage(E_text);
            } else {
                tost("ошибка передачи сообщения");
            }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
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
        final EditText name_ = (EditText) root.findViewById( R.id.detailsName );
        final EditText ipadr_ = (EditText) root.findViewById( R.id.detailsIpAdr );
        final EditText port_ = (EditText) root.findViewById( R.id.detailsPort );

        final AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( root );
        builder.setMessage( "Добавить запись" );

        builder.setPositiveButton( "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                TitleChatsItems item = new TitleChatsItems(
                        name_.getText().toString(),
                        ipadr_.getText().toString(),
                        port_.getText().toString() );
                DBManager.getInstance( getApplicationContext() ).addContact( item );
                //list.add( item );
                refreshList();
            }
        } ).setNegativeButton( "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        } );
        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openRemoveDialog(final TitleChatsItems item) {
        LayoutInflater dlgInfater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View root = dlgInfater.inflate( R.layout.row_pod_menu, null );
        final EditText name_ = (EditText) root.findViewById( R.id.detailsName );
        final EditText ipadr_ = (EditText) root.findViewById( R.id.detailsIpAdr );
        final EditText port_ = (EditText) root.findViewById( R.id.detailsPort );

        name_.setText( item.getName() );
        ipadr_.setText( item.getIp_adr() );
        port_.setText( item.getPort() );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( root );
        builder.setMessage( "Редактировать запись" );

        builder.setPositiveButton( "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                item.setName( name_.getText().toString() );
                item.setIp_adr( ipadr_.getText().toString() );
                item.setPort( port_.getText().toString() );

                DBManager.getInstance( getApplicationContext() ).updateContact( item );
                refreshList();
            }
        } ).setNegativeButton( "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        } );

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openDeleteDialog(final TitleChatsItems item) {

        LayoutInflater dlgInfater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View root = dlgInfater.inflate( R.layout.row_pod_menu, null );

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        //builder.setView( root );
        builder.setMessage( String.format( "Удалить контакт %s?", item.getName() ) );

        builder.setPositiveButton( "Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DBManager.getInstance( getApplicationContext() ).deleteContact( item.getID() );
                refreshList();
            }
        } ).setNegativeButton( "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        } );

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        //Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        //Log.d(LOG_TAG, "onSaveInstanceState");
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
           // tostShort( "Disconnected to Server" );
            menu_switch_btn.setIcon(android.R.drawable.checkbox_off_background);
        }
    }

    @Override
    public void onTCPMessageRecieved(String message) {
        // TODO Auto-generated method stub
        final String theMessage = message;
        mMessage = message;
        runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       //tost("onTCPMessageRecieved: "+theMessage);
                       addChatMessage(theMessage);
                       //chats.add(new MessageChat (theMessage));
                   }
               });
    }

    @Override
    public void onTCPMessageRecievedCharBuffer(char[] inMsgCharBuffer, int count, int len) {
        StringBuilder inMsg = new StringBuilder();
						int index = 0;
						if(count >= 0) {
                            do {
                                if ((inMsgCharBuffer[index]) != '\n' && (inMsgCharBuffer[index]) != 0x0D && (inMsgCharBuffer[index] >= 0 && (inMsgCharBuffer[index]) < 32)) {
                                    inMsg.append(DecimalToHex.toHex(inMsgCharBuffer[index]));
                                } else {
                                    inMsg.append((inMsgCharBuffer[index]));
                                }
                                index++;
                            } while (count != index);
                        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //tost("onTCPMessageRecieved: "+theMessage);
               // addChatMessage(theMessage);
                addChatMessage(String.valueOf(inMsg));
                //chats.add(new MessageChat (theMessage));
            }
        });
                            inMsg.delete(0,count); // обнуляем буфер
                            count = -1;// обнуляем счетчик
        //inMsg.
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
            msg2+=String.valueOf( messageChar );
        }
        else {
            msg = msg2;
        }
       final String theMessageChar = msg;

        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              addChatMessage(theMessageChar);;
                          }
        });
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
        if (theMessageInt == -1){

        }
        final String msg = (Message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addChatMessage(msg);
        }});
    }

    @Override
    public void onTCPConnectionStatusChanged(boolean isConnectedNow) {
        if(isConnectedNow)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                    //tostShort("Connected to server");
                    connectToServer = true;
                    if(is_fragment_TcpIP == true){
                        menu_switch_btn.setIcon(android.R.drawable.checkbox_on_background);
                    }
                }
            });
        }
    }

    public void TimePaused(long i){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {public void run() { }}, i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
     getMenuInflater().inflate( R.menu.menu_tcp_ip_client, menu );
        menu_switch_btn = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
     return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                DisconnectToServer();
                //fManager.beginTransaction().addToBackStack("fragment_blank").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.FrLay,new BlankFragment() ).commit();

                //replaceFragment(new BlankFragment(), "fragment_blank");
                replaceFragment(new SharedPreferenceFragment(), "SharedPreferenceFragment");

                //item.setChecked(!item.isChecked());
                return true;
            case R.id.action_Connect_Disconnect_TCP_IP:
                //item.setChecked(!item.isChecked());
                if (connectToServer == true&&is_fragment_TcpIP == true){
                    DisconnectToServer();
                    //item.setTitle( "Closed" );
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
            case R.id.xz:
                item.setChecked(!item.isChecked());
                return true;
            default:
               break;
        }
            return super.onOptionsItemSelected(item);
        }




//=================================================================================

}




 /*   public void initContacts() {
        //list = new ArrayList<adapter_listview>();
        list.add( new adapter_listview( "vedromeda", "gprs.so-ro.ru", "10003" ) );
        list.add( new adapter_listview( "vragi", "abrakadabra.ru", "10103" ) );
        //list.add( new adapter_listview( "neizvestno kto", "10.13.46.5", "2000" ) );
    }*/

/*  String txtName = name_.getText().toString();
                x = (TextView) findViewById( R.id.textViewName );
                x.setText( txtName );
                String txtipadr = ipadr_.getText().toString();
                y = (TextView) findViewById( R.id.textViewIpAdr );
                y.setText( txtipadr );
                String txtport = port_.getText().toString();
                z = (TextView) findViewById( R.id.textViewPort );
                z.setText( txtport );*/

 /* EditText textFragmentName = (EditText) findViewById( R.id.text_name );
                name = textFragmentName.getText().toString();
                textFragmentName.setText( "" );
                EditText textFragmentIpAdr = (EditText) findViewById( R.id.text_ipadress );
                ipadress = textFragmentIpAdr.getText().toString();
                textFragmentIpAdr.setText( "" );
                EditText textFragmentPort = (EditText) findViewById( R.id.text_port );
                port = textFragmentPort.getText().toString();
                textFragmentPort.setText( "" );
                mnb = true;
                fTrans.remove( frag2 );
                fTrans.add( R.id.FrLay, frag1 );
                fTrans.addToBackStack( null );
                fTrans.commit();*/

//FragmentManager fragmentManager = getFragmentManager();
// Получаем ссылку на второй фрагмент по ID
//Fragment fragtitl = (fragment_titles) fragmentManager.findFragmentById(R.id.textViewName);
//fragtitl.setText("Access to Fragment 2 from Activity");

                        /*Fragment fds = getFragmentManager().findFragmentById(R.id.text_port);
                        ((TextView) fds.getView().findViewById(R.id.textViewName))
                                .setText("Access to Fragment 2 from Activity");*/




//ЭТО РАБОТАЕТ
//выводит данные в текствиев активити (не фрагмента)
                        /* textViewName = (TextView) findViewById(R.id.textViewName);
                            textViewName.setText(name);
                         textViewIpAdr = (TextView) findViewById(R.id.textViewIpAdr);
                            textViewIpAdr.setText(ipadress);
                         textViewPort = (TextView) findViewById(R.id.textViewPort);
                        textViewPort.setText(port);*/



 /*  public void onClickSend(View v) {
        varconnectserver = true;
        fTrans.remove( frag1 );
        fTrans.add( R.id.FrLay, frag2 );
        fTrans.addToBackStack( null );
        fTrans.commit();

    }*/
 //fTrans = getFragmentManager().beginTransaction();
//ListView LVMain = (ListView) findViewById(R.id.list_view);
//ArrayAdapter<String> adapter1 = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, day_of_weeks );
//LVMain.setAdapter( adapter1 );