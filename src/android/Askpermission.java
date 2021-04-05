package de.mopsdom.askpermission;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class Askpermission extends CordovaPlugin {

    private final String pluginName = "cordova-plugin-askpermission";
    private CallbackContext callbackContext;
    private final static int REQUEST_CODE = 11010;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("cordova-plugin-askpermission","onActivityResult");

        if(REQUEST_CODE==requestCode)
        {
            Log.e("cordova-plugin-askpermission","HIER IS NIX ZU TUN!");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Deprecated
	public void CheckPermissions(String[] permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            boolean alltrue = true;
            for (String permission : permissions)
            {
                if (ContextCompat.checkSelfPermission(this.cordova.getActivity(), permission)!=PackageManager.PERMISSION_GRANTED)
                {
                    alltrue=false;
                    break;
                }
            }


            if (alltrue) {
                PluginResult presult = new PluginResult(PluginResult.Status.OK);
                presult.setKeepCallback(true);
                callbackContext.sendPluginResult(presult);
            } else {
                ActivityCompat.requestPermissions(this.cordova.getActivity(), permissions, REQUEST_CODE);
                //result im callback sendne
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            PluginResult presult = new PluginResult(PluginResult.Status.OK);
            presult.setKeepCallback(true);
            callbackContext.sendPluginResult(presult);
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        Log.e("cordova-plugin-askpermissions","onRequestPermissionResult");
        switch (requestCode) {
            case REQUEST_CODE:

                boolean alltrue = true;
                for (int n=0;n<grantResults.length;n++)
                {
                    Log.e("cordova-plugin-askpermissions",permissions[n]+(String.valueOf(grantResults[n]==PackageManager.PERMISSION_GRANTED)));
                    if (grantResults[n]!=PackageManager.PERMISSION_GRANTED)
                    {
                        alltrue=false;
                        break;
                    }
                }
                PluginResult presult = null;
                if (alltrue)
                {
                    presult=new PluginResult(PluginResult.Status.OK);
                }
                else
                {
                    presult=new PluginResult(PluginResult.Status.ERROR);
                }
                presult.setKeepCallback(true);
                callbackContext.sendPluginResult(presult);
                break;
        }
    }

    @Override
    public boolean execute(final String action, final JSONArray data, final CallbackContext callbackContext) {

        if (action.equals("request")) {
            this.callbackContext=callbackContext;
            try
            {
                JSONObject params = data!=null&&data.length()>0&&data.getString(0)!=null?new JSONObject(data.getString(0)):null;
                if (params!=null) {

                    Iterator<String> keys = params.keys();
                    ArrayList<String> permissionlist = new ArrayList<>();
                    while(keys.hasNext()) {
                        String key = keys.next();

                        if (key.equals("permissions"))
                        {
                            JSONArray arr = params.getJSONArray("permissions");
                            for (int k=0;k<arr.length();k++)
                            {
                                Log.e("cordova-plugin-askpermissions",arr.getString(k));
                                permissionlist.add(arr.getString(k));
                            }
                        }
                    }

                    if (permissionlist.size()==0)
                    {
                        Log.e("cordova-plugin-askpermissions","PERMISSION LIST = 0");
                        PluginResult presult=new PluginResult(PluginResult.Status.ERROR);
                        presult.setKeepCallback(true);
                        callbackContext.sendPluginResult(presult);
                    }
                    else
                    {
                        cordova.setActivityResultCallback(this);
                        cordova.requestPermissions(this,REQUEST_CODE,permissionlist.toArray(new String[permissionlist.size()]));
                    }
                }

            } catch (Throwable e) {
                Log.e("cordova-plugin-askpermissions",e.getMessage(),e);
                callbackContext.error(e.getMessage());
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
