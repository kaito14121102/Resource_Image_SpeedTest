package com.gdt.speedtest.features.main.fragment.result;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.gdt.speedtest.database.Result;
import com.gdt.speedtest.features.detailhistory.DetailHistoryActivity;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String WIFI="true";
    private static final String NOT_WIFI="false";
    private Context context;
    private List<Result> results;
    private int ITEM_TYPE = 1;

    ResultAdapter(Context context, List<Result> results) {
        this.context = context;
        this.results = results;
        results.add(new Result());
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ResultHolder) {
            ((ResultHolder) holder).onBind(results.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return results.size()-1;
    }


    class ResultHolder extends RecyclerView.ViewHolder {
        private Result result;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_ping_rate)
        TextView txtPingRate;
        @BindView(R.id.txt_download_rate)
        TextView txtDownloadRate;
        @BindView(R.id.txt_upload_rate)
        TextView txtUploadRate;
        @BindView(R.id.txt_network_name)
        TextView txtNetworkName;
        @BindView(R.id.txt_hour)
        TextView txtHour;
        @BindView(R.id.img_type_network)
        ImageView imgTypeNetwork;
        @BindView(R.id.root)
        LinearLayout root;

        ResultHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            txtNetworkName.setMaxLines(1);
            txtNetworkName.setEllipsize(TextUtils.TruncateAt.END);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Intent intent = new Intent(context, DetailHistoryActivity.class);
                    intent.putExtra("RESULT", json);
                    context.startActivity(intent);
                }
            });
        }

        void onBind(Result result) {
            this.result = result;
            try {
                DecimalFormat dec = new DecimalFormat(Constants.FORMAT);
                String pingRate = dec.format(result.getPing());
                String downloadRate = dec.format(result.getDownload());
                String uploadRate = dec.format(result.getUpload());
                txtPingRate.setText(pingRate);
                txtDownloadRate.setText(downloadRate);
                txtUploadRate.setText(uploadRate);
                String[] splitName = result.getName().split("-");
                if (splitName.length>0) {
                    txtNetworkName.setText(splitName[0]);
                }

                //split time
                String time = result.getTime();
                String[] split = time.split(" ");

                txtTime.setText(split[0]);
                txtHour.setText(split[1]);

                if (result.getTypeNetwork().equals(WIFI)) {
                    imgTypeNetwork.setBackgroundResource(R.drawable.ic_wifi_history);
                }else{
                    imgTypeNetwork.setBackgroundResource(R.drawable.ic_name_internet);
                }
            } catch (Exception e) {
            }
        }
    }
}
