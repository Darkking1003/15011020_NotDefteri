package yildiz.com.a15011020_notdefteri;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class View_Note extends AppCompatActivity {
    private final int PICKFILE_REQUEST_CODE=1,FileChanged=5;
    List<String> Attachments;
    List<String> Filenames;
    final static String RECORDS_FILENAME="Notes";
    Note note;
    final Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__note);

        Toolbar myToolBar=(Toolbar) findViewById(R.id.ViewNoteToolbar);
        myToolBar.setTitle("");
        setSupportActionBar(myToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         note=(Note)getIntent().getSerializableExtra("Note");
        ((EditText)findViewById(R.id.View_Title)).setText(note.Title);
        ((EditText)findViewById(R.id.ViewNot)).setText(note.Note);
        ((TextView)findViewById(R.id.ViewAttachments)).setText(ReturnAttachment(note.FileNames));
        ((TextView)findViewById(R.id.ViewAttachments)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewFiles.class);
                intent.putExtra("Note",note);
                startActivityForResult(intent,FileChanged);
            }
        });
        Attachments=note.FilePaths;
        Filenames= note.FileNames;

    }

    private String ReturnAttachment(List<String> Filenames){
        String Fnames=new String();
        Iterator iterator = Filenames.iterator();

        while(iterator.hasNext()){
            Fnames=Fnames.concat(iterator.next().toString());
            Fnames=Fnames.concat(";;");
        }
        return Fnames;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newnotebook_menu,menu);



        return true;
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
                ((TextView)findViewById(R.id.ViewAttachments)).setText(ReturnAttachment(Filenames));

            }
        }else if(requestCode==FileChanged){
            if(resultCode==RESULT_OK){
                Note nNote=(Note)data.getSerializableExtra("Note");
                if(note.FileNames.isEmpty()==false){
                    note.FileNames.clear();
                    note.FilePaths.clear();
                    if(nNote.FileNames.isEmpty()==false){
                        note.FileNames.addAll(nNote.FileNames);
                        note.FilePaths.addAll(nNote.FilePaths);
                    }
                    ((TextView)findViewById(R.id.ViewAttachments)).setText(ReturnAttachment(note.FileNames));
                }
            }
        }

    }
    private String ReturnFileName(Uri uri){
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = this.getContentResolver();
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
    private void UpdateNotes(){
        String Not=((TextView)findViewById(R.id.ViewNot)).getText().toString();
        String Baslik=((TextView)findViewById(R.id.View_Title)).getText().toString();
        note.Note=Not;
        note.Title=Baslik;
        note.FilePaths=Attachments;
        note.FileNames=Filenames;

        ArrayList<Note> notes=ReadSaved();

        int i=FindNote(note,notes);
        notes.set(i,note);

        WriteNotes(notes);

    }
    private int FindNote(Note note,ArrayList<Note> notes){
        Iterator iter=notes.iterator();
        int i=0;
        while(iter.hasNext()){
            Note tmp=(Note)iter.next();
            if((note.Date.equals(tmp.Date))&&(note.Hour.equals(tmp.Hour))){
                break;
            }
            i++;
        }
        return i;
    }

    public void WriteNotes(ArrayList<Note> notes){
        FileOutputStream fos;
        ObjectOutputStream oos=null;
        try{
            fos = getApplicationContext().openFileOutput(RECORDS_FILENAME, Context.MODE_PRIVATE);
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
            UpdateNotes();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
