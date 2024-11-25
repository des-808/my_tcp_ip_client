package com.example.des808.my_tcp_ip_client.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.des808.my_tcp_ip_client.R;
import com.example.des808.my_tcp_ip_client.adapter.ChatsTitleAdapter;
import com.example.des808.my_tcp_ip_client.classs.Adress;
import com.example.des808.my_tcp_ip_client.classs.TitleChatsItems;
import com.example.des808.my_tcp_ip_client.db.DBChatAdapter;
import com.example.des808.my_tcp_ip_client.db.DBChatHelper;
import com.example.des808.my_tcp_ip_client.db.DBManager;
import com.example.des808.my_tcp_ip_client.interfaces.onStartFragmentTcpIp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class fragment_titles extends Fragment
{
    //private OnFragmentItemClickListener mClickCallback;
   // private OnFragmentInteractionListener mListener;
    private onStartFragmentTcpIp OnStartFragmentTcpIp;
    //private static final String LOG_TAG = "LOG_TAG" ;

    private ChatsTitleAdapter C_Adapter;
    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;
    //final int MENU_CANCEL = 3;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean connectToServer = false;
    public String mMessage;
    public Toast toast;
    public String table_name, ipadr, port;
    public TitleChatsItems items;
    ListView LVMain;
    private Bundle savedInstanceState;
    DBChatAdapter db_chat_Adapter; //создаем переменную для работы с базой данных

    public fragment_titles() {}// Required empty public constructor

    // TODO: Rename and change types and number of parameters
    //@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static fragment_titles newInstance(int number) {
        Bundle args = new Bundle();
        fragment_titles fragment = new fragment_titles();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //ONListViewFragmentTitle.on_ListViewFragmentTitleInit() ; //шлем в активити привет. что фрагмент запустился. пускай шлёт пожелания
        //on_ListViewFragmentTitleInit() ; //шлем в активити привет. что фрагмент запустился. пускай шлёт пожелания
        //сработает когда запустится фрагмент fragment_title
        refreshTitleList();//перезагружает список
        final ListView newlist = getView().findViewById( R.id.list );//fragment_title
        registerForContextMenu( newlist ); //если  раньше запускать будет ошибка. фрагменты не мгновенно запускаются
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate( savedInstanceState );
        //if (getArguments() != null) {}
    }
    //######################################################################################################
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_title, container, false);
        db_chat_Adapter = new DBChatAdapter(getActivity());//создаём адаптер
        ListView newlist = (ListView) v.findViewById(R.id.list);
        newlist.setOnItemClickListener((arg0, arg1, position, arg3) -> onClickSelected( position ));
        FloatingActionButton btnAdd = (FloatingActionButton) v.findViewById(R.id.but_add);
        btnAdd.setOnClickListener(v1 -> {openAddDialog();});
        return v;
    }

    //######################################################################################################

    @Override
    public void onAttach(@Nullable Context context) {
        super.onAttach(context);
        if (context instanceof onStartFragmentTcpIp)
        {
            try {
                OnStartFragmentTcpIp = (onStartFragmentTcpIp) context;
            }catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString() + " must implement onStartFragmentTcpIp");
            }
        }
        /*try {
            mClickCallback = (OnFragmentItemClickListener) context;
        } catch (ClassCastException e) { throw new ClassCastException(context.toString() + " must implement OnFragmentItemClickListener");}*/
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else { throw new RuntimeException( context.toString() + " must implement OnFragmentInteractionListener" );}*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }
    @Override
    public void onStop() {
        super.onStop();
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

    private void refreshTitleList() {
        ArrayList<TitleChatsItems> list = DBManager.getInstance( getActivity() ).getAllContacts();
        C_Adapter = new ChatsTitleAdapter(getActivity() != null ? getActivity() : null, list );
        LVMain = getView().findViewById( R.id.list );
        LVMain.setAdapter( C_Adapter );
    }

    public void openAddDialog() {
        LayoutInflater dlgInfater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        View root = dlgInfater != null ? dlgInfater.inflate(R.layout.row_pod_menu, null) : null;
        final EditText name_ = root != null ? root.findViewById(R.id.detailsName) : null;
        final EditText ipadr_ = root != null ? root.findViewById(R.id.detailsIpAdr) : null;
        final EditText port_ = root != null ? root.findViewById(R.id.detailsPort) : null;

        final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( root );
        builder.setMessage( "Добавить запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            TitleChatsItems item = new TitleChatsItems(
                    name_ != null ? name_.getText().toString() : null,
                    ipadr_ != null ? ipadr_.getText().toString() : null,
                    port_ != null ? port_.getText().toString() : null);
            DBManager.getInstance( getContext() ).addContact( item );
            //list.add( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());
        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openRemoveDialog(final TitleChatsItems item) {
        LayoutInflater dlgInfater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        View root = dlgInfater != null ? dlgInfater.inflate(R.layout.row_pod_menu, null) : null;
        final EditText name_ = root != null ? root.findViewById(R.id.detailsName) : null;
        final EditText ipadr_ = root != null ? root.findViewById(R.id.detailsIpAdr) : null;
        final EditText port_ = root != null ? root.findViewById(R.id.detailsPort) : null;

        Objects.requireNonNull(name_).setText( item.getName() );
        ipadr_.setText( item.getIp_adr() );
        port_.setText( item.getPort() );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( root );
        builder.setMessage( "Редактировать запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            item.setName( name_.getText().toString() );
            item.setIp_adr( ipadr_.getText().toString() );
            item.setPort( port_.getText().toString() );

            DBManager.getInstance( getContext() ).updateContact( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openDeleteDialog(final TitleChatsItems item) {
        LayoutInflater dlgInflater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        Objects.requireNonNull(dlgInflater).inflate( R.layout.row_pod_menu, null );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setMessage( String.format( "Удалить контакт %s?", item.getName() ) );

        builder.setPositiveButton( "Удалить", (dialog, id) -> {
            DBManager.getInstance( getContext() ).deleteContact( item.getID() );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void onClickSelected(int position) {
        items = C_Adapter.getItem( position );
        HashMap<String, String> values = (DBManager.getInstance( getContext() ).readContact( position ));
        table_name = values.get( "param1" );
        ipadr = values.get( "param2" );
        port = values.get( "param3" );
        Adress adr = new Adress(table_name, ipadr, port );
        // создаём или добавляем таблицу в базу
        DBChatHelper.setTableName(table_name);
        db_chat_Adapter.createTableIfNotExists();
        //шлем в активити привет. что пора запускать фрагмент.
        OnStartFragmentTcpIp.on_startFragmentTcpIp(adr);
        //onStartFragmentTCP_IP();
    }
}