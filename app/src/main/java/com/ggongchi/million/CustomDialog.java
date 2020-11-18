package com.ggongchi.million;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CustomDialog {
    private Context context;
    public CustomDialog(Context context)
    {
        this.context=context;
    }


    public void callFunction(){
        final Dialog dig=new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(R.layout.plus_web);
        dig.show();

        final Button et_cut_bt= dig.findViewById(R.id.et_cut);
        final Button et_calcu_bt=dig.findViewById(R.id.et_cut_calcu);
        final Button dec_calcu_bt=dig.findViewById(R.id.dec_calcu);

        et_cut_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/imas_ml_td"));
                context.startActivity(intent);
            }
        });

        et_calcu_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/Alneys_Al"));
                context.startActivity(intent);
            }
        });

        dec_calcu_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://megmeg.work/unit_optimiser/"));
                context.startActivity(intent);
            }
        });
    }
}
