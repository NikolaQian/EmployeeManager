package com.huami.employeemanager.activity;

import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.huami.employeemanager.base.AppManager;
import com.huami.employeemanager.base.BaseActivity;
import com.huami.employeemanager.common.StringUtil;
import com.huami.employeemanager.common.UIHelper;
import com.huami.employeemanager.database.DBManager;
import com.huami.employeemanager.database.Employee;
import com.huami.employeemanager.fragment.DefaultFragment;
import com.huami.employeemanager.fragment.DisconnectedFragment;
import com.huami.employeemanager.fragment.FourPersonsFragment;
import com.huami.employeemanager.fragment.MoreThanFourPersonsFragment;
import com.huami.employeemanager.fragment.OnePersonFragment;
import com.huami.employeemanager.fragment.ThreePersonsFragment;
import com.huami.employeemanager.fragment.TowPersonsFragment;
import com.huami.employeemanager.thread.SetDefaultThread;
import com.huami.employeemanager.thread.SocketReceiveThread;
import com.huami.employeemanager.ui.DownLoadDialog;
import com.huami.employeemanager.ui.UpdateAPKDialog;
import com.huami.employeemanager.activity.R;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnGestureListener{
	//MainActivity�Ķ���
	public static MainActivity instance = null;
	//��ѯԱ��
	public static final int SELECT_EMPLOYEE = 0;
	//TOAST��Ϣ
	public static final int TOAST_MESSAGE = 1;
	//��ʾListview
	public static final int SET_DEFAULT = 2;
	//���ظ�������
	public static final int DOWNLOAD_DB = 3;
	//���ظ������
	public static final int UPDATE_APK = 4;
	//���ӳɹ�
	public static final int CONNECT_SUCCEED = 5;
	//���ӶϿ�
	public static final int DISCONNECTED = 6;
	
	private OnePersonFragment onePersonFragment;
	
	private TowPersonsFragment towPersonsFragment;
	
	private ThreePersonsFragment threePersonsFragment;
	
	private FourPersonsFragment fourPersonsFragment;
	
	private MoreThanFourPersonsFragment moreThanFourPersonsFragment;

    private ArrayList<Employee> listItem;
    
    private SocketReceiveThread socketThread;
    
//    private TextView text_other;
    //��һ�ΰ����˳���ʱ��
    private long firstTime;
    
    private View view;
    //��ǰhandler����ʱ��ʱ��
    private long currentTime;
    //��һ��handler�����ʱ��
    public static long previousTime = -3000;
    //���δ����msg����һ�δ�����Ƿ���ͬ
    public static boolean isSame;
    //�Ƿ�����ʱ��
    public static boolean isClear = true;
    //��ǰhandler�����msg���õ���JSONArray
    private JSONArray currentArray;
    //��һ��handler�����msg���õ���JSONArray
    private JSONArray previousArray;
    //��ǰhandler�����msg
    private String currentMsg;
    //�����һ��handler�����msg���õ���JSONArray
    private JSONArray saveFirstArray;
    //����handler�ļ���
    private int count = 1;
    
    private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SELECT_EMPLOYEE:
				currentTime = System.currentTimeMillis();
				
				currentMsg = (String)msg.obj;
				try {
					currentArray = new JSONObject(currentMsg).getJSONArray("das");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(count == 1){
					saveFirstArray = currentArray;
					count++;
				}
				
				if((currentTime - previousTime) < 2000){
					
					isSame = currentArray.equals(previousArray);
					
					Log.d("same", String.valueOf(isSame));
					Log.d("test", String.valueOf(String.valueOf(isSame).equals("false")));
					
					if(currentArray.equals(previousArray) == false){
						isClear = false;
					}
					
					if(saveFirstArray.equals(currentArray)){
						isClear = true;
					}
					
					Log.d("clear", String.valueOf(isClear));
					
					previousArray = currentArray;
					previousTime = System.currentTimeMillis();
					return;
				}
				
				previousTime = System.currentTimeMillis();
				previousArray = currentArray;
				
				listItem.clear();
				
				if(selectEmployee(currentMsg) == 0){
					return;
				}
				
				displayListView();
				
				SetDefaultThread setdefaultThread = new SetDefaultThread(handler);
				setdefaultThread.start();
				break;
			case TOAST_MESSAGE:
				Toast.makeText(MainActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case SET_DEFAULT:{
				listItem.clear();
				setDefaultFragment();
				previousTime = -3000;
				isClear = true;
				previousArray = null;
				count = 1;
			}
				break;
			case DOWNLOAD_DB:
				String keyWord = (String)msg.obj;
				downLoad(keyWord);
				break;
			case UPDATE_APK:
				String keyWord1 = (String)msg.obj;
				updateAPK(keyWord1);
			case CONNECT_SUCCEED:
				UIHelper.ToastMessage(MainActivity.this, R.string.connect_succeed);
				setDefaultFragment();
				break;
			case DISCONNECTED:
				UIHelper.ToastMessage(MainActivity.this, R.string.disconnected);
				setDisconnectedFragment();
				break;
			default:
				break;
			}
		}
		
	};
	
	protected int selectEmployee(String keyWord) {
		try {
			
			JSONArray jsonArray = new JSONObject(keyWord).getJSONArray("das");
			String[] str = new String[jsonArray.length()];
			for(int i=0; i<jsonArray.length(); i++){
				 str[i] = jsonArray.getString(i);
			}
			
			str = StringUtil.arrayUnique(str);
			
			
			DBManager dbmgr = new DBManager(this);
			
			
			for(String miID : str){
				if(dbmgr.query(miID).size() == 0){
					Employee employee = new Employee();
					employee.setName("δ֪");
					employee.setPhoto(DBManager.PHOTO_PATH + "/unknown.png");
//					UIHelper.ToastMessage(this, getResources().getString(R.string.bracelet_not_exist) + miID);
					listItem.add(0, employee);
					continue;
				}
				listItem.add(0, dbmgr.query(miID).get(0));
			}
			
			if(listItem.size() == 0){
				previousTime = -3000;
			}
			
			dbmgr.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listItem.size();
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		setAllowFullScreen(true);
		
		if (application.isTablet()) {
			//�ֻ�ƽ̨
		} else {
			view = View.inflate(this, R.layout.activity_main, null);
		}
		
		setContentView(view);
		setDisconnectedFragment();
		
		// ���������ж�
		if (!application.isNetworkConnected()) {
			UIHelper.ToastMessage(this, R.string.network_not_connected);
			finish(); 
		} else {
			if (application.isCheckUp()) {
				//����
			}
		}

		//�û���Ϣ��ʼ��
		application.initLoginInfo();

		if (!application.isTablet()) {
			initPortpaitView();
		}
		
		listItem = new ArrayList<Employee>();
		
		socketThread = new SocketReceiveThread(this, handler, application.getIpAdress());
		socketThread.start();
		
	}

	private void setDefaultFragment(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		DefaultFragment defaultFragment = new DefaultFragment();
		transaction.replace(R.id.fragment_main, defaultFragment);
		transaction.commit();
	}
	
	private void setDisconnectedFragment(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		DisconnectedFragment disconnectedFragment = new DisconnectedFragment();
		transaction.replace(R.id.fragment_main, disconnectedFragment);
		transaction.commit();
	}
	
	private void displayListView(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		
//		UIHelper.ToastMessage(this, listItem.size() + "");
		
		switch (listItem.size()) {
		case 1:{
			onePersonFragment = new OnePersonFragment(listItem.get(0));
			transaction.replace(R.id.fragment_main, onePersonFragment);
		}
			break;
		case 2:{
			towPersonsFragment = new TowPersonsFragment(listItem);
			transaction.replace(R.id.fragment_main, towPersonsFragment);
		}
			break;
		case 3:{
			threePersonsFragment = new ThreePersonsFragment(listItem);
			transaction.replace(R.id.fragment_main, threePersonsFragment);
		}
			break;
		case 4:{
			fourPersonsFragment = new FourPersonsFragment(listItem);
			transaction.replace(R.id.fragment_main, fourPersonsFragment);
		}
			break;

		default:{
			moreThanFourPersonsFragment = new MoreThanFourPersonsFragment(listItem);
			transaction.replace(R.id.fragment_main, moreThanFourPersonsFragment);
		}
			break;
		}
		
		transaction.commit();
		
	}
	
	private void downLoad(String keyWord){
		Bundle bundle = new Bundle();
		String[] keyArrays = keyWord.split(":");
		String str = keyArrays[2].replaceAll("\\s*", "");  //�����滻�󲿷ֿհ��ַ��� �����ڿո�   "\s"����ƥ��ո��Ʊ������ҳ���ȿհ��ַ�����������һ�� 
		long fileLength = Long.parseLong(str);
		
		bundle.putLong("fileLength", fileLength);
		startActivity(DownLoadDialog.class, bundle);
		
		AppManager.getAppManager().finishActivity(MainActivity.this);
	}
	
	private void updateAPK(String keyWord){
		Bundle bundle = new Bundle();
		String[] keyArrays = keyWord.split(":");
		long fileLength = Long.parseLong(keyArrays[2]);
		
		bundle.putLong("fileLength", fileLength);
		startActivity(UpdateAPKDialog.class, bundle);
		
		AppManager.getAppManager().finishActivity(MainActivity.this);
	}
	
	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis() - firstTime < 3000){
			onDestroy();
		}else {
			firstTime = System.currentTimeMillis();
			UIHelper.ToastMessage(this, R.string.application_exit);
		}
	}
	
	@Override
	protected void onDestroy() {
		SocketReceiveThread.isStop = true;
		try {
			SocketReceiveThread.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
		AppManager.getAppManager().finishActivity(MainActivity.this);
		
	}

	/**
	 * ��ʼ��������ͼ
	 * 
	 * @param view
	 * @return
	 */
	public View initPortpaitView() {
		return null;
	}
		
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
