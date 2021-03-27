package com.ivory.isasa.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.ivory.isasa.R;
import com.ivory.isasa.databinding.LoginBinding;
import com.ivory.isasa.entity.Emp;
import com.ivory.isasa.model.LinkEnum;
import com.ivory.isasa.model.ResultInfo;
import com.ivory.isasa.util.JsonUtil;
import com.ivory.isasa.util.RequestUtil;
import com.ivory.isasa.util.SharedPreferencesUtil;
import com.leaf.library.StatusBarUtil;
import org.apache.catalina.util.ParameterMap;

import es.dmoral.toasty.Toasty;


/**
 * describe: 登陆界面
 *
 * @Author: Aaron
 * @Date: 2021/3/16 12:14
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private LoginBinding loginBinding;

    private EditText et_account;
    private EditText et_pwd;
    private Button btn_login;

    private final Handler handler=new Handler(Looper.getMainLooper(),(msg)->{
        if (msg.what==1){
            ResultInfo resultInfo= (ResultInfo) msg.obj;
            if (!resultInfo.getCode().equals(200)){
                Toasty.error(this,resultInfo.getMsg()).show();
                return false;
            }
            getEmpInfo();
        }
        else if (msg.what==2){
            Emp emp= (Emp) msg.obj;
            if (null==emp){
                Toasty.error(this,"获取用户信息失败,请尝试重新登陆!").show();
                return false;
            }
            //保存用户信息
            SharedPreferencesUtil.putInt(this,"id", emp.getId());
            SharedPreferencesUtil.putString(this,"empName", emp.getEmpName());
            SharedPreferencesUtil.putString(this,"empPwd", emp.getEmpPwd());
            SharedPreferencesUtil.putInt(this,"shopId", emp.getShopId());
            SharedPreferencesUtil.putString(this,"empPhone",emp.getEmpPhone());
            SharedPreferencesUtil.putString(this,"tenantCrop",emp.getTenantCrop());
            SharedPreferencesUtil.putBoolean(this,"loginStatus", true);
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    });

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = LoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        StatusBarUtil.setColor(this, getColor(R.color.login));

        setControllerDate();
    }

    private void setControllerDate(){
        et_account=loginBinding.account;
        et_pwd=loginBinding.pwd;
        btn_login=loginBinding.login;

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId=v.getId();
        if (vId==btn_login.getId()){
            doLogin();
        }
    }

    /**
     * describe: 登陆
     *
     * @Author: Aaron
     * @Date: 2021/3/16 14:25
     */
    public void doLogin(){
        new Thread(()->{
            ParameterMap<String,Object> parameterMap=new ParameterMap<>();
            parameterMap.put("empPhone",et_account.getText());
            parameterMap.put("empPwd",et_pwd.getText());
            String  result = RequestUtil.requestGet(LinkEnum.INTERFACE_LINK.getLink()+"/login/mobile-doLogin",parameterMap);
            Message message=new Message();
            message.what=1;
            message.obj=JsonUtil.jsonStr2Object(result, ResultInfo.class);
            handler.sendMessage(message);
        }).start();
    }

    /**
     * describe: 获取用户信息
     *
     * @Author: Aaron
     * @Date: 2021/3/16 14:30
     */
    public void getEmpInfo(){
        new Thread(()->{
            ParameterMap<String,Object> parameterMap=new ParameterMap<>();
            parameterMap.put("phone",et_account.getText());
            String  result = RequestUtil.requestGet(LinkEnum.INTERFACE_LINK.getLink()+"/emp/getEmpDetails",parameterMap);
            Message message=new Message();
            message.what=2;
            message.obj=JsonUtil.jsonStr2Object(result, Emp.class);
            handler.sendMessage(message);
        }).start();
    }

    /**
     * describe: 禁止系统级滑动
     * 防止 回到登陆前页面
     * @Author: Aaron
     * @Date: 2021/3/17 12:31
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return  false;
    }
}


