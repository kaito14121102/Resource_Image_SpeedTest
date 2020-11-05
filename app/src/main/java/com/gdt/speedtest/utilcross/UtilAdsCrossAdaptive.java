package com.gdt.speedtest.utilcross;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.gdt.speedtest.data.model.ItemCross;

import java.util.ArrayList;
import java.util.Random;

import static com.gdt.speedtest.Constants.PACKAGE_APPLOCK;
import static com.gdt.speedtest.Constants.PACKAGE_WATERMARK_PHOTO;
import static com.gdt.speedtest.Constants.TYPE_IMAGE;

public class UtilAdsCrossAdaptive {


    public static View getLayoutCross(Context context, ArrayList<ItemCross> listCross) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads_cross_adaptive, null);
        ItemCross itemCross = listCross.get(new Random().nextInt(listCross.size()));
        ImageView imgIcon = view.findViewById(R.id.img_icon_app);
        TextView txtName = view.findViewById(R.id.txt_name_app);
        TextView txtDescription = view.findViewById(R.id.txt_description);
        TextView txtInstall = view.findViewById(R.id.btn_install);
        ImageView imgAdchoice = view.findViewById(R.id.img_adchoice);
        RelativeLayout layoutAdsAdaptive=view.findViewById(R.id.layout_ads_adaptive);

        txtDescription.setMaxLines(2);
        txtDescription.setEllipsize(TextUtils.TruncateAt.END);

        txtName.setText(itemCross.name_app);
        txtDescription.setText(itemCross.description_app);
        if (itemCross.package_name.equals(PACKAGE_WATERMARK_PHOTO)) {
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/watermark/ic_icon_watermark" + TYPE_IMAGE))
                    .into(imgIcon);
        }else if (itemCross.package_name.equals(PACKAGE_APPLOCK)) {
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/applock/ic_icon_applock" + TYPE_IMAGE))
                    .into(imgIcon);
        }
        layoutAdsAdaptive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + itemCross.package_name));
                context.startActivity(intentUpdate);
            }
        });
        imgAdchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRate = new Intent(Intent.ACTION_VIEW);
                intentRate.setData(Uri.parse(Constants.URL_INHOUSE_ADS));
                context.startActivity(intentRate);
            }
        });

        return view;
    }
}
