package com.ggongchi.million;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    int app_type; // 0->일본 1->한국
    int e_type; //0->Theater 1->Tour
    int e_run; //0-> 영업 1-> 라이브
    int scroe;
    int t_scroe;
    int coin;
    int st;
    int max_st;

    int setting_bar;
    int run_bar;


    CountDownTimer CDT;
    TextView ins;
    Switch setting;

    Intent s_intent;

    Button btn_opt;
    EditText et_score;
    EditText et_t_scroe;
    EditText et_coin;





    int spend;
    int song_count;
    int event_count;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TAB 화면 구성
        final TabHost tabHost = findViewById(R.id.m_tabHost);


        tabHost.setup();
        tabHost.getTabWidget().setDividerDrawable(null);

        TabHost.TabSpec st_timer =tabHost.newTabSpec("Tab Spec 1");
        st_timer.setContent(R.id.tab1);
        st_timer.setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.st_click));
        ImageView iv_st=new ImageView(this);
        iv_st.setImageResource(R.drawable.tab);
        tabHost.addTab(st_timer);

        TabHost.TabSpec e_cal =tabHost.newTabSpec("Tab Spec 2");
        e_cal.setContent(R.id.tab2);
        e_cal.setIndicator("",getApplicationContext().getResources().getDrawable(R.drawable.ev));
        ImageView iv_ev=new ImageView(this);
        iv_ev.setImageResource(R.drawable.tab);
        tabHost.addTab(e_cal);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#00ff0000"));
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#00ff0000"));
        final int menu_off[]={R.drawable.st,R.drawable.ev};
        final int menu_on[]={R.drawable.st_click,R.drawable.ev_click};
        tabHost.getTabWidget().getChildAt(0).getLayoutParams().width=150;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().width=150;
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
                    ImageView iv=(ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.icon);
                    iv.setImageDrawable(getResources().getDrawable(menu_off[i]));

                }
                ImageView ip=(ImageView)tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(android.R.id.icon);
                ip.setImageDrawable(getResources().getDrawable(menu_on[tabHost.getCurrentTab()]));

            }
        });
        tabHost.setCurrentTab(0);





        //PLUS 버튼 구현
        Button plus_bt=findViewById(R.id.btn_web);
        plus_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog webDialog=new CustomDialog(MainActivity.this);
                webDialog.callFunction();
            }
        });




        //각종 데이터 저장
        SharedPreferences sharedPreferences=getSharedPreferences("sFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        e_type=sharedPreferences.getInt("TYPE",0);
        e_run=sharedPreferences.getInt("RUN",0);
        st=sharedPreferences.getInt("ST",-1);
        max_st=sharedPreferences.getInt("MAX",0);
        scroe=sharedPreferences.getInt("SCORE",-1);
        t_scroe=sharedPreferences.getInt("TSCORE",0);
        coin=sharedPreferences.getInt("COIN",0);
        setting_bar=sharedPreferences.getInt("SB",0);
        run_bar=sharedPreferences.getInt("RB",0);
        app_type=sharedPreferences.getInt("APPT",0);

        et_score=findViewById(R.id.et_now_score);
        et_t_scroe=findViewById(R.id.et_target_score);
        et_coin=findViewById(R.id.et_coin);

        if(scroe!=-1){
            et_score.setText(String.valueOf(scroe));
        }
        if(t_scroe!=0)
        {
          et_t_scroe.setText(String.valueOf(t_scroe));
        }
        if(coin!=0){
            et_coin.setText(String.valueOf(coin));
        }
        if(coin==-1)
            coin=0;


        //스위치들
        final TextView tv_type=findViewById(R.id.e_inst_tv);
        Switch type_switch = findViewById(R.id.e_type_switch);
        if(e_type==1) {
            type_switch.setChecked(true);
            tv_type.setText("이벤트 타입 :  Tour    ");
        }
        type_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                    {
                        tv_type.setText("이벤트 타입 :  Tour    ");
                        e_type=1;
                    }
                else
                {
                    tv_type.setText("이벤트 타입 : Theater");
                    e_type=0;
                }
            }
        });

        final TextView tv_run=findViewById(R.id.e_run_tv);
        Switch run_switch = findViewById(R.id.e_run_switch);
        if(e_run==1) {
            run_switch.setChecked(true);
            tv_run.setText("이벤트  런   : 라이브런!");
        }
        run_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    tv_run.setText("이벤트  런   : 라이브런!");
                    e_run=1;
                }
                else
                {
                    tv_run.setText("이벤트  런   : 영업런 !");
                    e_run=0;
                }
            }
        });
        // Tab2 Edit Text
         final EditText score_et=findViewById(R.id.et_now_score);
         final EditText target_et=findViewById(R.id.et_target_score);
         final EditText coin_et=findViewById(R.id.et_coin);

        if(scroe!=-1)
            score_et.setText(String.valueOf(scroe));
        if(t_scroe!=0)
            target_et.setText(String.valueOf(t_scroe));
        if(coin !=0)
            coin_et.setText(String.valueOf(coin));

        Button e_btn=findViewById(R.id.btn_calcu);
        e_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score_et.getText().length() !=0 )
                    scroe= Integer.parseInt(score_et.getText().toString());
                else
                    scroe=-1;

                if(target_et.getText().length() != 0)
                    t_scroe=Integer.parseInt(target_et.getText().toString());
                else
                    t_scroe=0;
                if(coin_et.getText().length()!=0)
                    coin=Integer.parseInt(coin_et.getText().toString());
                else
                    coin=0;
            }
        });






        //EditText
         setting=findViewById(R.id.bar_switch);
        final Switch work=findViewById(R.id.bar_work_switch);
        final EditText now_st=findViewById(R.id.now_st);
        final EditText m_st=findViewById(R.id.max_st);
        ins=findViewById(R.id.result_st);
        if(st!=-1)
            now_st.setText(String.valueOf(st));
        if(max_st!=0)
            m_st.setText(String.valueOf(max_st));

        Button st_bt = findViewById(R.id.btn_start);
        Button st_end=findViewById(R.id.btn_end);
        st_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //스테미나 계산

                if(now_st.getText().length() !=0 )
                    st= Integer.parseInt(now_st.getText().toString());
                else
                    st=-1;

                if(m_st.getText().length() != 0)
                    max_st=Integer.parseInt(m_st.getText().toString());
                else
                    max_st=0;

                if(st>max_st)
                    Toast.makeText(getApplicationContext(),"스테미나를 제대로 입력해주세요",Toast.LENGTH_LONG).show();
                // 최대체력 현 체력 입력 여부 확인
                if(now_st.getText().length()!=0 && m_st.getText().length()!=0 && max_st>st)
                {
                    if(max_st>240)
                        Toast.makeText(getApplicationContext(),"최대 스테미나는 240 입니다.",Toast.LENGTH_LONG).show();
                    else {
                        //타이머 시작
                        int tmp_time = (max_st - st) * 5;
                        if (tmp_time > 60)
                            ins.setText("약 " + tmp_time / 60 + "시간 " + tmp_time % 60 + "분 후 MAX");
                        else
                            ins.setText("약  " + tmp_time % 60 + "분 후 MAX");

                        st--;
                        CDT = new CountDownTimer((max_st - st) * 5 * 60000, 60000 * 5) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                st++;
                                int tmp_time = (max_st - st) * 5;
                                if (tmp_time > 60)
                                    ins.setText("약 " + tmp_time / 60 + "시간 " + tmp_time % 60 + "분 후 MAX");
                                else
                                    ins.setText("약  " + tmp_time % 60 + "분 후 MAX");
                                now_st.setText(String.valueOf(st));
                            }

                            @Override
                            public void onFinish() {
                                ins.setText("충전 완료!");
                            }

                        };
                        CDT.start();


                    }
                }
        }});


        //계산 종료

        st_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CDT != null)
                {
                    CDT.cancel();
                }
                setting.setChecked(false);
                s_intent = new Intent(getApplicationContext(), MTimerService.class);
                stopService(s_intent);
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });

        //상태바 상태 확인중
        if(setting_bar ==1)
            setting.setChecked(true);
        if(run_bar == 1)
            work.setChecked(true);


        setting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                    setting_bar=1;
                else
                    setting_bar=0;

            }
        });

        work.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    if(getPackageList())
                        run_bar=1;

                    else
                    {
                        Toast.makeText(getApplicationContext(),"밀리시타가 설치 안 되어있어요",Toast.LENGTH_LONG).show();
                        run_bar=0;
                        work.setChecked(false);
                    }
                    //com.bandainamcoent.imas_millionlive_theaterdats
                }
                else
                        run_bar=0;
            }
        });


        //애니메이션
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        ImageView hand=findViewById(R.id.hand);
        hand.startAnimation(animation);



        //국가설정
        btn_opt=findViewById(R.id.btn_option);
        final String[] apps={"일본 밀리시타","한국 밀리시타"};
         btn_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("앱의 국가 설정");
                dlg.setSingleChoiceItems(apps, app_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        app_type=which;
                    }
                });
                dlg.setPositiveButton("결정",null);
                dlg.create();
                dlg.show();
            }
        });


         //점수 계산
        final Button btn_calcu=findViewById(R.id.btn_calcu);
        btn_calcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(coin_et.length()==0 || Integer.parseInt(coin_et.getText().toString())<0)
                {
                    Toast.makeText(getApplicationContext(),"재화를 다시 입력해주세요 ",Toast.LENGTH_LONG).show();
                }
                else if(et_score==null)
                {
                    Toast.makeText(getApplicationContext(),"버튼",Toast.LENGTH_LONG).show();
                }
                else if(scroe<t_scroe){

                    scroe=Integer.parseInt(et_score.getText().toString());
                    t_scroe=Integer.parseInt(et_t_scroe.getText().toString());
                    coin=Integer.parseInt(et_coin.getText().toString());
                    max_st=Integer.parseInt(m_st.getText().toString());
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("계산 완료");


                    Calcuate_score();
                    if(e_run==0 && e_type==1)
                        builder.setMessage("소모 주얼 : "+spend+"\n이벤트 곡 횟수 (3배수 기준) :"+event_count/3+"\n영업 횟수 (2배수 기준) : "+song_count);
                    else if(e_run==0)
                        builder.setMessage("소모 주얼 : "+spend+"\n이벤트 곡 횟수 (3배수 기준) :"+event_count/3+"\n일반 곡 횟수 (2배수 기준) : "+song_count);
                    else
                        builder.setMessage("소모 주얼 : "+spend+"\n이벤트 곡 횟수 (4배수 기준) : "+event_count+"\n일반 곡 횟수 (2배수 기준) : "+song_count);

                    builder.create();
                    builder.show();


                    }

                 else
                 {

                     Toast.makeText(getApplicationContext(),"목표 점수를 다시 입력해주세요",Toast.LENGTH_LONG).show();

                    }




            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();


        EditText et_score=findViewById(R.id.et_now_score);
        EditText et_t_scroe=findViewById(R.id.et_target_score);
        EditText et_coin=findViewById(R.id.et_coin);
        EditText et_st=findViewById(R.id.now_st);
        EditText et_mst=findViewById(R.id.max_st);

        if(et_st.getText().length()!=0)
            st=Integer.parseInt(et_st.getText().toString());
        if(et_mst.getText().length()!=0)
            max_st=Integer.parseInt(et_mst.getText().toString());


        if(et_score.getText().length()!=0)
            scroe=Integer.parseInt(et_score.getText().toString());
        if(et_t_scroe.getText().length()!=0)
         t_scroe=Integer.parseInt(et_t_scroe.getText().toString());
        if(et_coin.getText().length()!=0)
            coin=Integer.parseInt(et_coin.getText().toString());

        //저장
        SharedPreferences sharedPreferences=getSharedPreferences("sFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("TYPE",e_type);
        editor.putInt("RUN",e_run);
        editor.putInt("ST",st);
        editor.putInt("MAX",max_st);
        editor.putInt("SCORE",scroe);
        editor.putInt("TSCORE",t_scroe);
        editor.putInt("COIN",coin);
        editor.putInt("SB",setting_bar);
        editor.putInt("RB",run_bar);
        editor.putInt("APPT",app_type);
        editor.commit();

        //상태바 허용
        if(setting.isChecked()==true) {
                s_intent = new Intent(MainActivity.this, MTimerService.class);
                startService(s_intent);
        }
        else {
            s_intent = new Intent(MainActivity.this, MTimerService.class);
            stopService(s_intent);
        }
    }




    @Override
    public  void onResume() {
        super.onResume();

        s_intent = new Intent(getApplicationContext(), MTimerService.class);
        if (stopService(s_intent)) {


            ins = findViewById(R.id.result_st);
            SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
            st = sharedPreferences.getInt("ST", -1);
            Log.i("tag","onResume");
            //  Toast.makeText(getApplicationContext(),st,Toast.LENGTH_LONG).show();
            EditText now_st = findViewById(R.id.now_st);
            now_st.setText(String.valueOf(st));
            max_st = sharedPreferences.getInt("MAX", 0);
            if (st != -1) {
                if (st == max_st)
                    ins.setText("충전이 완료되었습니다.");
                else {
                    int remain = (max_st - st) * 5;
                    if (remain > 60)
                        ins.setText("약 " + remain / 60 + "시간 " + remain % 60 + "분 후 MAX");
                    else
                        ins.setText("약  " + remain % 60 + "분 후 MAX");

                }
            }
        }
    }

    public boolean getPackageList(){
        boolean isExist=false;
        String app_name="";

        if(app_type==0)
            app_name="com.bandainamcoent.imas_millionlive_theaterdays";
        else
            app_name="com.bandainamcoent.imas_millionlive_theaterdays_kr";
        PackageManager packageManager=getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent=new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps=packageManager.queryIntentActivities(mainIntent,0);

        try{
            for(int i=0;i<mApps.size();i++){
                if(mApps.get(i).activityInfo.packageName.startsWith(app_name)){
                    isExist=true;
                    break;
                }
            }
        }catch (Exception e){
            isExist=false;
        }

        return isExist;
    }

    public void Calcuate_score() {
        int getScore = 0;
        int getCoin = coin;

        spend = 0;
        song_count = 0;
        event_count = 0;






        if (e_type == 0) {//Theater
                if (e_run == 1)//Live run
                {
                    while (t_scroe > getScore + scroe) {
                        int count = max_st / 60;
                        getCoin = getCoin + count * 190;
                        song_count = song_count + count;
                        getScore = getScore + count * 190;

                        while (getCoin > 720) {
                            getCoin = getCoin - 720;
                            getScore = getScore + 2148;
                            event_count++;
                        }
                        spend = spend + 50;
                    }
                } else {
                    //oshigoto run
                    while (t_scroe > getScore + scroe) {
                        int count = max_st / 300;
                        Log.e("MainActivity",String.valueOf(count));

                        int i=0;
                        while(count<=0){
                            count=(max_st*i)/300;
                            spend=spend+50;
                            i++;

                        }
                        getCoin = getCoin + count * 595;
                        song_count = song_count + count;
                        getScore = getScore + count * 595;

                        while (getCoin > 720) {
                            getCoin = getCoin - 720;
                            getScore = getScore + 2148;
                            event_count++;
                        }

                    }


                }

            } else {
                //Tour
                getCoin=coin*20;

                if (e_run == 1)
                    while (t_scroe >= (getScore + scroe)) {
                        int count = max_st / 60;

                        getCoin = getCoin + (count * 6);
                        song_count = song_count + count;
                        getScore = getScore + 280 * count;
                        while (getCoin > 20) {
                            getCoin = getCoin - 20;
                            getScore = getScore + 720;
                            event_count++;
                        }
                        spend = spend + 50;
                    }
                else {
                    while (t_scroe >= (getScore + scroe)) {
                        int count = max_st / 60;
                        getCoin = getCoin + (count * 6);
                        song_count = song_count + count;
                        getScore = getScore + 60 * count;
                        while (getCoin > 20) {
                            getCoin = getCoin - 20;
                            getScore = getScore + 720;
                            event_count++;
                        }
                        spend = spend + 50;
                    }
                }

            }
        }
}