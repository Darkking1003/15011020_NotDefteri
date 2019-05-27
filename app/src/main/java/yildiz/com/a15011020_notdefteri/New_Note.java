package yildiz.com.a15011020_notdefteri;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class New_Note extends AppCompatActivity {
    Button Kaydet;
    Context context=this;
    InputMethodManager imm;
    List<String> Attachments=new ArrayList<String>();
    List<String> Filenames=new ArrayList<String>();
    final int PICKFILE_REQUEST_CODE=1,FileChanged=5;
    boolean SameDay=false;
    final static String RECORDS_FILENAME="Notes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__note);

        Toolbar myToolBar=(Toolbar) findViewById(R.id.ViewNoteToolbar);
        myToolBar.setTitle("");
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.Attachments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewFiles.class);
                Note note=new Note("File","Empty",Filenames,Attachments,"Now","Now","Yüksek Öncelik");
                intent.putExtra("Note",note);
                startActivityForResult(intent,FileChanged);
            }
        });

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Kaydet=(Button) findViewById(R.id.Kaydet);
        Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nDialog=new Dialog(context);

                nDialog.setContentView(R.layout.newnote_popup);
                nDialog.show();

                nDialog.findViewById(R.id.Kapat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       nDialog.cancel();
                    }
                });



                nDialog.findViewById(R.id.Kaydet2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Not=((TextView)findViewById(R.id.ViewNot)).getText().toString();
                        String Baslik=((TextView)findViewById(R.id.View_Title)).getText().toString();
                        String Date=FindDate();
                        String Hour=FindHour();
                        String Oncelik=((Spinner)nDialog.findViewById(R.id.Oncelik)).getSelectedItem().toString();
                        Note note=new Note(Baslik,Not,Filenames,Attachments,Date,Hour,Oncelik);
                        String RDate=((TextView)nDialog.findViewById(R.id.Tarih)).getText().toString();
                        String RTime=((TextView)nDialog.findViewById(R.id.Saat)).getText().toString();
                        //Date ve Hour şimdiki zaman bakıyor kullanıcının girdiği bilgiyi daha almadım
                        if((RDate.length()!=0)&&(RTime.length()!=0)){
                            Toast.makeText(context,"Alarm Oluşturuldu "+RDate+" "+RTime,Toast.LENGTH_SHORT).show();
                            note.setReminTimes(RDate,RTime);
                            SetupAlarm(note);
                        }


                        Saveinternal(note);
                        finish();
                    }
                });

                addItemsOnSpinner2(nDialog);


                ((TextView)nDialog.findViewById(R.id.Tarih)).setInputType(InputType.TYPE_NULL);
                ((TextView)nDialog.findViewById(R.id.Saat)).setInputType(InputType.TYPE_NULL);

                ((TextView)nDialog.findViewById(R.id.Tarih)).setFocusable(true);
                ((TextView)nDialog.findViewById(R.id.Tarih)).setFocusableInTouchMode(true);
                ((TextView)nDialog.findViewById(R.id.Saat)).setFocusable(true);
                ((TextView)nDialog.findViewById(R.id.Saat)).setFocusableInTouchMode(true);
                nDialog.findViewById(R.id.Tarih).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view,boolean b) {
                        Log.d("Deneme", "onFocusChange: a");
                        if (b) {
                            final Calendar takvim = Calendar.getInstance();
                            final int yil = takvim.get(Calendar.YEAR);
                            final int ay = takvim.get(Calendar.MONTH);
                            final int gun = takvim.get(Calendar.DAY_OF_MONTH);
                            Log.d("Deneme", "onFocusChange: ");
                            nDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            DatePickerDialog dpd = new DatePickerDialog(context,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            month += 1;

                                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                            if (year < yil) {
                                                Toast.makeText(context, "Sadece ilerki zamanlar icin hatırlatma koyabilirsiniz yil", Toast.LENGTH_SHORT).show();
                                            } else if ((year==yil)&&(month < ay + 1)) {
                                                Toast.makeText(context, "Sadece ilerki zamanlar icin hatırlatma koyabilirsiniz ay", Toast.LENGTH_SHORT).show();
                                            } else if ((month==ay+1)&&(dayOfMonth < gun)) {
                                                Toast.makeText(context, "Sadece ilerki zamanlar icin hatırlatma koyabilirsiniz gun", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ((TextView) nDialog.findViewById(R.id.Tarih)).setText(dayOfMonth + "/" + month + "/" + year);
                                            }
                                            if((year==yil)&&(dayOfMonth==gun)&&(month==ay+1)){
                                                SameDay=true;
                                            }else{
                                                SameDay=false;
                                            }

                                        }
                                    }, yil, ay, gun);

                            dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                            dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                            dpd.show();
                            nDialog.findViewById(R.id.Tarih).clearFocus();
                        }
                    }
                });

                nDialog.findViewById(R.id.Saat).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v,boolean b) {
                        if (b){
                            // TODO Auto-generated method stub
                            Calendar mcurrentTime = Calendar.getInstance();
                        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        final int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    ((TextView) nDialog.findViewById(R.id.Saat)).setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                            nDialog.findViewById(R.id.Saat).clearFocus();

                    }

                    }
                });



            }
        });
    }



    private String FindDate(){
        final Calendar takvim = Calendar.getInstance();
        final int yil = takvim.get(Calendar.YEAR);
        final int ay = takvim.get(Calendar.MONTH)+1;
        final int gun = takvim.get(Calendar.DAY_OF_MONTH);
        return gun+"/"+ay+"/"+yil;
    }

    private String FindHour(){
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        final int second =mcurrentTime.get(Calendar.SECOND);
        return hour+":"+minute+":"+second;
    }



    private void Saveinternal(Note note){
        File file =context.getFileStreamPath(RECORDS_FILENAME);
        ArrayList<Note> Notes=null;
        if (file.exists()){
            Notes=ReadSaved();

        }

        if(Notes==null){
            Notes=new ArrayList<Note>();
        }

        Notes.add(note);
        WriteNotes(Notes);
    }
    public void WriteNotes(ArrayList<Note> notes){
        FileOutputStream fos;
        ObjectOutputStream oos=null;
        try{
            fos = getApplicationContext().openFileOutput(RECORDS_FILENAME, Context.MODE_PRIVATE);
            Toast.makeText(context,RECORDS_FILENAME,Toast.LENGTH_SHORT).show();
            oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();

        }catch(Exception e){

            Log.e("Write", "Cant save records"+e.getMessage());
        }
        finally{
            if(oos!=null)
                try{
                    oos.close();
                }catch(Exception e){
                    Log.e("WRite", "Error while closing stream "+e.getMessage());
                }
        }
    }

    public ArrayList<Note> ReadSaved(){
        FileInputStream fin;
        ObjectInputStream ois=null;
        try{
            fin = getApplicationContext().openFileInput(RECORDS_FILENAME);
            ois = new ObjectInputStream(fin);
            ArrayList<Note> records = (ArrayList<Note>) ois.readObject();
            ois.close();

            return records;
        }catch(Exception e){
            Log.e("Reading", "Cant read saved records"+e.getMessage());
            return null;
        }
        finally{
            if(ois!=null)
                try{
                    ois.close();
                }catch(Exception e){
                    Log.e("Reading", "Error in closing stream while reading records"+e.getMessage());
                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newnotebook_menu,menu);



        return true;
    }

    public void addItemsOnSpinner2(Dialog nDialog) {

        Spinner spinner2 = (Spinner) nDialog.findViewById(R.id.Oncelik);
        List<String> list = new ArrayList<String>();
        list.add("Yüksek Öncelik");
        list.add("Orta Öncelik");
        list.add("Az Öncelik");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    private void SetupAlarm(Note note){
        AlarmManager am=(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReciever.class);
        intent.putExtra("Title",note.Title);
        Log.d("Deneme", "SetupAlarm: "+note.RemindDate+" "+Integer.toString(note.id));
        intent.putExtra("Priority",note.Oncelik);
        PendingIntent pi=PendingIntent.getBroadcast(this,note.id,intent,0);


        String input=note.RemindDate+":"+note.RemindHour;
        Long time=0l;
        try {
            Date RDate = new SimpleDateFormat("dd/MM/yyyy:hh:mm").parse(input);
            Log.d("Deneme", "SetupAlarm: "+RDate.toString());
            time=RDate.getTime();
        }catch (ParseException ex){
            Log.d("Deneme", "SetupAlarm: "+ex.getMessage());
        }
//        Date RDate=new Date(Integer.parseInt(dates[2]),Integer.parseInt(dates[1]),Integer.parseInt(dates[0]),Integer.parseInt(Hours[0]),Integer.parseInt(Hours[1]));


        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pi);
    }


    public void CloseKeyboard(View arg0){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("Deneme", "CloseKeyboard: ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_add_file) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        }else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICKFILE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                Attachments.add(uri.toString());
                String Filename=ReturnFileName(uri);
                Filenames.add(Filename);
                SetFilenames();

            }
        }else if(requestCode==FileChanged){
            if(resultCode==RESULT_OK){
                Note nNote=(Note)data.getSerializableExtra("Note");

                if(Filenames.isEmpty()==false){
                    Filenames.clear();
                    Attachments.clear();
                    if(nNote.FileNames.isEmpty()==false){
                        Filenames.addAll(nNote.FileNames);
                        Attachments.addAll(nNote.FilePaths);
                    }
                    SetFilenames();
                }
            }
        }

    }
    private void SetFilenames(){
        TextView attach=(TextView) findViewById(R.id.Attachments);
        Iterator iterator = Filenames.iterator();
        String str=new String();
        while(iterator.hasNext()){
            str=str.concat(iterator.next().toString());
            str=str.concat(";;");
        }
        attach.setText(str);
    }
    private String ReturnFileName(Uri uri){
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = context.getContentResolver();
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        String path=null;
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    path = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return path;
    }

}
