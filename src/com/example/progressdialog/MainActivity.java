package com.example.progressdialog;

import java.lang.Thread.UncaughtExceptionHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    
    TextView label = null;
    AsyncTaskProgressDialogSimple task = null;
    String taskStatus = null;
    
    private static final int TASK_COUNT = 10;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        	@Override
        	public void uncaughtException(Thread thread, Throwable ex) {
                Log.d("test", ex.getLocalizedMessage());
        	}
        });
 
        label = (TextView)findViewById(R.id.textView1);
        findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("�^�C�g��");
                progressDialog.setMessage("���b�Z�[�W");
                progressDialog.setMax(TASK_COUNT);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                
                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // ProgressDialog ���L�����Z��
                                Log.d("test", "BUTTON_CANCEL clicked");
                            	taskStatus = "���f";
                                task.cancel(true);
                            }
                        });
                
                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                    	
                    	//--------------------------------------------------
                    	// ���̃��\�b�h�́A�A�A
                    	//
                    	//  �Etask���I�������ꍇ
                    	//  �E�L�����Z�����ꂽ�ꍇ
                    	//  �EActivity���j�����ꂽ�ꍇ�i��ʉ�]�ȂǂŁj
                    	//
                    	// �ɌĂ΂�邱�ƂɂȂ�
                    	//--------------------------------------------------
                    	
                        Log.d("test", taskStatus);
                    	label.setText(taskStatus);
                    }
                });
                
                Log.d("test", "�J�n");
            	label.setText("�J�n");
                task = new AsyncTaskProgressDialogSimple();
                task.execute();
                
                /*
                thread = new Thread(MainActivity.this);
                mHandler = new ExampleHandler(MainActivity.this);
                thread.start();
                */
        	}
        });
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        this.closeDialog();
    }

    private void closeDialog() {
    	if (task == null) {
    		return;
    	}

    	synchronized(task) {
            Log.d("test", "closing...");
			if (progressDialog != null) {
            	progressDialog.dismiss();
            	progressDialog = null;

            	/*
            	// �����ł��Ă��邩�̊m�F�p�ɏ���sleep���Ă݂�
        		try {
        			Thread.sleep(1000);
        		} catch(Exception ex) {
        		}*/
        		
                Log.d("test", "...closed");
            }
    	}
    }

    
    public class AsyncTaskProgressDialogSimple extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	
        	taskStatus = "���s��";
            progressDialog.show();
        }
        
        @Override
        protected String doInBackground(Void... params) {
        	for(int i = 0; i < TASK_COUNT; i++) {
        		
        		if (this.isCancelled()) {
                    Log.d("test", "cancel...");
        			break;
        		}
        		
        		try {
        			Thread.sleep(500);
            		publishProgress(i);
        		} catch(Exception ex) {
        		}
        	}
			return "�I��";
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
        	super.onProgressUpdate(params);

    		synchronized(this) {
                Log.d("test", "updating...");
    			if (progressDialog != null) {
    	        	progressDialog.setMessage("�����F" + params[0]);
    	        	progressDialog.incrementProgressBy(1);
    	            Log.d("test", "...updated");
            	}
        	}
        }

        @Override
        protected void onPostExecute(String result) {
        	
        	//--------------------------------------------------
        	// task��cancel���ꂽ�炱�̃��\�b�h�͌Ă΂�Ȃ�
        	//--------------------------------------------------
        	
        	super.onPostExecute(result);
        	
            Log.d("test", result);
        	taskStatus = result;

        	closeDialog();
        }
        
    }

/*
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO �����������ꂽ catch �u���b�N
            e.printStackTrace();
        }
        if (progressDialog != null && progressDialog.isIndeterminate()) {
        	progressDialog.dismiss();
        }
        mHandler.sendEmptyMessage(0);
    }
 
    private static class ExampleHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        ExampleHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
        	Activity activity = mActivity.get();
        	if (activity != null) {
	            Toast.makeText(activity.getApplicationContext(), "slept 5 seconds",
	                    Toast.LENGTH_LONG).show();
        	}
        }
    };
    private ExampleHandler mHandler;
*/
}
