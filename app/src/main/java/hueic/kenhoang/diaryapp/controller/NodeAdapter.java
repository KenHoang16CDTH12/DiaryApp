package hueic.kenhoang.diaryapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import hueic.kenhoang.diaryapp.R;
import hueic.kenhoang.diaryapp.model.NoteModel;
import hueic.kenhoang.diaryapp.util.Config;
import hueic.kenhoang.diaryapp.util.Util;

/**
 * Created by kenhoang on 3/29/17.
 */

public class NodeAdapter extends BaseAdapter{
    private String[] arrColor = {"#CDDC39", "#e91e63", "#CDDC39", "#e91e63",
                                "#8e24aa", "#9c27b0", "#f3e5f5", "#f48fb1",
                                "#90caf9", "#0d47a1", "#6200ea", "#0097a7",
                                "#004d40", "#00e5ff", "#cddc39", "#4caf50"};
    private Context context;
    private ArrayList<NoteModel> arr;
    private Random random;

    public NodeAdapter(Context context, ArrayList<NoteModel> arr) {
        this.context = context;
        this.arr = arr;
        random = new Random();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)arr.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View rowView = inflater.inflate(R.layout.note_layout, parent, false);
        //findview
        LinearLayout layoutMark = (LinearLayout) rowView.findViewById(R.id.linearLayoutMark);
        TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) rowView.findViewById(R.id.tvContent);
        ImageView imgAttach = (ImageView) rowView.findViewById(R.id.imgAttach1);
        TextView tvDay = (TextView) rowView.findViewById(R.id.tvDay);
        TextView tvMonth = (TextView) rowView.findViewById(R.id.tvMonth);
        TextView tvTime = (TextView) rowView.findViewById(R.id.tvTime);
        // update value
        layoutMark.setBackgroundColor(Color.parseColor(arrColor[random.nextInt(arrColor.length)]));

        NoteModel noteModel = arr.get(position);
        tvTitle.setText(noteModel.title);
        tvContent.setText(noteModel.content);
        //set datetime
        Date date = Util.converStringToDate(noteModel.datetime);
        String dateOfWeek = (String) DateFormat.format("EEEE", date); //Thursday
        String stringMonth = (String) DateFormat.format("MMM", date);//Jun
        String day = (String) DateFormat.format("dd", date);//20
        String time = (String) DateFormat.format("hh:mm", date);
        tvDay.setText(dateOfWeek);
        tvMonth.setText(day + " " + stringMonth);
        tvTime.setText(time);
        //set image
        Util.setBitmapToImage(context, Config.FOLDER_IMAGES, noteModel.image, imgAttach);
        return rowView;
    }
}
