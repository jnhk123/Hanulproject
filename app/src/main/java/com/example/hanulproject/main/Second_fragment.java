package com.example.hanulproject.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hanulproject.MainActivity;
import com.example.hanulproject.R;
import com.example.hanulproject.main.statusTask.StatusSelect;
import com.example.hanulproject.menu.status.HomeStatus.CallStatusValue;
import com.example.hanulproject.menu.status.HomeStatus.HomeBoilerView;
import com.example.hanulproject.menu.status.HomeStatus.HomeLightView;
import com.example.hanulproject.menu.status.HomeStatus.HomeSecurityView;
import com.example.hanulproject.menu.status.HomeStatus.HomeWindowView;
import com.example.hanulproject.vo.StatusVO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Second_fragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    LinearLayout water, light, secom, gas, boiler, window, status;
    MainActivity activity = new MainActivity();
    ImageView onLight;
    ImageView offLight;
    public static StatusVO statusVO;
    public static ProgressDialog dialog;

    //값받아올때까지 쓰레드 정지
    public static boolean status_is_check = true;


    static private TextView mConnectionStatus;
//    private EditText mInputEditText;
//    private ArrayAdapter<String> mConversationArrayAdapter;

    private static final String TAG = "TcpClient";
    private boolean isConnected = false;

    private String mServerIP = null;
    private Socket mSocket = null;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private Thread mReceiverThread = null;

    //상태체크
    int is_light = 3; // 1: 전구켜짐 2: 전구꺼짐
    int is_window = 3; // 1: 문열림 2: 문닫힘

    // newInstance constructor for creating fragment with arguments
    public static Second_fragment newInstance(int page, String title) {
        Second_fragment fragment = new Second_fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
//        CallStatusValue callStatusValue = new CallStatusValue();
//        callStatusValue.execute();
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        StatusSelect statusSelect = new StatusSelect();
        statusSelect.setProgressDialog(dialog);
        try {
            statusVO = statusSelect.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        FragmentManager manager=(getActivity().getSupportFragmentManager());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.main_fragment2, container, false);
        light = rootview.findViewById(R.id.status_light);
        secom = rootview.findViewById(R.id.status_secom);
        boiler = rootview.findViewById(R.id.status_boiler);
        window = rootview.findViewById(R.id.status_window);
        status=rootview.findViewById(R.id.status);
        mConnectionStatus = rootview.findViewById(R.id.mConnectionStatus);
        onLight = rootview.findViewById(R.id.onLight);
        offLight = rootview.findViewById(R.id.offLight);
        onLight.setVisibility(View.VISIBLE);
        offLight.setVisibility(View.GONE);


        // 값을 아두이노로 보내고 싶을때는 senderThread 를 사용해서 매개변수로 값을 넘기면 됨

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구현함
                getFragmentManager().beginTransaction().replace(R.id.status,new HomeLightView() ).commit();
            }
        });
        secom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구현함
                getFragmentManager().beginTransaction().replace(R.id.status, new HomeSecurityView()).commit();
            }
        });

        boiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구현함
                getFragmentManager().beginTransaction().replace(R.id.status, new HomeBoilerView()).commit();
            }
        });
        window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구현함
                getFragmentManager().beginTransaction().replace(R.id.status, new HomeWindowView() ).commit();
            }
        });

//        new Thread(new ConnectThread("192.168.0.92", 80)).start();

        return rootview;
    }

    // Don't touch bottom of the line
    //======================================================================================================
    // 밑부터는 와이파이 연결하는 용도 클래스들
    // 손대지 마시오 !!
    // 값을 아두이노로 보내고 싶을때는  new Thread(new SenderThread("보낼값")).start(); 이렇게 사용

    private class ConnectThread implements Runnable {

        private String serverIP;
        private int serverPort;

        ConnectThread(String ip, int port) {
            serverIP = ip;
            serverPort = port;

            mConnectionStatus.setText("connecting to " + serverIP + ".......");
        }

        @Override
        public void run() {

            try {

                mSocket = new Socket(serverIP, serverPort);
                //ReceiverThread: java.net.SocketTimeoutException: Read timed out 때문에 주석처리
                //mSocket.setSoTimeout(3000);

                mServerIP = mSocket.getRemoteSocketAddress().toString();

            } catch( UnknownHostException e )
            {
                Log.d(TAG,  "ConnectThread: can't find host");
            }
            catch( SocketTimeoutException e )
            {
                Log.d(TAG, "ConnectThread: timeout");
            }
            catch (Exception e) {

                Log.e(TAG, ("ConnectThread:" + e.getMessage()));
            }


            if (mSocket != null) {

                try {

                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")), true);
                    mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));

                    isConnected = true;
                } catch (IOException e) {

                    Log.e(TAG, ("ConnectThread:" + e.getMessage()));
                }
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (isConnected) {

                        Log.d(TAG, "connected to " + serverIP);
                        mConnectionStatus.setText("connected to " + serverIP);

                        mReceiverThread = new Thread(new ReceiverThread());
                        mReceiverThread.start();
                    }else{

                        Log.d(TAG, "failed to connect to server " + serverIP);
                        mConnectionStatus.setText("failed to connect to server "  + serverIP);
                    }

                }
            });
        }
    }

    private class SenderThread implements Runnable {

        private String msg;

        SenderThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {

            mOut.println(this.msg);
            mOut.flush();

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d(TAG, "send message: " + msg);
//                    mConversationArrayAdapter.insert("Me - " + msg, 0);
//                }
//            });
        }
    }

    private class ReceiverThread implements Runnable {

        @Override
        public void run() {

            try {

                while (isConnected) {

                    if ( mIn ==  null ) {

                        Log.d(TAG, "ReceiverThread: mIn is null");
                        break;
                    }

                    final String recvMessage =  mIn.readLine();

                    if (recvMessage != null) {
                        Log.d(TAG, "recv message: "+recvMessage);
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                                Log.d(TAG, "recv message: "+recvMessage);
//                                mConversationArrayAdapter.insert(mServerIP + " - " + recvMessage, 0);
//                            }
//                        });
                    }
                }

                Log.d(TAG, "ReceiverThread: thread has exited");
                if (mOut != null) {
                    mOut.flush();
                    mOut.close();
                }

                mIn = null;
                mOut = null;

                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {

                Log.e(TAG, "ReceiverThread: "+ e);
            }
        }

    }

    public void showErrorDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                finish();
            }
        });
        builder.create().show();
    }

}
